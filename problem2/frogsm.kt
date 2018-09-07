///////////////////////////////////////////////////////////////////////////////// 클래스 및 인터페이스
open class BaseMember(
        val name: String,
        val position: String,
        var vacation: String
) {
    override fun toString(): String {
        return "$name|$position|$vacation"
    }

    fun getVacationRange(): LongRange {
        val split = vacation.replace(".", "")
                .split("""[휴가(~)]""".toRegex())
                .filter { it.isNotEmpty() }
        return if (split.size == 2) {
            LongRange(split.first().toLong(), split.last().toLong())
        } else {
            LongRange.EMPTY
        }
    }
}

class TeamLeader(
        name: String,
        position: String,
        vacation: String
) : BaseMember(name, position, vacation) {
    lateinit var propagation: (String) -> Unit

    fun applyForALeave(vacation: Vacation) {
        val message = if (`내가_휴가중_인지?`() || `나와_휴가가_겹치는지?`(vacation)) {
            "${vacation.member.name} Leave REJECT"
        } else {
            "${vacation.member.name} [${vacation.period.start}~${vacation.period.last}] Leave OK"
        }
        propagation.invoke(message)
    }

    private fun `내가_휴가중_인지?`(): Boolean {
        val myPeriod = getVacationRange()
        return currentDate in myPeriod.start..myPeriod.last
    }

    private fun `나와_휴가가_겹치는지?`(memberVacation: Vacation): Boolean {
        val myPeriod = getVacationRange()
        val memberPeriod = memberVacation.period
        return when {
            myPeriod.start in memberPeriod.start..memberPeriod.last -> true
            memberPeriod.start in myPeriod.start..myPeriod.last -> true
            else -> false
        }
    }
}

class TeamMember(
        name: String,
        position: String,
        vacation: String
) : BaseMember(name, position, vacation) {

    fun onMailArrived(message: String, from: String) {
        println("[Mail Of $name] $message from.$from")
    }

    infix fun leaveUntil(date: Long): Vacation {
        return Vacation(this, currentDate..date)
    }
}

data class Team(val leader: TeamLeader, val members: List<TeamMember>) {
    init {
        leader.propagation = { message ->
            members.forEach { it.onMailArrived(message, leader.name) }
        }
    }

    override fun toString(): String {
        return (listOf(leader) + members).joinToString("\n")
    }
}

class Vacation(
        val member: TeamMember,
        val period: LongRange // 휴가 기간
)

///////////////////////////////////////////////////////////////////////////////// 최상위 프로퍼티
var currentDate: Long = 0
val rawTeamMember = "" +
        "전경주|담당|휴가(2018.02.18~2018.02.19)\n" +
        "김인혁|선임|\n" +
        "김상현|담당|\n" +
        "황인규|담당|휴가(2018.03.01~2018.03.03)\n" +
        "김지환|선임|\n" +
        "강영길|담당|휴가(2018.03.22~2018.04.01)\n" +
        "박귀남|선임|"

object CompanySystem {
    fun getRawTeamMembers(): List<StringBuilder> {
        return listOf(
                StringBuilder("A|사원|휴가(2018.04.02~2018.04.05)"),
                StringBuilder("B|과장|"),
                StringBuilder("C|차장|")
        )
    }

    fun getRawTeamLeader(): StringBuffer {
        return StringBuffer("김부장|부장|(2018.02.04~2018.02.15)")
    }
}

fun String.toBaseMember(regex: String = "|"): BaseMember {
    val name = substringBefore(regex)
    val position = substringAfter(regex).substringBefore(regex)
    val vacation = substringAfter(regex).substringAfter(regex)
    return BaseMember(name, position, vacation)
}

fun StringBuffer.toTeamLeader(): TeamLeader {
    return toString()
            .toBaseMember()
            .let { TeamLeader(it.name, it.position, it.vacation) }
}

fun String.toTeamMemberList(lineFeed: String = "\n"): List<TeamMember> {
    return split(lineFeed)
            .map { text -> text.toBaseMember().let { TeamMember(it.name, it.position, it.vacation) } }
}

fun Collection<StringBuilder>.toTeamMemberList(): List<TeamMember> {
    return map { it.toString() }
            .map { text -> text.toBaseMember().let { TeamMember(it.name, it.position, it.vacation) } }
}

///////////////////////////////////////////////////////////////////////////////// Main 함수
fun main(args: Array<String>) {
    val team = Team(leader = CompanySystem.getRawTeamLeader().toTeamLeader(),
            members = rawTeamMember.toTeamMemberList() + CompanySystem.getRawTeamMembers().toTeamMemberList()
    )

    println("[휴가 신청시 동료 직원에게 알려주기]")
    currentDate = 20180201
    val me = team.members[3]
    var vacation = me leaveUntil 20180203
    team.leader.applyForALeave(vacation)

    println("[팀장 상태에 따른 휴가 신청]")
    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180214
    vacation = me leaveUntil 201802019
    team.leader.applyForALeave(vacation)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180212
    vacation = me leaveUntil 20180218
    team.leader.applyForALeave(vacation)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    currentDate = 20180216
    vacation = me leaveUntil 20180220
    team.leader.applyForALeave(vacation)
}