class TeamLeader(val name: String) {
    val teamMembers = mutableListOf<TeamMember>()
    var status: Status = Status.Work() // 평시엔 일하는중

    fun 팀장님오늘몸이좀안좋아서휴가를쓰고싶은데요(requestDate: Long): Boolean {
        val currentStatus = status
        return when(currentStatus) {
            is Status.Work -> true
            is Status.Leave -> requestDate !in currentStatus.startDate..currentStatus.endDate
        }
    }
}

class TeamMember(val name: String, val memberRequest: TeamLeader) {
    fun applyForALeave(requestDate: Long) {
        val rejection = if(memberRequest.팀장님오늘몸이좀안좋아서휴가를쓰고싶은데요(requestDate)) "OK" else "REJECT"
        memberRequest.teamMembers.forEach {
            println("Mail Of ${it.name} $name Leave $rejection from.${memberRequest.name}")
        }
    }
}

sealed class Status {
    class Leave(val startDate: Long, val endDate: Long): Status()
    class Work : Status() // object로 쓰고 싶어요!
}

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
