/**
 * Created by codemaker88 on 2018-08-20.
 */

package chapter2

interface NotifyAble {
    fun notifyAllForLeave(leaveMember: TeamMember, requestDate: Long)
}

open class TeamMember(val name: String, val memberRequest: NotifyAble?) {
    fun applyForALeave(requestDate: Long) {
        memberRequest?.notifyAllForLeave(this, requestDate)
    }
}

class TeamLeader(name: String, memberRequest: NotifyAble? = null) : TeamMember(name, memberRequest), NotifyAble {
    val teamMembers: MutableList<TeamMember> = mutableListOf()
    var status: Status = Status.Work()

    override fun notifyAllForLeave(leaveMember: TeamMember, requestDate: Long) {
        for (teamMember in teamMembers) {
            println("[Mail Of ${teamMember.name}] ${leaveMember.name} Leave ${checkResult(requestDate)} from.${this.name}")
        }
    }

    private fun checkResult(requestDate: Long): Result {
        return when (status) {
            is Status.Leave -> {
                if (requestDate in status.startDate..status.endDate) Result.REJECT else Result.OK
            }
            else -> {
                Result.OK
            }
        }
    }
}

open class Status(val startDate: Long = 0L, val endDate: Long = 0L) {
    class Leave(startDate: Long, endDate: Long) : Status(startDate, endDate)
    class Work : Status()
}

enum class Result {
    OK, REJECT
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

    //문제 1-1
    println("[휴가 신청시 동료 직원에게 알려주기]")
    me.applyForALeave(requestDate = currentDate)

    //문제 1-2
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
