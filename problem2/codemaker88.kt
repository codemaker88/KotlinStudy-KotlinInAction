/**
 * Created by codemaker88 on 2018-08-20.
 */

package chapter3

import java.text.SimpleDateFormat

val intranetSystemDateFormat = SimpleDateFormat("yyyyMMdd")

enum class ConfirmResult {
    OK, REJECT
}

class Vacation(
        val member: TeamMember,
        val period: LongRange // 휴가 기간
)

data class IntranetInfo(val name: String, val rank: String, var vacation: LongRange) {
    override fun toString(): String {
        return "$name|$rank|${if (vacation == LongRange.EMPTY) "" else "휴가($vacation)"}"
    }
}

open class TeamMember(val intranetInfo: IntranetInfo) {
    infix fun leaveUntil(period: Long) = Vacation(this,
            LongRange(intranetSystemDateFormat.format(System.currentTimeMillis()).toLong(), period))

    infix fun leaveRange(period: LongRange) = Vacation(this, period)
}

class TeamLeader(intranetInfo: IntranetInfo, memberList: List<IntranetInfo>) : TeamMember(intranetInfo) {
    val teamMembers: List<TeamMember>

    init {
        teamMembers = memberList.map { memberName -> TeamMember(memberName) }.toList()
    }

    fun applyForALeave(vacationInfo: Vacation) {
        val result = checkConfirm(vacationInfo.period)
        if (result == ConfirmResult.OK) {
            vacationInfo.member.intranetInfo.vacation = vacationInfo.period
        }
        notifyAllForLeave(vacationInfo, result)
    }

    private fun checkConfirm(requestDate: LongRange): ConfirmResult {
        return if (intranetInfo.vacation != LongRange.EMPTY
                && (requestDate.first in intranetInfo.vacation || requestDate.last in intranetInfo.vacation)) {
            ConfirmResult.REJECT
        } else {
            ConfirmResult.OK
        }
    }

    private fun notifyAllForLeave(vacationInfo: Vacation, result: ConfirmResult) {
        for (teamMember in teamMembers) {
            println("[Mail Of ${teamMember.intranetInfo.name}] [${vacationInfo.period.first}~${vacationInfo.period.last}] " +
                    "${vacationInfo.member.intranetInfo.name} " + "Leave $result from.${this.intranetInfo.name}")
        }
    }
}

// ----- utils -----
fun String.parseToIntranetInfo(): IntranetInfo {
    fun parseToRange(string: String): LongRange {
        val rangesStr = string.split("~")
                .map { it.replace("""\.|휴가|\(|\)""".toRegex(), "") }
        return LongRange(rangesStr[0].toLong(), rangesStr[1].toLong())
    }

    val splitInfo = this.split("|")
    return IntranetInfo(splitInfo[0], splitInfo[1],
            if (splitInfo[2].isEmpty()) LongRange.EMPTY else parseToRange(splitInfo[2]))
}

// ----- main -----
fun main(args: Array<String>) {
    val defaultMemberList: List<IntranetInfo> = DefaultSystem.getMemberInfo().map { it.parseToIntranetInfo() }
    val systemMemberList: List<IntranetInfo> = CompanySystem.getRawTeamMembers()
            .map { it.toString().parseToIntranetInfo() }
    val systemLeaderInfo: IntranetInfo = CompanySystem.getRawTeamLeader().toString().parseToIntranetInfo()

    val leader = TeamLeader(systemLeaderInfo, memberList = defaultMemberList + systemMemberList)
    val me = leader.teamMembers[3] // 팀원 하나 선택

    println(leader.intranetInfo)

    //문제 1-1
    println("[휴가 신청시 동료 직원에게 알려주기]")
    println(me.intranetInfo)
    leader.applyForALeave(me leaveRange me.intranetInfo.vacation)//입력된 정보로 노출

    //문제 1-2
    println("[팀장 상태에 따른 휴가 신청]")
    leader.intranetInfo.vacation = LongRange(20180910, 20180912)

    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    println(leader.intranetInfo)
    var currentDate: Long = 20180909
    leader.applyForALeave(me leaveUntil currentDate)

    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    println(leader.intranetInfo)
    currentDate = 20180911
    leader.applyForALeave(me leaveUntil currentDate)

    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.intranetInfo.vacation = LongRange.EMPTY
    println(leader.intranetInfo)
    leader.applyForALeave(me leaveRange LongRange(20180913, 20180914))
}

// ----- input -----
object DefaultSystem {
    fun getMemberInfo(): List<String> {
        val defaultList = """
        전경주|담당|휴가(2018.02.18~2018.02.19)
        김인혁|선임|
        김상현|담당|
        황인규|담당|휴가(2018.03.01~2018.03.03)
        김지환|선임|
        강영길|담당|휴가(2018.03.22~2018.04.01)
        박귀남|선임|
        """
        return defaultList.split("\n").toList()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
    }
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

/*
// output
김부장|부장|
[휴가 신청시 동료 직원에게 알려주기]
황인규|담당|휴가(20180301..20180303)
[Mail Of 전경주] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 김인혁] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 김상현] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 황인규] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 김지환] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 강영길] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of 박귀남] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of A] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of B] [20180301~20180303] 황인규 Leave OK from.김부장
[Mail Of C] [20180301~20180303] 황인규 Leave OK from.김부장
[팀장 상태에 따른 휴가 신청]
1) 휴가 신청 날짜가 팀장 휴가 전일 때
김부장|부장|휴가(20180910..20180912)
[Mail Of 전경주] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 김인혁] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 김상현] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 황인규] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 김지환] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 강영길] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of 박귀남] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of A] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of B] [20180906~20180909] 황인규 Leave OK from.김부장
[Mail Of C] [20180906~20180909] 황인규 Leave OK from.김부장
2) 휴가 신청 날짜가 팀장 휴가 중일 때
김부장|부장|휴가(20180910..20180912)
[Mail Of 전경주] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 김인혁] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 김상현] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 황인규] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 김지환] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 강영길] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of 박귀남] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of A] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of B] [20180906~20180911] 황인규 Leave REJECT from.김부장
[Mail Of C] [20180906~20180911] 황인규 Leave REJECT from.김부장
3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때
김부장|부장|
[Mail Of 전경주] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 김인혁] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 김상현] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 황인규] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 김지환] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 강영길] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of 박귀남] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of A] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of B] [20180913~20180914] 황인규 Leave OK from.김부장
[Mail Of C] [20180913~20180914] 황인규 Leave OK from.김부장
 */
