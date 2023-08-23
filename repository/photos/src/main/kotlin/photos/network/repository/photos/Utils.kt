package photos.network.repository.photos

import photos.network.repository.photos.model.Box
import java.util.Vector

object Utils {
    /**
     * Flip the array along the diagonal
     *
     * @param data array of pixels
     * @param h Height of the data
     * @param w Width of the data
     * @param stride
     */
    fun flipDiagonal(data: FloatArray, h: Int, w: Int, stride: Int) {
        val tmp = FloatArray(w * h * stride)
        for (i in 0 until w * h * stride) {
            tmp[i] = data[i]
        }
        for (y in 0 until h) {
            for (x in 0 until w) {
                for (z in 0 until stride) {
                    data[(x * h + y) * stride + z] = tmp[(y * w + x) * stride + z]
                }
            }
        }
    }

    /**
     * Convert an array of pixels into 3-dimensional
     *
     * @param src array of pixels
     * @param dst 3-dimensional destination array
     */
    fun expand(src: FloatArray, dst: Array<Array<FloatArray>>) {
        var idx = 0
        for (y in dst.indices) {
            for (x in dst[0].indices) {
                for (c in dst[0][0].indices) {
                    dst[y][x][c] = src[idx++]
                }
            }
        }
    }

    //dst=src[:,:,1]
    fun expandProb(src: FloatArray, dst: Array<FloatArray>) {
        var idx = 0
        for (y in dst.indices) {
            for (x in dst[0].indices) {
                dst[y][x] = src[idx++ * 2 + 1]
            }
        }
    }

    /**
     * cleans the given vector by removing deleted items.
     *
     * @param faces Candidates including deleted ones
     *
     * @return sanitized candidates
     */
    fun updateBoxes(faces: Vector<Box>): Vector<Box> {
        val b = Vector<Box>()
        for (i in faces.indices) {
            if (!faces[i].deleted) {
                b.addElement(faces[i])
            }
        }
        return b
    }
}
