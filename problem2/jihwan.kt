var currentDate: Long = 20180320

fun main(args: Array<String>) {
    val members = textSource.split("\n").map { it.toMember() }.toMutableList() //멤버 생성
    members.addAll(CompanySystem.getRawTeamMembers().map { it.toString().toMember() })

    val leader = CompanySystem.getRawTeamLeader().toString().toLeader(members) //리더 생성

    val me = leader.teamMembers[2] // 팀원 하나 선택

    println("[팀장 상태에 따른 휴가 신청]")

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180320
    val vacation1 = me leaveUntil 20180321
    leader.applyForALeave(vacation1)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180324
    val vacation2 = me leaveUntil 20180325
    leader.applyForALeave(vacation2)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    currentDate = 20180402
    val vacation3 = me leaveUntil 20180405
    leader.applyForALeave(vacation3)
}

class TeamLeader(val teamMembers: List<TeamMember>,
                 name: String,
                 title: String,
                 status: Status) : TeamMember(name, title, status) {

    fun applyForALeave(vacation: Vacation) {
        val decision = status.let { leader ->
            when (leader) {
                is Status.Work -> "OK"
                is Status.Leave -> if (leader.isOnVacation(vacation.period)) "REJECT" else "OK"
            }
        }

        teamMembers.forEach {
            println("[Mail Of ${it.name}] [${vacation.period.first}~${vacation.period.last}] ${vacation.member.name} Leave $decision from.$name")
        }
    }
}

open class TeamMember(val name: String, val title: String, val status: Status) {
    infix fun leaveUntil(endTime: Long): Vacation{
        return Vacation(this, currentDate..endTime)
    }

    override fun toString(): String {
        return "$name|$title|$status"
    }
}

sealed class Status {
    class Leave(private val startDate: Long, private val endDate: Long) : Status() {
        fun isOnVacation(checkDate: LongRange): Boolean = checkDate.intersect(startDate..endDate).isNotEmpty()
    }

    class Work : Status()
}

val textSource = """
전경주|담당|휴가(2018.02.18~2018.02.19)
김인혁|선임|
김상현|담당|
황인규|담당|휴가(2018.03.01~2018.03.03)
김지환|선임|
강영길|담당|휴가(2018.03.22~2018.04.01)
박귀남|선임|
""".trim()

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
        //return StringBuffer("김부장|부장|휴가(2018.03.22~2018.04.01)")
    }
}

private fun String.toLeader(members: List<TeamMember>): TeamLeader {
    val member = toMember()
    return TeamLeader(members, member.name, member.title, member.status)
}

private fun String.toMember(): TeamMember {
    return this.split("|").let {
        TeamMember(it[0], it[1], it[2].toStatus())
    }
}

private fun String.toStatus(): Status {
    return if (length > 0) this.split("~").let {
        Status.Leave(it[0].toDate(), it[1].toDate())
    } else Status.Work()
}

private fun String.toDate(): Long {
    return this.replace("[^\\d]".toRegex(), "").toLong()
}

class Vacation(
        val member: TeamMember,
        val period: LongRange // 휴가 기간
)