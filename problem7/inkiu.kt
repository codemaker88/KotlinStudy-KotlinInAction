import java.io.File
import java.lang.RuntimeException
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val IMAGE_TXT_FILE_PATH = "D:\\ML_DATA_SET\\images.txt"
const val CAPTION_TXT_FILE_PATH = "D:\\ML_DATA_SET\\captions.txt"

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
    val secondTransformer = getTimeTransformer(TimeUnit.SECONDS)   // 1234567 -> 0.001234567 s
    println("spend time : ")
    println("\t${nanoTransformer(totalTime)}")
    println("\t${milliTransformer(totalTime)}")
    println("\t${secondTransformer(totalTime)}")
    println("result : size - ${skyImageCaptions.size} \t ${skyImageCaptions.last()}")

    filteredImageCaptionTest(skyImageCaptions)
}

inline fun confirmTxtFiles(crossinline confirmation: () -> Boolean) {
    val pass = confirmation()
    if (pass) {
        /** You can change codes below here **/
        return
    }
    throw RuntimeException("not touch here")
}

inline fun makeSure(confirmation: () -> Unit) {
    confirmation()
}

fun filterImageCaptions(skyImageCaptions: List<ImageCaptions>,
                        mustContain: List<String>,
                        mustNotContain: List<String>): List<ImageCaptions> {
    return skyImageCaptions.asSequence()
            .filter { imageCaption ->
                imageCaption.descriptions.any { description ->
                    mustContain.any { it in  description }
                } && !imageCaption.descriptions.any { description ->
                    mustNotContain.any { it in description }
                }
            }
            .toList()
}

fun convertToImageWithCaptions(images: List<Image>, captions: List<Caption>): List<ImageCaptions> {
    images.sortedBy { it.imgId }
    captions.sortedBy { it.imgId }
    val captionMap = captions.groupBy { it.imgId }
    return images.map { image ->
        ImageCaptions(
                image.imgId,
                image.imgUrl,
                captionMap[image.imgId]?.map { it.description } ?: throw RuntimeException("empty")
        )
    }
}

fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return when (unit) {
        TimeUnit.NANOSECONDS -> { ns -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(ns)} ns" }
        TimeUnit.MILLISECONDS -> { ns -> "${ns / 1000000.0} ms" }
        TimeUnit.SECONDS -> { ns -> "${ns / 1000000000.0} s" }
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

