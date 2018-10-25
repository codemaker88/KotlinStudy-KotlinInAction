# 문제[7] Chapter8 고차함수 파라미터와 반환 값으로 람다 사용

  - inline, crossinline, noinline 사용해보기
  - 고차함수를 리턴하는 함수 만들어 보기
  - 이미 빌트-인 되어있는 Kotlin standart library의 고차함수 파라미터 사용해보기 (ex: Collection.filter{ } )

### 문제 풀기 준비
- problem.md 파일과 같은 디렉토리에 images.txt, captions.txt가 있다.
- 이 파일들을 자기 컴퓨터에 편한 장소에 복사하고,
- 제일 아래 코드내 IMAGE_TXT_FILE_PATH, CAPTION_TXT_FILE_PATH를 변경해 주자.

### 문제 설명
- Task 01
  - 철수는 요즘 머신러닝에 관심이 있어서 훈련용으로 사용할 데이터를 받았다.
  - 그러나 그 데이터가 너무 방대하여 눈으로 분류하긴 귀찮아 이미지에 대한 설명(Caption)자료도 다운 받았다.
  - images.txt 파일은 이미지에 대해 ImageId와 ImageUrl이 쭉 적혀있다.
  - captions.txt 파일은 ImageId와 Description이 쭉 적혀있다.
  - 하나의 imageId에 대해서, caption의 수는 1보다 많을 수 있다.
  - 철수가 원하는 것은, images.txt와 captions.txt를 읽어서, image에 대해 caption을 모으는 것이다.
```kotlin
fun convertToImageWithCaptions(images: List<Image>, captions: List<Caption>): List<ImageCaptions> {
    // TODO - implementation
}
```
```kotlin
data class ImageCaptions(val imageId: Int, val imageUrl: String, val descriptions: List<String>)
data class Image(val imgId: Int, val imgUrl: String)
data class Caption(val imgId: Int, val description: String)
```
> images.txt<br />
> ImageId : ImageUrl<br />
> 516168 : http://images.cocodataset.org/train2014/COCO_train2014_000000516168.jpg<br />
> 475546 : http://images.cocodataset.org/train2014/COCO_train2014_000000475546.jpg<br />
> ...

> captions.txt<br />
> ImageId : Description<br />
> 516168 : a man sits in front of a table with some wine glasses on it.<br />
> 516168 : A man is sitting at a table with papers and wine goblets.<br />
> 475546 : People having a drink in a basement bar.<br />
> ...

- Task 02
  - 그리고 철수는 Task01에서 모든 List<ImageCaptions>를 이용해 자기가 원하는 사진만 고르고 싶다.
  - 철수가 원하는 사진 설명은 sky, cloud, sunset, dawn, sun, moon, sunrise 중 하나 이상이 포함되고
  - indoor, bathroom, kitchen, selfie, basement, bed, video .. 은 포함되지 않아야한다. (and 연산이다)
  - 철수가 원하는 것을 needToContain, 철수가 원하지 않는 것은 needToAvoid라 표현하겠다.
  - A tall building with a full moon behind it. (needToContain 중 moon이 있고, needToAvoid는 하나도 없다) -> Good
  - A man takes selfie under the sunrise (needToContain 중 sunrise가 있지만 selfie가 있다.) -> BAD
```Kotlin
fun filterImageCaptions(skyImageCaptions: List<ImageCaptions>,
                        mustContain: List<String>,
                        mustNotContain: List<String>): List<ImageCaptions> {
    // TODO - implementation
}
```
```Kotlin
val needToContain = listOf("sky", "cloud", "sunset", "dawn", "sun ", "sun.", "moon", "moon.", "sunrise")
val needToAvoid = listOf("indoor", "bathroom", "kitchen", "selfie", "basement", "bed", "video", "desk", "refrigerator", "food", "pizza", "mirror", "computer", "web", "table", "plate")
```

- Task 03
  - 그리고 철수는 이 작업을 보다 빠르게 하고 싶어서 벤치마크를 찍어보고 싶다.
  - Input이 nano sec인 Long을 변환하는 함수를 반환하는 함수를 쓰다 말았다.
```kotlin
fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return when (unit) {
        TimeUnit.NANOSECONDS -> // TODO - implementation
        TimeUnit.MILLISECONDS -> // TODO - implementation
        TimeUnit.SECONDS -> // TODO - implementation
        else -> {
            throw RuntimeException()
        }
    }
}
```

   - 철수가 바빠서인지, 귀차니즘이 심해서인지는 모르지만
   - 자기코드를 짜다가 TODO 만 남기고 자러 가버렸다. (그리고 컴파일 에러도 종종 보인다)
   - 누구보다 빠르게 남들과는 다르게 효율적이고 빠른 코드를 짜보자.
   - 이하는 철수가 짜던 코드이다.  /** You can change codes below here **/ 이하만 수정이 가능하다.
```Kotlin
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

const val IMAGE_TXT_FILE_PATH = "D:\\images.txt"
const val CAPTION_TXT_FILE_PATH = "D:\\captions.txt"

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

    // Test!
    //filteredImageCaptionTest(skyImageCaptions)
}

inline fun confirmTxtFiles(crossinline confirmation: () -> Boolean) {
    val pass = confirmation()
    if (pass) {
/** You can change codes below here **/
        // TODO - write code
    }
    throw RuntimeException("not touch here")
}

fun makeSure(noinline confirmation: () -> Unit) {
    confirmation()
}

fun filterImageCaptions(skyImageCaptions: List<ImageCaptions>,
                        mustContain: List<String>,
                        mustNotContain: List<String>): List<ImageCaptions> {
    // TODO - implementation
}

fun convertToImageWithCaptions(images: List<Image>, captions: List<Caption>): List<ImageCaptions> {
    // TODO - implementation
}

fun getTimeTransformer(unit: TimeUnit): (Long) -> String {
    return when (unit) {
        TimeUnit.NANOSECONDS ->   // TODO - 1234567 ->  1,234,567 ns
        TimeUnit.MILLISECONDS ->  // TODO - 1234567 ->   1.234567 ms
        TimeUnit.SECONDS ->       // TODO - 1234567 -> 0.001234567 s
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

/* test your code! */
val goodDescription1 = listOf(
        "이 문장들에는 SKY 가 있음",
        "이 문장들에는 needToAvoid 중 어느 것도 포함하지 않음",
        "철수는 SKY 가 있으니 관심이 있음"
)
val goodDescription2 = listOf(
        "이 문장들에는 SKY 가 있음",
        "이 문장들에는 또한 CLOUD 도 있음",
        "철수는 SKY 뿐만 아니라 CLOUD 도 있으니 관심이 있음"
)
val badDescription1 = listOf(
        "이 문장들에는 needToAvoid 뿐만 아니라 needToContain 중 어느 것도 포함하지 않음",
        "철수는 관심이 없음"
)
val badDescription2 = listOf(
        "이 문장들에는 SKY 가 있음",
        "이 문장들에는 INDOOR 도 있음",
        "철수는 SKY 는 있지만 INDOOR 가 있어서 관심이 없음"
)

fun filteredImageCaptionTest(imageCaptions: List<ImageCaptions>) {
    imageCaptions.forEach {
        it.descriptions.forEach { desc ->
            needToAvoid.forEach {
                if (desc.contains(it)) throw RuntimeException("'$desc' has $it")
            }
        }
    }

    imageCaptions.forEach {
        var passed = false
        it.descriptions.forEach { desc ->
            needToContain.forEach {
                if (desc.contains(it)) passed = true
            }
        }
        if (!passed) throw RuntimeException("'${it.descriptions}' hasn't any of needToContain")
    }
}
```

