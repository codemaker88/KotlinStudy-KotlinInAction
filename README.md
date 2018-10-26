# KotlinStudy-KotlinInAction
Kotlin in Action Book Study.

코틀린 스터디 깃허브 입니다.
Kotlin in action 책 기반으로 스터디 진행과 간단한 실습들을 진행합니다.
http://acornpub.co.kr/book/kotlin-in-action

## 문제1 - 휴가 신청 시스템

현재 우리회사의 휴가신청 시스템을 보면,
어느 팀의 팀장님이 휴가 신청을 하여 휴가 승인이 될 시에 원할한 업무를 위해 부서원에게 notify 해주는 시스템입니다.

위 시스템과 다르지만, 비슷하게 간단한 휴가 신청 시스템을 만들어 봅시다.

### 문제 1-1
우리회사 휴가 시스템과 다르게 생각해봅시다.
팀원이 팀장에게 휴가 신청을 내면 원할한 업무를 위해 부서원 모두에게 notify 해주도록 만들어 봅시다.

* 아래 **Main**을 참고하여 **TeamLeader(팀장) 클래스와 TeamMember(팀원) 클래스**를 생성해주세요!
* 코틀린의 **문자열 템플릿**을 이용해서 **출력**에 나와있는대로 출력해주세요!

#### Main
```text
fun main(args: Array<String>) {
    var currentDate: Long = 20180825
    val leader = TeamLeader(name = "TeamLeader").apply {
        teamMembers.add(TeamMember(name = "A", memberRequest = this)) // memberRequest is TeamLeader Reference.
        teamMembers.add(TeamMember(name = "B", memberRequest = this))
        teamMembers.add(TeamMember(name = "C", memberRequest = this))
        teamMembers.add(TeamMember(name = "D", memberRequest = this))
    }

    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]")
    me.applyForALeave(requestDate = currentDate)
}
```

#### 출력
```text
[휴가 신청시 동료 직원에게 알려주기]
[Mail Of A] D Leave OK from.TeamLeader
[Mail Of B] D Leave OK from.TeamLeader
[Mail Of C] D Leave OK from.TeamLeader
[Mail Of D] D Leave OK from.TeamLeader
```

### 문제 1-2
위에서 만들어진 TeamLeader 클래스와 TeamMember 클래스를 조금 더 확장 시켜보도록 해요.

이 휴가 시스템에서는 좀 어이없게 생각하실 수도 있지만.. 
팀장이 휴가가 아닐 때는 언제든 휴가신청을 받아주지만, 팀장이 휴가 중일 때는 전부 휴가신청을 반려합니다. (반려 또한 팀원들에게 notify 해주도록 합니다.)

* 아래 **Main**을 참고하여 **TeamLeader 클래스에 Status 멤버변수**를 추가하여 기능을 추가해 주세요!
* **Status.Work 와 Status.Leave 클래스**를 추가하여 사용하도록 합니다.
* 코틀린 문법인 **When, in, SmartCast**를 이용해서 기능을 추가해주세요!
* 아래 **출력** 처럼 출력하도록 합니다.

#### Main
```text
fun main(args: Array<String>) {
                .
                .
                .
    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180825, endDate = 20180830)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180823
    me.applyForALeave(requestDate = currentDate)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180825
    me.applyForALeave(requestDate = currentDate)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.status = Status.Work()
    me.applyForALeave(requestDate = currentDate)
}
```

#### 출력
```text
[팀장 상태에 따른 휴가 신청]
1) 휴가 신청 날짜가 팀장 휴가 전일 때
[Mail Of A] D Leave OK from.TeamLeader
[Mail Of B] D Leave OK from.TeamLeader
[Mail Of C] D Leave OK from.TeamLeader
[Mail Of D] D Leave OK from.TeamLeader
2) 휴가 신청 날짜가 팀장 휴가 중일 때
[Mail Of A] D Leave REJECT from.TeamLeader
[Mail Of B] D Leave REJECT from.TeamLeader
[Mail Of C] D Leave REJECT from.TeamLeader
[Mail Of D] D Leave REJECT from.TeamLeader
3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때
[Mail Of A] D Leave OK from.TeamLeader
[Mail Of B] D Leave OK from.TeamLeader
[Mail Of C] D Leave OK from.TeamLeader
[Mail Of D] D Leave OK from.TeamLeader
```

## 문제2 - 휴가 신청 시스템
휴가 신청 시스템이 유용하게 잘 쓰이기 시작하자, 조금씩 요구사항이 더 생기기 시작했습니다.
문제 1의 코드를 그대로 가져와서, 조금씩 개선해 보겠습니다.

문제 2-1
------------------------------------------
#### 기존 시스템 개선
- TeamMember의 생성자에서 TeamLeader 삭제
```
현재는 TeamMember에서 TeamLeader 객체를 받기 때문에, 미리 TeamMember 객체를 만드는것이 불가능합니다.
따라서 다음과 같이 수정해서 TeamLeader를 삭제합니다. 본인이 생각하기에 더 좋은 구조가 있다면 다른 방법을 사용하셔도 좋습니다.
1. applyForALeave 메소드를 TeamLeader로 이동하고, 파라미터로 TeamMember를 받도록 수정
2. TeamMember는 멤버변수로 가진 TeamLeader 제거
```
- TeamMember가 TeamLeader 생성자에서 결정되도록 변경
```
현재는 TeamLeader 객체를 만든 후에, TeamLeader의 members 변수에 TeamMember를 더하는 형식인데요.
TeamLeader 객체를 만드는 시점에 TeamMember가 결정되도록 수정해 주세요.
```

문제 2-2
--------------------------------
인재팀에서 조직도에 있는 정보를 연동해달라는 요구가 들어왔습니다.

* 기존 데이터를 잘 담을 수 있도록 자유롭게 클래스를 생성/변경/제거해 주세요.
* 기존 데이터들을 파싱해서 TeamLeader, TeamMember 객체를 만들어야 합니다.
* 조직도 정보는 텍스트와 함수 형태로 나뉘어서 제공되고 있는데, 이 데이터를 합쳐서 전부 가져와야 합니다.

#### 1. 텍스트 형태로 제공되는 조직도 정보
```text
전경주|담당|휴가(2018.02.18~2018.02.19)
김인혁|선임|
김상현|담당|
황인규|담당|휴가(2018.03.01~2018.03.03)
김지환|선임|
강영길|담당|휴가(2018.03.22~2018.04.01)
박귀남|선임|
```
#### 2. 사내 시스템을 통해 제공되는 조직도 정보 (object를 그대로 가져다가 활용해 주세요)
```kotlin
object CompanySystem {

    fun getRawTeamMembers(): List<StringBuilder> {
        return listOf(
                StringBuilder("A|사원|휴가(2018.04.02~2018.04.05)"),
                StringBuilder("B|과장|"),
                StringBuilder("C|차장|")
        )
    }

    fun getRawTeamLeader(): StringBuffer {
        return StringBuffer("김부장|부장|")
    }

}

```

#### 두 곳에서 받아온 데이터를 이용해 TeamLeader, TeamMember 객체를 만들어 주세요.
```kotlin
fun main(args: Array<String>) {
    /* 받아온 데이터를 TeamLeader 혹은 본인이 만든 클래스에 넣어주세요!
    * 받아와야 하는 총 인원은 11명입니다. */
    
    val leader = TeamLeader(name = "TeamLeader", /* 연동된 조직도 */)
}
```

문제 2-3
-----------------------------------------
- 만들어진 TeamLeader, TeamMember 객체들을 위의 *"1. 텍스트 형태로 제공되는 조직도 정보"* 텍스트 형식에 맞춰 String 으로 만드는 기능을 추가해 주세요!
- 함수 형태, 인풋, 아웃풋 등은 자유입니다.

문제 2-4
--------------------------------------------
#### 휴가신청을 의미하는 클래스가 생겼습니다. 이 클래스를 사용하도록 휴가 신청 메소드를 변경해주세요.
- 휴가 정책은 팀장님과 휴가가 하루라도 겹칠 시 반려입니다.

#### Vaction 클래스
```kotlin
class Vacation(
    val member: TeamMember,
    val period: LongRange // 휴가 기간
)
```

#### Main
```text
fun main(args: Array<String>) {
                .
                .
                .
    val me = leader.members[3]
    // 이 구문을 사용해서 vacation을 만들어 주세요.
    val vacation = me leaveUntil 20180823
    leader.applyForALeave(vacation)
                .
                .
                .
}
```

더불어, 기존 휴가신청의 포맷에 휴가 기간이 추가되어야 합니다.

#### 출력 포맷
```text
[Mail Of A] [20180302~20180304] D Leave OK from.TeamLeader
[Mail Of B] [20180302~20180304] D Leave OK from.TeamLeader
[Mail Of C] [20180302~20180304] D Leave OK from.TeamLeader
[Mail Of D] [20180302~20180304] D Leave OK from.TeamLeader
```


### 큰 틀을 벗어나지 않는 선에서 main을 포함해서 문제의 내용을 바꾸셔도 무관합니다. 3챕터에 있는 내용을 최대한 사용해 주세요 😊


## **[문제 3] Chapter4 클래스, 객체, 인터페이스** 
이번 과제는 4장의 내용을 이용하여 안드로이드에서 연습해봅시다.
최종적으로 아래의 그림과 같이 완성해 보는것이 목표입니다.

[그림 - 이번엔 이런걸 한번 해봅시다]
![problem3](https://user-images.githubusercontent.com/23000328/45427181-33527300-b6d9-11e8-8777-cbe9cab9b7ed.png)

**이번 과제로 아래의 내용들을 연습해 봅시다.**
* 인터페이스 사용해보기
* 클래스 사용해보기
* 클래스 위임 by 사용해보기
* companion object 사용해보기
* 객체식 - 여러 메소드를 오버라이드 해보기

위의 내용들이 포함 된다면 상세 구현은 마음대로 하셔도 좋습니다. 
또는 문제 이해를 위해 올려진 코드 조각을 활용하셔도 좋습니다. (주요 부분 제거)

**[문제 3-1]** 
![decoratableimageview](https://user-images.githubusercontent.com/23000328/45429231-db6a3b00-b6dd-11e8-9d2c-2228b867a6cf.png)

* DecoratableImageView
기본적으로 위의 그림과 같이 이미지를 그려주고 있는 이미지 뷰가 있습니다.
```
//activity_main.xml
<study.kotlin.problem3.view.DecoratableImageView
        android:id="@+id/image_view1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_launcher_background" />
```

이것을 처음 봤던 그림처럼 꾸밀 수 있도록 만들어 보세요.
예를들면 아래와 같습니다.
```
override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
	//꾸밀 수 있는것이 있다면 추가적으로 그려줌.
        mDecoratable?.onDraw(canvas)
    }
```
문제 이해를 돕기 위해 DecoratableImageView.kt 를 참고하세요.


**[문제 3-2]**
DecoratableImageView를 아래처럼 꾸밀 수 있도록 꾸미기 클래스들을 만들어 보세요.
```
//companion object 연습
val rect = Decorate.getDefault(Decorate.Shape.RECT)
val circle = Decorate.getDefault(Decorate.Shape.CIRCLE)
val triangle = Decorate.getDefault(Decorate.Shape.TRIANGLE)
val text1 = Decorate.getDefaultText("#1")
val text2 = Decorate.getDefaultText("#2")
val text3 = Decorate.getDefaultText("#3")

image_view1.setDecoratable(AlphaDecorate(ColorDecorate(rect, Color.RED), 100))
//순서가 바뀌어도 꾸밀 수 있음
image_view2.setDecoratable(ColorDecorate(AlphaDecorate(circle, 100), Color.GREEN))
image_view3.setDecoratable(AlphaDecorate(ColorDecorate(triangle, Color.BLUE), 255))

image_view4.setDecoratable(AlphaDecorate(ColorDecorate(text1, Color.YELLOW), 150))
//텍스트와 모양도 같이 그릴 수 있음
image_view5.setDecoratable(AlphaDecorate(ColorDecorate(CircleDecorate(text2), Color.WHITE), 150))
image_view6.setDecoratable(AlphaDecorate(ColorDecorate(TriangleDecorate(RectDecorate(text3)), Color.RED), 100))
```
문제 이해를 돕기 위해 MainActivity.kt, DecorateItems.kt 를 참고하세요.


**[문제 3-3]**
아래 처럼 각 이미지뷰들을 클릭, 롱클릭 하면 꾸며진 정보들이 로그로 출력되도록 해보세요.
```
onClick : ColorDecorate + AlphaDecorate + CircleDecorate + DefaultDecorate
onLongClick : ColorDecorate + AlphaDecorate + CircleDecorate + DefaultDecorate

onClick : AlphaDecorate + ColorDecorate + TriangleDecorate + RectDecorate + TextDecorate + DefaultDecorate
onLongClick : AlphaDecorate + ColorDecorate + TriangleDecorate + RectDecorate + TextDecorate + DefaultDecorate
```
문제 이해를 돕기 위해 MainActivity.kt 를 참고하세요.


## 문제4 - 팀 찾기

회사에 긴급 프로젝트 하나가 들어 왔습니다.
회사는 시간을 아끼기 위해 가장 빠르게 만들수 있는 팀을 고르려고 합니다.
주어진 팀들 중 주어진 조건에 가장 적합한 팀을 골라 봅시다.

### 문제 1-1 디자이너가 있는 팀들을 찾기
* 총 멤버는 3 명이상 이어야함
* 모든 멤버들중 Designer 한명이 있어야함
* 모든 멤버들의 평균 경력이 10 년 이상 이어야함
* 팀 보너스 셋팅은 LeaderOnly 가 아니어야함

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val firstTeams: List<Team> =
    //출력 firstTeams
}
```

#### 출력
```text
[Found 1,000 teams!]
```

### 문제 1-2 기획자가 팀장이 아닌 팀 찾기
* 총 멤버는 3 명이상 이어야함
* 리더가 Planner 면 안됨
* 멤버의 최소 경력이 5 년 이상
* Incentive 는 Even 이어야함

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val secondTeams: List<Team> =
    //출력 secondTeams
}
```

#### 출력
```text
[Found 1,000 teams!]
```

### 문제 1-3 가장 이상적인 팀 찾기
* 1 과 2 의 조건을 모두 충족하고
* 총 경력이 가장 높은 팀

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val finalTeam: Team? =
    //출력 finalTeam
}
```

#### 출력
```text
[Found the perfect Team!]
[Mr.1234] Worked as Coder for 10 yrs
[Mr.1222] Worked as Designer for 10 yrs
.
.
.
[Mr.1222] Worked as Planner for 1 yrs
```

## 문제5 - Simple 7 Find Game

간단한 7 찾기 게임을 만들어 봅시다.
인원이 절반으로 줄어 7명 이하 일때까지 7을 찾는 게임은 계속 됩니다.
최후의 승자의 이름과 상금을 출력해주세요.

### 7 찾기
* 게임 시작의 인원은 7미만 일 수 없습니다.
* 7 미만일 경우 RuntimeException이 발생합니다.
* 각 사용자는 사이즈만큼 생성되며, 각각 고유한 번호를 부여 받습니다.
* 게임은 사용자가 7명 미만일 경우까지 진행됩니다.
* 게임에서 승리할 경우 돈이 없는 사용자의경우 기본금액을 받습니다.
* 게임에서 승리할 경우 돈이 있는 사용자의 경우 가진 금액의 2배를 받습니다.
* 각 게임에서 이전 승자의 상금은 이후 승자에게 전달됩니다.
* 사용자들을 생성하는 코드는 아래의 코드를 사용합니다. (https://github.com/DiUS/java-faker) //compile group: 'com.github.javafaker', name: 'javafaker', version: '0.16'

```kotlin
class Person(val name: String, val number: Int, var cash: Int = 0)

fun List<Person>.findNumber(number: Int): Person? {
    return this.firstOrNull { it.number == number }
}

object PersonGenerator {
    val faker = Faker(Locale("ko"))

    fun generatePeople(size: Int): List<Person> {
        return generatePeople(size, null)
    }

    fun generatePeople(size: Int, winner: Person?): List<Person> {
        if (size < 7) {
            throw RuntimeException("size는 7이상 이여야 합니다.")
        }
        val personList = mutableListOf<Person>()
        for (index in 1..size) {
            val name = faker.name().fullName()
            val number = index
            personList.add(Person(name, number))
        }
        winner?.let { personList.add(winner) }
        return personList.subList(0, size - 2).shuffled()
    }
}

sealed class SevenFindGame {
   class WinGame: SevenFindGame()
   class LoseGame: SevenFindGame()

   companion object {
           fun startGame(size: Int) {
               val people: List<Person> = PersonGenerator.generatePeople(size)
               startGame(size, people)
           }

           private fun startGame(size: Int, people: List<Person>) {
               val winner = people.findNumber(7)!!
               if (size / 2 > 7) {
                   WinGame(winner, size / 2)
               } else {
                   WinGame(winner)
                   println("winner : $winner")
               }
               LoseGame(people.filter { it != winner })
           }
       }
}


fun main(args: Array<String>) {
    SevenFindGame.startGame(100) // 7이상의 수
}

```

### 출력 예시
```text
    winner : Person(name=강 우진, number=7, cash=80000)
```

## 문제6 - 프로퍼티 위임을 사용하여 코드를 작성해봅시다

아주 심플한 문제입니다. 각 문제의 요구사항에 맞게 코드를 작성하여
출력사항과 동일하게 출력해봅시다.

문제1. Person 클래스에 salary 프로퍼티를 생성(및 위임)하여 급여를 출력할 수 있게 해봅시다.
(위임 예제는 제공되는 코드를 잘 살펴봅시다.)

var person = PersonRepository.getPerson("1233") // 사번에 해당하는 Person을 가져옵니다.
println(person)

-> 이름 : 김인혁 / 사번 : 1233 / 급여 : 2000

문제2. 문제1에서 작성한 salary 프로퍼티 위임을 수정하여 salary가 변경될 때 결과를 출력해봅시다.
(7장 335p 리스트 7.19 ~ 7.23 예제를 참고하여 진행해봅시다)
var person = PersonRepository.getPerson("1233") // 사번에 해당하는 Person을 가져옵니다.
println(person) // -> 이름 : 김인혁 / 사번 : 1233 / 급여 : 2000
person.salary = 2000

-> 김인혁님의 급여가 1000에서 2000로 변경되었습니다.

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
    val secondTransformer = getTimeTransformer(TimeUnit.MILLISECONDS)   // 1234567 -> 0.001234567 s
    println("spend time : ")
    println("\t${nanoTransformer(totalTime)}")
    println("\t${milliTransformer(totalTime)}")
    println("\t${secondTransformer(totalTime)}")
    println("result : size - ${imageCaptions.size} \t ${imageCaptions.last()}")
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
```
