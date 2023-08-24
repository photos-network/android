/*
 * Copyright 2020-2023 Photos.network developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package photos.network.repository.photos.model

import kotlin.math.max

class Box internal constructor() {
    // box[0] = left
    // box[1] = top
    // box[2] = right
    // box[3] = bottom
    var box: IntArray = IntArray(4)
    var score = 0f // probability

    // bounding box regression
    var bbr: FloatArray = FloatArray(4)
    var deleted = false

    // ONet output
    var landmark: Array<Landmark?> = arrayOfNulls(5)

    val left: Int
        get() = box[0]

    val right: Int
        get() = box[2]

    val top: Int
        get() = box[1]

    val bottom: Int
        get() = box[3]

    val width: Int
        get() = box[2] - box[0] + 1

    val height: Int
        get() = box[3] - box[1] + 1

    override fun toString(): String {
        return "Box { score=$score, deleted=$deleted, $left, $top, $right, $bottom, landmark=$landmark }"
    }

    val area: Int
        get() = width * height

    // Bounding Box Regression
    fun calibrate() {
        val w = box[2] - box[0] + 1
        val h = box[3] - box[1] + 1
        box[0] = (box[0] + w * bbr[0]).toInt()
        box[1] = (box[1] + h * bbr[1]).toInt()
        box[2] = (box[2] + w * bbr[2]).toInt()
        box[3] = (box[3] + h * bbr[3]).toInt()
        for (i in 0..3) bbr[i] = 0.0f
    }

    // Turn box into a square on the long-edge
    fun toSquareShape() {
        val w = width
        val h = height
        if (w > h) {
            box[1] -= (w - h) / 2
            box[3] += (w - h + 1) / 2
        } else {
            box[0] -= (h - w) / 2
            box[2] += (h - w + 1) / 2
        }
    }

    // Prevent border overflow and maintain square size
    fun limitSquare(limitWidth: Int, limitHeight: Int) {
        // left | top
        if (box[0] < 0 || box[1] < 0) {
            val len = max(-box[0], -box[1])
            box[0] += len
            box[1] += len
        }
        // right | bottom
        if (box[2] >= limitWidth || box[3] >= limitHeight) {
            val len = max(box[2] - limitWidth + 1, box[3] - limitHeight + 1)
            box[2] -= len
            box[3] -= len
        }
    }
}
