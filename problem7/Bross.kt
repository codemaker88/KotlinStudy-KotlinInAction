import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import java.text.DecimalFormat


const val IMAGE_TXT_FILE_PATH = "C:\\Users\\Bross-Internet\\IdeaProjects\\kotlin-study\\src\\main\\resources\\images.txt"
const val CAPTION_TXT_FILE_PATH = "C:\\Users\\Bross-Internet\\IdeaProjects\\kotlin-study\\src\\main\\resources\\captions.txt"

val needToContain = listOf("sky", "cloud", "sunset", "dawn", "sun ", "sun.", "moon", "moon.", "sunrise")
val needToAvoid = listOf("indoor", "bathroom", "kitchen", "selfie", "basement", "bed", "video", "desk", "refrigerator", "food", "pizza", "mirror", "computer", "web", "table", "plate")


object Cache {
    val imageCache = mutableMapOf<Int, Image>() // by image id
    val captionCache = mutableMapOf<Int, MutableList<Caption>>() // by image id
    val imageCaptionCache = mutableMapOf<Int, ImageCaptions>()

    fun initImage(images: List<Image>) {
        if (imageCache.isEmpty()) {
            images.forEach {
                imageCache[it.imgId] = it
            }
        }
    }

    fun initCaption(captions: List<Caption>) {
        if (captionCache.isEmpty()) {
            captions.forEach {
                if (captionCache.containsKey(it.imgId)) {
                    captionCache[it.imgId]?.add(it)
                } else {
                    val list = mutableListOf<Caption>()
                    list.add(it)
                    captionCache[it.imgId] = list
                }
            }
        }
    }

    fun initImageCaption(imageCaptions: List<ImageCaptions>) {
        if (imageCaptionCache.isEmpty()) {
            imageCaptions.forEach {
                imageCaptionCache[it.imageId] = it
            }
        }
    }
}


fun main(args: Array<String>) {
    val fileExists = File(IMAGE_TXT_FILE_PATH).exists() && File(CAPTION_TXT_FILE_PATH).exists()
    confirmTxtFiles { fileExists }
    makeSure { if (!fileExists) return@makeSure throw RuntimeException("file check! ") }

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
}

inline fun confirmTxtFiles(crossinline confirmation: () -> Boolean) {
    val pass = confirmation()
    if (pass) {
        /** You can change codes below here **/
        // - write code
        return println("file is exists")
    }
    throw RuntimeException("not touch here")
}

inline fun makeSure(noinline confirmation: () -> Unit) {
    confirmation()
}

fun filterImageCaptions(imageCaptions: List<ImageCaptions>,
                        mustContain: List<String>,
                        mustNotContain: List<String>): List<ImageCaptions> {
    // - implementation
    Cache.initImageCaption(imageCaptions)
    val conditionCaptions = Cache.captionCache
            .map { it.value }
            .flatMap { it -> it }
            .asSequence()
            .filter { it ->
                !it.description.split(" ").none { mustContain.contains(it) && !mustNotContain.contains(it) }
            }
            .map { it.imgId }
            .toList()

    return Cache.imageCaptionCache
            .map { it.value }
            .filter { conditionCaptions.contains(it.imageId) }
}


fun convertToImageWithCaptions(images: List<Image>, captions: List<Caption>): List<ImageCaptions> {
    // implementation
    Cache.initCaption(captions)
    Cache.initImage(images)

    return images.map { it ->
        val descriptions = Cache.captionCache[it.imgId]?.map { it.description } ?: emptyList()
        ImageCaptions(imageId = it.imgId, imageUrl = it.imgUrl, descriptions = descriptions)
    }
}

fun TimeUnit.postfix(): String {
    return when (this) {
        TimeUnit.NANOSECONDS -> "ns"
        TimeUnit.MILLISECONDS -> "ms"
        TimeUnit.SECONDS -> "s"
        else -> throw RuntimeException()
    }
}

fun Long.cipher(): Double { // 자릿수 - 1
    return (Math.log10(this.toDouble()))
}

fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return when (unit) {
        TimeUnit.NANOSECONDS ->   // - 1234567 ->  1,234,567 ns
            fun(value: Long): String {
                return "${DecimalFormat("#,##0").format(value)} ${unit.postfix()}"
            }

        TimeUnit.MILLISECONDS ->
            fun(value: Long): String {
                return "${value / Math.pow(10.toDouble(), value.cipher())} ${unit.postfix()}"
            }
        // - 1234567 ->   1.234567 ms

        TimeUnit.SECONDS -> fun(value: Long): String {
            return "0.00$value ${unit.postfix()}"
        }

        // - 1234567 -> 0.001234567 s
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
