package problem1

// p1
//fun main(args: Array<String>) {
//    var currentDate: Long = 20180825
//    val leader = TeamLeader(name = "TeamLeader").apply {
//        teamMembers.add(TeamMember(name = "A", memberRequest = this)) // memberRequest is TeamLeader Reference.
//        teamMembers.add(TeamMember(name = "B", memberRequest = this))
//        teamMembers.add(TeamMember(name = "C", memberRequest = this))
//        teamMembers.add(TeamMember(name = "D", memberRequest = this))
//    }
//
//    val me = leader.teamMembers[3] // 팀원 하나 선택
//
//    println("[휴가 신청시 동료 직원에게 알려주기]")
//    me.applyForALeave(requestDate = currentDate)
//}
//
//class TeamLeader(val name: String) {
//    val teamMembers = arrayListOf<TeamMember>()
//}
//
//class TeamMember(val name: String, val memberRequest: TeamLeader) {
//    fun applyForALeave(requestDate: Long) {
//        memberRequest.teamMembers.forEach {
//            println("[Mail Of ${it.name}] $name Leave OK from.${memberRequest.name}")
//        }
//    }
//}

// p2
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

// 음?
interface Status {
    class Leave(val startDate: Long, val endDate: Long): Status

    class Work: Status
}

class TeamLeader(val name: String) {
    val teamMembers = arrayListOf<TeamMember>()
    var status: Status = Status.Work()
}

class TeamMember(val name: String, val memberRequest: TeamLeader) {
    fun applyForALeave(requestDate: Long) {
        val possible = when (possible(requestDate)) {
            true -> "OK"
            false -> "REJECT"
        }
        memberRequest.teamMembers.forEach {
            println("[Mail Of ${it.name}] $name Leave $possible from.${memberRequest.name}")
        }
    }

    private fun possible(requestDate: Long): Boolean {
        val teamReaderStatus = memberRequest.status
        return when (teamReaderStatus) {
            is Status.Leave -> {
                requestDate !in teamReaderStatus.startDate .. teamReaderStatus.endDate
            }
//            is Status.Work -> {
//                true
//            }
            else -> true
        }
    }
}
