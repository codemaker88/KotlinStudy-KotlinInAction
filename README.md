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
        return StringBuilder("A|사원|휴가(2018.04.02~2018.04.05)\nB|과장|\nC|차장|")
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
