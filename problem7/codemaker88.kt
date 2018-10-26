package chapter8

import java.io.File
import java.lang.RuntimeException
import java.text.NumberFormat
import java.util.concurrent.TimeUnit
import kotlin.math.pow

const val IMAGE_TXT_FILE_PATH = "./src/main/kotlin/chapter8/images.txt"
const val CAPTION_TXT_FILE_PATH = "./src/main/kotlin/chapter8/captions.txt"

val needToContain = listOf("sky", "cloud", "sunset", "dawn", "sun ", "sun.", "moon", "moon.", "sunrise")
val needToAvoid = listOf("indoor", "bathroom", "kitchen", "selfie", "basement", "bed", "video", "desk", "refrigerator", "food", "pizza", "mirror", "computer", "web", "table", "plate")

fun main(args: Array<String>) {
    val fileExists = File(IMAGE_TXT_FILE_PATH).exists() && File(CAPTION_TXT_FILE_PATH).exists()
    confirmTxtFiles { fileExists }
    makeSure { if (!fileExists) return }

    val images = File(IMAGE_TXT_FILE_PATH)
            .readLines()
            .map { val ss = it.split("\t"); Image(ss[0].toInt(), ss[1]) }
    val captions = File(CAPTION_TXT_FILE_PATH)
            .readLines()
            .map { val ss = it.split("\t"); Caption(ss[1].toInt(), ss[2]) }


    val startNanoTime = System.nanoTime()

    val imageCaptions: List<ImageCaptions> = convertToImageWithCaptions(images, captions)
    val skyImageCaptions: List<ImageCaptions> = filterImageCaptions(imageCaptions, needToContain, needToAvoid)

    val totalTime = System.nanoTime() - startNanoTime

    val nanoTransformer = getTimeTransformer(TimeUnit.NANOSECONDS)      // 1234567 ->  1,234,567 ns
    val milliTransformer = getTimeTransformer(TimeUnit.MILLISECONDS)    // 1234567 ->   1.234567 ms
    val secondTransformer = getTimeTransformer(TimeUnit.SECONDS)        // 1234567 -> 0.001234567 s
    println("spend time : ")
    println("\t${nanoTransformer(totalTime)}")
    println("\t${milliTransformer(totalTime)}")
    println("\t${secondTransformer(totalTime)}")
    println("result : size - ${skyImageCaptions.size} \t ${skyImageCaptions.last()}")
}

/*
result : size - 3685
ImageCaptions(imageId=339278,
imageUrl=http://images.cocodataset.org/train2014/COCO_train2014_000000339278.jpg,
descriptions=[
A photo of a castle like building in Europe,
The sun is setting near a decorative building.,
The majestic Chateau looks like a castle on a sunny day.,
A clock that is on a large building in the background.,
An old church with steeples shines in the sunlight.
])
 */

inline fun confirmTxtFiles(crossinline confirmation: () -> Boolean) {
    val pass = confirmation()
    if (pass) {
        /** You can change codes below here **/
        println("confirmTxtFiles : $pass")
    } else {
        println("invalid path : \"$IMAGE_TXT_FILE_PATH\" or \"$CAPTION_TXT_FILE_PATH\"")
    }
}

inline fun makeSure(confirmation: () -> Unit) {
    confirmation()
}

fun filterImageCaptions(skyImageCaptions: List<ImageCaptions>,
                        mustContain: List<String>,
                        mustNotContain: List<String>): List<ImageCaptions> {
    val mustContainRegex = mustContain.joinToString(separator = "|")
            .replace(".", "\\.")
            .toRegex()
    val mustNotContainRegex = mustNotContain.joinToString(separator = "|")
            .replace(".", "\\.")
            .toRegex()
    return skyImageCaptions.asSequence()
            .filter { it.descriptions.any { s: String -> s.contains(mustContainRegex) } }
            .filter { !it.descriptions.any { s: String -> s.contains(mustNotContainRegex) } }
            .toList()
}

fun convertToImageWithCaptions(images: List<Image>, captions: List<Caption>): List<ImageCaptions> {
    val imageMap = images.associate { it -> Pair(it.imgId, it.imgUrl) }
    return captions.groupBy({ it.imgId }, { it.description })
            .map { ImageCaptions(it.key, imageMap[it.key] ?: "", it.value) }
}

/*
fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return { ns -> "${unit.convert(ns, TimeUnit.NANOSECONDS)} ${unit.name}" }
}
*/

fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return when (unit) {
        TimeUnit.NANOSECONDS -> { ns -> "${NumberFormat.getNumberInstance().format(ns)} ns" }
        TimeUnit.MILLISECONDS -> { ns -> "${ns / 10.toFloat().pow(6)} ms" }
        TimeUnit.SECONDS -> { ns -> "${ns / 10.toFloat().pow(9)} s" }
        else -> {
            throw RuntimeException()
        }
    }
}

/* class */
data class ImageCaptions(
        val imageId: Int,
        val imageUrl: String,
        val descriptions: List<String>
)

data class Image(
        val imgId: Int,
        val imgUrl: String
)

data class Caption(
        val imgId: Int,
        val description: String
)
