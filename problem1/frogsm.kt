class TeamLeader(private val name: String) : MemberRequest {
    val teamMembers = mutableListOf<TeamMember>()
    lateinit var status: Status

    override fun onArrivedLeaveRequest(requestDate: Long, from: String) {
        val status = status
        val message = when (status) {
            is Status.Work ->
                "$from Leave OK"
            is Status.Leave -> if (requestDate in status.startDate..status.endDate)
                "$from Leave REJECT"
            else
                "$from Leave OK"
            else -> throw IllegalStateException()
        }
        teamMembers.forEach { it.onMailArrived(message, name) }
    }
}

interface MemberRequest {
    fun onArrivedLeaveRequest(requestDate: Long, from: String)
}

class TeamMember(
        private val name: String,
        private val memberRequest: MemberRequest
) {
    fun applyForALeave(requestDate: Long) {
        memberRequest.onArrivedLeaveRequest(requestDate, name)
    }

    fun onMailArrived(message: String, from: String) {
        println("[Mail Of $name] $message from.$from")
    }
}

interface Status {
    class Work : Status
    class Leave(val startDate: Long, val endDate: Long) : Status
}

fun main(args: Array<String>) {
    var currentDate: Long = 20180825
    val leader = TeamLeader(name = "TeamLeader").apply {
        teamMembers.add(TeamMember(name = "A", memberRequest = this))
        teamMembers.add(TeamMember(name = "B", memberRequest = this))
        teamMembers.add(TeamMember(name = "C", memberRequest = this))
        teamMembers.add(TeamMember(name = "D", memberRequest = this))
    }

    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]") // 문자열 템플릿 및 전파
    leader.status = Status.Work()
    me.applyForALeave(requestDate = currentDate)

    println("[팀장 상태에 따른 휴가 신청]") // when, in, SmartCast
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
