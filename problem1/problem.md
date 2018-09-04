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
```kotlin
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
```kotlin
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
