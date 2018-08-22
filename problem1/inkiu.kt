fun main(args: Array<String>) {
    // p1
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

    //p2
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

open class TeamMember(val name: String, val memberRequest: TeamLeader? = null) {

    fun applyForALeave(requestDate: Long) {
        memberRequest?.onRequestVacation(requestDate, name)
    }

    fun onMailArrived(msg: String, from: String) {
        println("[Mail Of $name] $msg from.$from")
    }
}

class TeamLeader(name: String) : TeamMember(name) {
    val teamMembers = mutableListOf<TeamMember>()
    var status: Status = Status.Work()

    fun onRequestVacation(requestDate: Long, from: String) {
        val response = if (status.isVacationRequestAcceptable(requestDate)) "OK" else "REJECT"
        teamMembers.forEach {
            it.onMailArrived("$from Leave $response", name)
        }
    }
}

sealed class Status {
    class Leave(private val startDate: Long, private val endDate: Long) : Status() {
        override fun isVacationRequestAcceptable(requestDate: Long): Boolean {
            return requestDate < startDate || requestDate > endDate
        }
    }

    class Work : Status() {
        override fun isVacationRequestAcceptable(requestDate: Long): Boolean {
            return true
        }
    }

    abstract fun isVacationRequestAcceptable(requestDate: Long): Boolean
}
