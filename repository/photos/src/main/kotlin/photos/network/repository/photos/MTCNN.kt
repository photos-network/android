package photos.network.repository.photos

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import photos.network.repository.photos.Utils.expand
import photos.network.repository.photos.Utils.expandProb
import photos.network.repository.photos.Utils.flipDiagonal
import photos.network.repository.photos.Utils.updateBoxes
import photos.network.repository.photos.model.Box
import photos.network.repository.photos.model.Landmark
import java.util.Vector
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Multi-task Cascaded Convolutional Neural Network is an algoritm for face detection and recognition.
 *
 * MTCNN is using a cascading series of neural networks to detect, align and extract faces from images.
 * Its high accuracy and speed is uncomparable with other solutions.
 *
 * This algoritm is split into 3 major steps
 *
 *  * PNET - Proposing different regions that may contain a face.
 *  * RNET - Refine the proposed regions by passing them through a series of layers to classify.
 *  * ONET - Extracts the coordinates of five facial landmarks (eyebrows, eyes, nose, mouth, jawline)
 *
 */
class MTCNN internal constructor(
    private val assetManager: AssetManager,
    private val factor: Float = 0.709f,
    private val thresholdPNet: Float = 0.6f,
    private val thresholdRNet: Float = 0.7f,
    private val thresholdONet: Float = 0.7f,
) {
    // The processing time of the last image in ms
    private var lastProcessTime: Long = 0
    private var inferenceInterface: TensorFlowInferenceInterface? = null

    init {
        loadModel()
    }

    /**
     * @param bitmap Image to detect faces
     * @param minFaceSize Smallest size of a face in pixels
     *
     * @return List of found faces
     *
     * Use [MTCNN](Multi-task Cascaded Convolutional Neural Network) to detect faces within an image.
     *
     * The larger the `minFaceSize` is, desto faster is the detection.
     */
    fun detectFaces(bitmap: Bitmap, minFaceSize: Int): Vector<Box> {
        val startTime = System.currentTimeMillis()

        // 1 • PNet generate candidate boxes
        var boxes = runPNet(bitmap, minFaceSize)
        squareLimit(boxes, bitmap.width, bitmap.height)

        // 2 • RNet
        boxes = runRNet(bitmap, boxes)
        squareLimit(boxes, bitmap.width, bitmap.height)

        // 3 • ONet
        boxes = runONet(bitmap, boxes)

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        logcat(LogPriority.INFO) { "MTCNN detection took $duration ms. Found ${boxes.size} faces." }

        lastProcessTime = System.currentTimeMillis() - startTime
        return boxes
    }

    /**
     * PNET • The Proposal Network
     *
     * It is used to generate a set of regions (bounding boxes) that may contain a face.
     * This stage takes the image input and applies a series of convolutional filters, these are then
     * processed by a set of FCN (fully convolutional network) layers to predict the existence of a face.
     *
     */
    private fun runPNet(bitmap: Bitmap, minSize: Int): Vector<Box> {
        val minWidthHeight = min(bitmap.width, bitmap.height)
        var currentFaceSize = minSize.toFloat()
        val totalBoxes = Vector<Box>()

        // 1 • Image Paramid and Feed to Pnet
        while (currentFaceSize <= minWidthHeight) {
            val scale = 12.0f / currentFaceSize

            // A • Image Resize
            val bm = bitmapResize(bitmap, scale)
            val w = bm.width
            val h = bm.height

            // B • RUN CNN
            val outWidthPNet = (ceil(w * 0.5 - 5) + 0.5).toInt()
            val outHeightPNet = (ceil(h * 0.5 - 5) + 0.5).toInt()
            val outProbPNet = Array(outHeightPNet) { FloatArray(outWidthPNet) }
            val outBiasPNet = Array(outHeightPNet) { Array(outWidthPNet) { FloatArray(4) } }
            runPNetForward(bm, outProbPNet, outBiasPNet)

            // C • data analysis
            val curBoxes = Vector<Box>()
            generateBoxes(outProbPNet, outBiasPNet, scale, thresholdPNet, curBoxes)
            logcat(LogPriority.DEBUG) { "[*]CNN Output Box number:${curBoxes.size} Scale:$scale" }

            // D • nms 0.5
            nonMaximumSuppression(curBoxes, 0.5f, "Union")

            // E • add to totalBoxes
            for (i in curBoxes.indices) {
                if (!curBoxes[i].deleted) {
                    totalBoxes.addElement(curBoxes[i])
                }
            }

            // F • Face Size proportional increase
            currentFaceSize /= factor
        }

        // 2 • NMS 0.7
        nonMaximumSuppression(totalBoxes, 0.7f, "Union")

        // 3 • BBR
        boundingBoxReggression(totalBoxes)

        return updateBoxes(totalBoxes)
    }

    /**
     * RNET • The Refinement Network
     *
     * It is used to refine the candidate regions from PNET, by cropping and resizing the corresponding
     * regions of the input image and passed through a series of convolutional and fully connected
     * layers to classify each bounding box as a face or non-face.
     *
     */
    private fun runRNet(bitmap: Bitmap, boxes: Vector<Box>): Vector<Box> {
        val num = boxes.size
        val RNetIn = FloatArray(num * 24 * 24 * 3)
        val curCrop = FloatArray(24 * 24 * 3)
        var RNetInIdx = 0

        for (i in 0 until num) {
            cropAndResize(bitmap, boxes[i], 24, curCrop)
            flipDiagonal(curCrop, 24, 24, 3)
            //Log.i(TAG,"[*]Pixels values:"+curCrop[0]+" "+curCrop[1]);
            for (j in curCrop.indices) RNetIn[RNetInIdx++] = curCrop[j]
        }

        //Run RNet
        runRNetForward(RNetIn, boxes)

        //RNetThreshold
        for (i in 0 until num) {
            if (boxes[i].score < thresholdRNet) {
                boxes[i].deleted = true
            }
        }

        //Nms
        nonMaximumSuppression(boxes, 0.7f, "Union")

        boundingBoxReggression(boxes)

        return updateBoxes(boxes)
    }

    /**
     * ONET • The Output Network
     *
     * It is used to further refine the bounding boxes and extract the facial landmarks by regressing
     * the coordinates  of the bounding box to further refine the location of the detected face and
     * extracts the coordinates of five facial landmarks.
     */
    private fun runONet(bitmap: Bitmap, boxes: Vector<Box>): Vector<Box> {
        val num = boxes.size
        val ONetIn = FloatArray(num * 48 * 48 * 3)
        val curCrop = FloatArray(48 * 48 * 3)
        var ONetInIdx = 0

        for (i in 0 until num) {
            cropAndResize(bitmap, boxes[i], 48, curCrop)
            flipDiagonal(curCrop, 48, 48, 3)
            for (j in curCrop.indices) ONetIn[ONetInIdx++] = curCrop[j]
        }

        //Run ONet
        runONetForward(ONetIn, boxes)

        //ONetThreshold
        for (i in 0 until num) {
            if (boxes[i].score < thresholdONet) {
                boxes[i].deleted = true
            }
        }

        boundingBoxReggression(boxes)

        //Nms
        nonMaximumSuppression(boxes, 0.7f, "Min")

        return updateBoxes(boxes)
    }

    // The input is flipped before the output is flipped
    private fun runPNetForward(
        bitmap: Bitmap,
        outProbPNet: Array<FloatArray>,
        outBiasPNet: Array<Array<FloatArray>>
    ): Int {
        val w = bitmap.width
        val h = bitmap.height
        val PNetIn = normalizeImage(bitmap)

        flipDiagonal(PNetIn, h, w, 3) //Flip along the diagonal

        inferenceInterface!!.feed(PNetInName, PNetIn, 1, w.toLong(), h.toLong(), 3)
        inferenceInterface!!.run(PNetOutName, false)

        val PNetOutSizeW = Math.ceil(w * 0.5 - 5).toInt()
        val PNetOutSizeH = Math.ceil(h * 0.5 - 5).toInt()
        val PNetOutP = FloatArray(PNetOutSizeW * PNetOutSizeH * 2)
        val PNetOutB = FloatArray(PNetOutSizeW * PNetOutSizeH * 4)

        inferenceInterface!!.fetch(PNetOutName[0], PNetOutP)
        inferenceInterface!!.fetch(PNetOutName[1], PNetOutB)

        //[Writing 1] Flip first, then convert to a 2/3 dimensional array
        flipDiagonal(PNetOutP, PNetOutSizeW, PNetOutSizeH, 2)
        flipDiagonal(PNetOutB, PNetOutSizeW, PNetOutSizeH, 4)
        expand(PNetOutB, outBiasPNet)
        expandProb(PNetOutP, outProbPNet)

        return 0
    }

    /*
     * RNET Run the neural network, write the score and bias into boxes
     */
    private fun runRNetForward(inputFromRNet: FloatArray, boxes: Vector<Box>) {
        val num = inputFromRNet.size / 24 / 24 / 3

        // feed & run
        inferenceInterface!!.feed(RNetInName, inputFromRNet, num.toLong(), 24, 24, 3)
        inferenceInterface!!.run(RNetOutName, false)

        // fetch
        val RNetP = FloatArray(num * 2)
        val RNetB = FloatArray(num * 4)
        inferenceInterface!!.fetch(RNetOutName[0], RNetP)
        inferenceInterface!!.fetch(RNetOutName[1], RNetB)

        // convert
        for (i in 0 until num) {
            boxes[i].score = RNetP[i * 2 + 1]
            for (j in 0..3) {
                boxes[i].bbr[j] = RNetB[i * 4 + j]
            }
        }
    }

    /*
     * ONet Run the neural network, write the score and bias into boxes
     */
    private fun runONetForward(inputFromONet: FloatArray, boxes: Vector<Box>) {
        val num = inputFromONet.size / 48 / 48 / 3

        // feed & run
        inferenceInterface!!.feed(ONetInName, inputFromONet, num.toLong(), 48, 48, 3)
        inferenceInterface!!.run(ONetOutName, false)

        // fetch
        val ONetP = FloatArray(num * 2) // prob
        val ONetB = FloatArray(num * 4) // bias
        val ONetL = FloatArray(num * 10) // landmark
        inferenceInterface!!.fetch(ONetOutName[0], ONetP)
        inferenceInterface!!.fetch(ONetOutName[1], ONetB)
        inferenceInterface!!.fetch(ONetOutName[2], ONetL)

        // convert
        for (i in 0 until num) {
            //prob
            boxes[i].score = ONetP[i * 2 + 1]
            //bias
            for (j in 0..3) {
                boxes[i].bbr[j] = ONetB[i * 4 + j]
            }

            // landmark
            for (j in 0..4) {
                val x: Int = boxes[i].left + (ONetL[i * 10 + j] * boxes[i].width).toInt()
                val y: Int = boxes[i].top + (ONetL[i * 10 + j + 5] * boxes[i].height).toInt()
                boxes[i].landmark[j] = Landmark(
                    eyebrows = PointF(),
                    eyes = PointF(),
                    nose = PointF(x.toFloat(), y.toFloat()),
                    mouth = PointF(),
                    jawline = PointF(),
                )
                logcat(LogPriority.VERBOSE) { "[*] landmark: ${boxes[i].landmark[j]}" }
            }
        }
    }

    /**
     * Load tensorflow model from assets
     */
    private fun loadModel() {
        try {
            inferenceInterface = TensorFlowInferenceInterface(assetManager, MODEL_FILE)
        } catch (exception: Exception) {
            logcat(LogPriority.ERROR) { "Failed to load $MODEL_FILE" }
            logcat(LogPriority.ERROR) { exception.asLog() }
        }
    }

    /**
     * Convert the given bitmap into a 1-dimensional array
     *
     * @param bitmap Input image
     *
     * @return 1-dimensional array of pixels
     */
    private fun normalizeImage(bitmap: Bitmap): FloatArray {
        val w = bitmap.width
        val h = bitmap.height
        val floatValues = FloatArray(w * h * 3)
        val intValues = IntArray(w * h)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val imageMean = 127.5f
        val imageStd = 128f

        for (i in intValues.indices) {
            val `val` = intValues[i]
            floatValues[i * 3 + 0] = ((`val` shr 16 and 0xFF) - imageMean) / imageStd
            floatValues[i * 3 + 1] = ((`val` shr 8 and 0xFF) - imageMean) / imageStd
            floatValues[i * 3 + 2] = ((`val` and 0xFF) - imageMean) / imageStd
        }

        return floatValues
    }

    /**
     * Resize the given image
     *
     * @param bitmap Input image
     * @param scale Factor to scale the image with
     *
     * @return downsized image
     */
    private fun bitmapResize(
        bitmap: Bitmap,
        scale: Float
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val matrix = Matrix()

        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(
            /* source = */ bitmap,
            /* x = */ 0,
            /* y = */ 0,
            /* width = */ width,
            /* height = */ height,
            /* m = */ matrix,
            /* filter = */ true
        )
    }

    /**
     * Non-Maximum Suppression
     *
     * Delete overlapping candidates by comparing pairwise
     */
    private fun nonMaximumSuppression(boxes: Vector<Box>, threshold: Float, method: String) {
        var deleteCount = 0
        for (i in boxes.indices) {
            val box = boxes[i]
            if (!box.deleted) {
                // score <0 Indicates that the current rectangle is deleted
                for (j in i + 1 until boxes.size) {
                    val box2 = boxes[j]
                    if (!box2.deleted) {
                        val x1 = max(box.box[0], box2.box[0])
                        val y1 = max(box.box[1], box2.box[1])
                        val x2 = min(box.box[2], box2.box[2])
                        val y2 = min(box.box[3], box2.box[3])

                        if (x2 < x1 || y2 < y1) {
                            continue
                        }

                        val areaIoU = (x2 - x1 + 1) * (y2 - y1 + 1)
                        var iou = 0f

                        if (method == "Union") {
                            iou =
                                1.0f * areaIoU / (box.area + box2.area - areaIoU)
                        } else {
                            if (method == "Min") {
                                iou = 1.0f * areaIoU / min(box.area, box2.area)
                                logcat(LogPriority.VERBOSE) { "[*]iou=$iou" }
                            }
                        }
                        if (iou >= threshold) {
                            // Delete the box with the smaller prob
                            if (box.score > box2.score) {
                                box2.deleted = true
                            } else {
                                box.deleted = true
                            }
                            deleteCount++
                        }
                    }
                }
            }
        }
        logcat(LogPriority.DEBUG) { "compared ${boxes.size} candidates and removed $deleteCount overlapping candidates." }
    }

    private fun generateBoxes(
        prob: Array<FloatArray>,
        bias: Array<Array<FloatArray>>,
        scale: Float,
        threshold: Float,
        boxes: Vector<Box>
    ) {
        val h = prob.size
        val w = prob[0].size
        (0 until h).forEach { y ->
            (0 until w).forEach { x ->
                val score = prob[y][x]

                //only accept prob >threadshold(0.6 here)
                if (score > threshold) {
                    val box = Box()

                    //score
                    box.score = score

                    //box
                    box.box[0] = (x * 2 / scale).roundToInt()
                    box.box[1] = (y * 2 / scale).roundToInt()
                    box.box[2] = ((x * 2 + 11) / scale).roundToInt()
                    box.box[3] = ((y * 2 + 11) / scale).roundToInt()

                    //bbr
                    (0..3).forEach { i ->
                        box.bbr[i] = bias[y][x][i]
                    }

                    //add
                    boxes.addElement(box)
                }
            }
        }
    }

    private fun boundingBoxReggression(boxes: Vector<Box>) {
        boxes.indices.forEach { i ->
            boxes[i].calibrate()
        }
    }

    private fun cropAndResize(bitmap: Bitmap, box: Box, size: Int, data: FloatArray) {
        // crop and resize
        val matrix = Matrix()
        val scale: Float = 1.0f * size / box.width
        matrix.postScale(scale, scale)

        val croped = Bitmap.createBitmap(
            bitmap,
            box.left,
            box.top,
            box.width,
            box.height,
            matrix,
            true
        )

        // save
        val pixelsBuffer = IntArray(size * size)
        croped.getPixels(pixelsBuffer, 0, croped.width, 0, 0, croped.width, croped.height)
        val imageMean = 127.5f
        val imageStd = 128f

        pixelsBuffer.indices.forEach { i ->
            val value = pixelsBuffer[i]
            data[i * 3 + 0] = ((value shr 16 and 0xFF) - imageMean) / imageStd
            data[i * 3 + 1] = ((value shr 8 and 0xFF) - imageMean) / imageStd
            data[i * 3 + 2] = ((value and 0xFF) - imageMean) / imageStd
        }
    }

    /**
     * Create squares for each box by keeping the limits
     */
    private fun squareLimit(boxes: Vector<Box>, w: Int, h: Int) {
        for (i in boxes.indices) {
            boxes[i].toSquareShape()
            boxes[i].limitSquare(w, h)
        }
    }

    companion object {
        private const val MODEL_FILE = "file:///android_asset/mtcnn_freezed_model.pb"

        private const val PNetInName = "pnet/input:0"
        private val PNetOutName = arrayOf("pnet/prob1:0", "pnet/conv4-2/BiasAdd:0")
        private const val RNetInName = "rnet/input:0"
        private val RNetOutName = arrayOf("rnet/prob1:0", "rnet/conv5-2/conv5-2:0")
        private const val ONetInName = "onet/input:0"
        private val ONetOutName =
            arrayOf("onet/prob1:0", "onet/conv6-2/conv6-2:0", "onet/conv6-3/conv6-3:0")
    }
}
