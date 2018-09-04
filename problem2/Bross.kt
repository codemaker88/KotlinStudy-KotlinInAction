
data class TeamMember(val name: String, var leader: TeamLeader?) {
    fun applyForALeave(vacation: Vacation) {
        leader?.let {
            //            [Mail Of A] [20180302~20180304] D Leave OK from.TeamLeader
            val leader = leader!!

            val confirm = when (leader.status) {
                is Status.Work -> "OK"
                is Status.Leave ->
                    if ((leader.status as Status.Leave).start <= vacation.period.start && vacation.period.start <= (leader.status as Status.Leave).end) {
                        "REJECT"
                    } else "OK"
            }

            leader.teamMembers.forEach {
                println("[Mail Of ${it.name}] $vacation $confirm from.TeamLeader")
            }

        }
    }
}

sealed class Status {
    class Work : Status()
    data class Leave(val start: Long, val end: Long) : Status()
}

data class TeamLeader(val name: String, var teamMembers: List<TeamMember>, var status: Status = Status.Work())

class Vacation(
        val member: TeamMember,
        val period: LongRange // 휴가 기간
) {
    override fun toString(): String {
        return "[${period.first}~${period.last}] ${member.name} Leave"
    }
}

fun StringBuilder.parseTeamMember(): TeamMember {
    val teamMemberInfo = this.toString().split("|").iterator()
    val name = teamMemberInfo.next()
    val position = teamMemberInfo.next()
    val vaction = teamMemberInfo.next()
    return TeamMember(name, null)
}

fun StringBuffer.parseTeamLeader(teamMembers: List<TeamMember>): TeamLeader {
    val teamMemberInfo = this.toString().split("|").iterator()
    val name = teamMemberInfo.next()
    return TeamLeader(name, teamMembers = teamMembers)
}

object InHouseSystem {
    fun StringBuilder(str: String): List<StringBuilder> = str.split("\n").map { it -> java.lang.StringBuilder(it) }


    fun getRawTeamMembers(): List<StringBuilder> {
        return StringBuilder("A|사원|휴가(2018.04.02~2018.04.05)\nB|과장|\nC|차장|")
    }

    fun getRawTeamLeader(): StringBuffer {
        return StringBuffer("김부장|부장|")
    }

    private fun String.toDate(): Date {
        return SimpleDateFormat("yyyy.mm.dd").parse(this)
    }

    fun TeamMember.parseVaction(str: String): Vacation {
        val splits = str.split("[()]".toRegex())
        val list = splits[1].split("~").toList()
        val startTime = list.first().toDate().time
        val endTime = list.last().toDate().time
        return Vacation(this, LongRange(startTime, endTime))
    }
}


val text = """
전경주|담당|휴가(2018.02.18~2018.02.19)
김인혁|선임|
김상현|담당|
황인규|담당|휴가(2018.03.01~2018.03.03)
김지환|선임|
강영길|담당|휴가(2018.03.22~2018.04.01)
박귀남|선임|"""

operator fun <T, C : Collection<T>> List<C>.plus(toAppend: C): List<C> {
    val ret = java.util.ArrayList<C>(this.size + 1)
    ret.addAll(this)
    ret.add(toAppend)
    return ret
}

fun Date.nowDay(): Long {
    return SimpleDateFormat("yyyymmdd").format(this).toLong()
}

infix fun TeamMember.leaveUntil(endTime: Long): Vacation {
    return Vacation(this, LongRange(Date().nowDay(), endTime))
}

fun main(args: Array<String>) {

    val list = InHouseSystem.getRawTeamMembers() + text.split("\n").map { it -> StringBuilder(it) }
    val teamMembers = list.filter { it -> it.isNotBlank() }.map { it -> it.parseTeamMember() }
    val leader = InHouseSystem.getRawTeamLeader().parseTeamLeader(teamMembers)
    teamMembers.forEach { it.leader = leader }
    val me = leader.teamMembers.shuffled().first()
    val vacation = me leaveUntil 20180823
    me.applyForALeave(vacation)

}
