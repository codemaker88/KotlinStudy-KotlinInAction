fun main(args: Array<String>) {
    val leader = TeamMemberService.getLeader()
    val me = leader.members[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]")
    leader.applyForALeave(me leaveFrom 20180907 leaveUntil 20180907)

    //p2
    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180906, endDate = 20180908)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    leader.applyForALeave(me leaveFrom 20180904 leaveUntil 20180905)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    leader.applyForALeave(me leaveFrom 20180905 leaveUntil 20180907)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.applyForALeave(me leaveFrom 20180909 leaveUntil 20180910)

    println("2. 만들어진 TeamLeader, TeamMember 객체들을 위의 \"1. 텍스트 형태로 제공되는 조직도 정보\" 텍스트 형식에 맞춰 String 으로 만드는 기능을 추가해 주세요!")
    TeamMemberService.memberToPlainText(listOf(
            leader,
            *leader.members.toTypedArray()
    ))
}

open class TeamMember(val name: String, val position: String) : MailReceptible {
    var vacationInfo: String = ""
    var status: Status = Status.Work()

    override fun onMailArrived(msg: String, from: String) {
        println("[Mail Of $name] $msg from.$from")
    }
}

class TeamLeader(name: String, position: String, val members: List<TeamMember>) : TeamMember(name, position), VacationReceptible {
    override fun applyForALeave(vacation: Vacation) {
        val status = status
        val response = when (status) {
            is Status.Leave -> {
                if (status.toLongRange().intersect(vacation.period).isNotEmpty()) "REJECT" else "OK"
            }
            is Status.Work -> {
                "OK"
            }
        }
        members.forEach {
            it.onMailArrived("[${vacation.toPrintFormat()}] ${vacation.member.name} Leave $response ", name)
        }
    }
}

interface VacationReceptible {
    fun applyForALeave(vacation: Vacation)
}

interface MailReceptible {
    fun onMailArrived(msg: String, from: String)
}

class Vacation(
        val member: TeamMember,
        val period: LongRange
)

sealed class Status {
    class Leave(private val startDate: Long, private val endDate: Long) : Status() {
        fun toLongRange(): LongRange = startDate..endDate
    }
    class Work : Status()
}

object TeamMemberService {
    fun getLeader(): TeamLeader {
        return parse(CompanySystem.getRawTeamLeader().toString()).let {
            TeamLeader(it.name, it.position, getMembers())
        }
    }

    fun getMembers(): List<TeamMember> {
        return listOf(
                *parseEnterSeparate(plainText).toTypedArray(),
                *parseStringBuilders(CompanySystem.getRawTeamMembers()).toTypedArray()
        )
    }

    fun memberToPlainText(members: List<TeamMember>) {
        println(
                members.map { "${it.name}|${it.position}|${it.vacationInfo}" }
                        .fold(StringBuilder()) { acc, next ->
                            acc.append(next)
                            acc.append("\n")
                        }.toString().trimIndent()
        )
    }

    private fun parseStringBuilders(builders: List<StringBuilder>): List<TeamMember> {
        return builders.map { parse(it.toString()) }
    }

    private fun parseEnterSeparate(enterSeparatedString: String): List<TeamMember> {
        return enterSeparatedString.apply { trimIndent() }.split("\n").map { parse(it) }
    }

    private fun parse(string: String): TeamMember {
        return string.split("|").let {
            TeamMember(it[0], it[1]).apply { if (it.size > 2) vacationInfo = it[2] }
        }
    }
}

/* APIs */
val plainText = """
        전경주|담당|휴가(2018.02.18~2018.02.19)
        김인혁|선임|
        김상현|담당|
        황인규|담당|휴가(2018.03.01~2018.03.03)
        김지환|선임|
        강영길|담당|휴가(2018.03.22~2018.04.01)
        박귀남|선임|
""".trimIndent()

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

/* extensions */
//private fun StringBuilder(s: String) = s.split("\n").map { java.lang.StringBuilder(it) }
private fun Vacation.toPrintFormat() = "${period.start}~${period.endInclusive}"
private infix fun TeamMember.leaveFrom(from: Long) = Pair(this, from)
private infix fun Pair<TeamMember, Long>.leaveUntil(until: Long) = Vacation(first, second..until)
