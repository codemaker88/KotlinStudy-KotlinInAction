const val currentDate: Long = 20180825

fun main(args: Array<String>) {

    val raw = """
        전경주|담당|휴가(2018.02.18~2018.02.19)
        김인혁|선임|
        김상현|담당|
        황인규|담당|휴가(2018.03.01~2018.03.03)
        김지환|선임|
        강영길|담당|휴가(2018.03.22~2018.04.01)
        박귀남|선임|
    """.trimIndent()

    val lines = raw.split("\n")
    val members = lines.map { it.toTeamMember() }.toTypedArray()
    val members2 = CompanySystem.getRawTeamMembers().map { it.toTeamMember() }.toTypedArray()
    val leaderAsMember = CompanySystem.getRawTeamLeader().toTeamMember()


    val leader = TeamLeader(
            leaderAsMember.name,
            leaderAsMember.position,
            *members, *members2, leaderAsMember
    )

    val organizationText = leader.teamMembers.joinToString(separator = "\n")
    println(organizationText)

    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]")
    leader.applyForALeave(me leaveUntil currentDate)

    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180825, endDate = 20180830)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")

    leader.applyForALeave(me leaveUntil 20180823)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    leader.applyForALeave(me leaveUntil 20180825)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.status = Status.Work
    leader.applyForALeave(me leaveUntil 20180825)
}

open class TeamMember(
        val name: String,
        val position: String = "",
        var status: Status = Status.Work // 평시엔 일하는중
) {
    override fun toString(): String {
        return "$name|$position|$status"
    }
}

class TeamLeader(
        name: String,
        position: String = "",
        vararg val teamMembers: TeamMember
) : TeamMember(name, position) {
    private fun 팀장님오늘몸이좀안좋아서휴가를쓰고싶은데요(period: LongRange): Boolean {
        val (start, end) = period

        val currentStatus = status
        return when (currentStatus) {
            is Status.Work -> true
            is Status.Leave -> end < currentStatus.startDate || start > currentStatus.endDate
        }
    }

    fun applyForALeave(vacation: Vacation) {
        val rejection = if (팀장님오늘몸이좀안좋아서휴가를쓰고싶은데요(vacation.period)) "OK" else "REJECT"
        teamMembers.forEach {
            println("Mail Of ${it.name} [${vacation.period.toString().replace("..", "~")}] ${vacation.member.name} Leave $rejection from.$name")
        }
    }

    operator fun LongRange.component1() = start
    operator fun LongRange.component2() = endInclusive
}

sealed class Status {

    class Leave(val startDate: Long, val endDate: Long) : Status() {
        override fun toString(): String {
            return "휴가(${startDate.convertToDateFormat()}~${endDate.convertToDateFormat()})"
        }
    }

    object Work : Status() {
        override fun toString(): String = ""
    }

}

fun Long.convertToDateFormat(): String {
    val str = this.toString()
    return "${str.substring(0, 4)}.${str.substring(4, 6)}.${str.substring(6, 8)}"
}

fun String.toStatus(): Status {
    return if (this.contains("휴가")) {
        try {
            val (start, end) = this.substring(3, this.lastIndex).split("~").map { it.replace(".", "").toLong() }
            Status.Leave(start, end)
        } catch (e: Exception) {
            throw RuntimeException("잘못된 형식입니다.", e)
        }
    } else {
        Status.Work
    }
}

fun CharSequence.toTeamMember(): TeamMember {
    val (name, position, strStatus) = this.split("|")
    return TeamMember(name, position, strStatus.toStatus())
}

class Vacation(
        val member: TeamMember,
        val period: LongRange
)

infix fun TeamMember.leaveUntil(endDate: Long): Vacation {
    return Vacation(this, currentDate..endDate)
}

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
