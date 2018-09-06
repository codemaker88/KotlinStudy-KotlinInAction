package problem2

// P2-1
/*
fun main(args: Array<String>) {
    var currentDate: Long = 20180825
    val leader = TeamLeader("TeamLeader",
            mutableListOf(
                    TeamMember(name = "A"), // memberRequest is TeamLeader Reference.
                    TeamMember(name = "B"),
                    TeamMember(name = "C"),
                    TeamMember(name = "D")))


    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]")
    leader.applyForALeave(me, currentDate)

    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180825, endDate = 20180830)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180823
    leader.applyForALeave(me, currentDate)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180825
    leader.applyForALeave(me, currentDate)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.status = Status.Work()
    leader.applyForALeave(me, currentDate)
}

// 음?
interface Status {
    class Leave(val startDate: Long, val endDate: Long): Status

    class Work: Status
}

class TeamLeader(val name: String, val teamMembers: List<TeamMember>) {
    var status: Status = Status.Work()

    fun applyForALeave(member: TeamMember, requestDate: Long) {
        val possible = when (possible(requestDate)) {
            true -> "OK"
            false -> "REJECT"
        }
        teamMembers.forEach {
            println("[Mail Of ${it.name}] ${member.name} Leave $possible from.$name")
        }
    }

    private fun possible(requestDate: Long): Boolean {
        val teamReaderStatus = status
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

class TeamMember(val name: String)
*/


var currentDate: Long = 20180823

fun main(args: Array<String>) {
    /* 받아온 데이터를 TeamLeader 혹은 본인이 만든 클래스에 넣어주세요!
    * 받아와야 하는 총 인원은 11명입니다. */

    val teamMembers = mutableListOf<TeamMember>()
    CompanySystem.getRawTeamMembers().forEach {
        val memberInfo = CompanySystem.parseRawData(it.toString())
        memberInfo?.apply {
            teamMembers.add(TeamMember(memberInfo.name, memberInfo.rank, memberInfo.vacation))
        }
    }

    val teamLeaderInfo = CompanySystem.parseRawData(CompanySystem.getRawTeamLeader().toString())
            ?: throw RuntimeException("Raw data is invalid")

    val leader = TeamLeader(teamLeaderInfo.name, teamLeaderInfo.rank,  teamLeaderInfo.vacation, teamMembers)

    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[휴가 신청시 동료 직원에게 알려주기]")
    var vacation = me leaveUntil 20180825
    leader.applyForALeave(vacation)

    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180825, endDate = 20180830)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180823
    vacation = me leaveUntil 20180824
    leader.applyForALeave(vacation)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180824
    vacation = me leaveUntil 20180827
    leader.applyForALeave(vacation)
    currentDate = 20180826
    vacation = me leaveUntil 20180831
    leader.applyForALeave(vacation)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.status = Status.Work()
    leader.applyForALeave(vacation)

    leader.teamMembers.forEach {
        println(it.info())
    }
}

interface Status {
    class Leave(val startDate: Long, val endDate: Long): Status

    class Work: Status
}

open class TeamMember(val name: String, val rank: String, val vacation: String)

//P2-3
fun TeamMember.info(): String {
    return "$name|$rank|$vacation"
}

infix fun TeamMember.leaveUntil(endDate: Long): Vacation {
    return Vacation(this, LongRange(currentDate, endDate))
}

class TeamLeader(
        name: String,
        rank: String,
        vacation: String,
        val teamMembers: List<TeamMember>
): TeamMember(name, rank, vacation) {
    var status: Status = Status.Work()

    fun applyForALeave(vacation: Vacation) {
        val possible = when (possible(vacation.period)) {
            true -> "OK"
            false -> "REJECT"
        }
        teamMembers.forEach {
            println("[Mail Of ${it.name}] [${vacation.period.start}~${vacation.period.endInclusive}] ${vacation.member.name} Leave $possible from.$name")
        }
    }

    private fun possible(period: LongRange): Boolean {
        val teamReaderStatus = status
        return when (teamReaderStatus) {
            is Status.Leave -> {
                period.start !in teamReaderStatus.startDate .. teamReaderStatus.endDate &&
                        period.endInclusive !in teamReaderStatus.startDate .. teamReaderStatus.endDate
            }
            else -> true
        }
    }
}

class Vacation(
        val member: TeamMember,
        val period: LongRange
)

object CompanySystem {

    fun getRawTeamMembers(): List<StringBuilder> {
        return listOf(
                StringBuilder("A|사원|휴가(2018.04.02~2018.04.05)"),
                StringBuilder("B|과장|"),
                StringBuilder("C|차장|"),
                StringBuilder("전경주|담당|휴가(2018.02.18~2018.02.19)"),
                StringBuilder("김인혁|선임|"),
                StringBuilder("김상현|담당|"),
                StringBuilder("황인규|담당|휴가(2018.03.01~2018.03.03)"),
                StringBuilder("김지환|선임|"),
                StringBuilder("강영길|담당|휴가(2018.03.22~2018.04.01)"),
                StringBuilder("박귀남|선임|")
        )
    }

    fun getRawTeamLeader(): StringBuffer {
        return StringBuffer("김부장|부장|")
    }

    fun parseRawData(rawData: String): MemberInfo? {
        val regex = """(.+)\|(.+)\|(.*)""".toRegex()
        val matchResult = regex.matchEntire(rawData)

        return matchResult?.let {
            val (name, rank, vacation) = it.destructured
            MemberInfo(name, rank, vacation)
        }
    }
}

class MemberInfo(val name: String, val rank: String, val vacation: String)
