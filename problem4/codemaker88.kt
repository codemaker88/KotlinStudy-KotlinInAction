/**
 * Created by codemaker88 on 2018-10-01.
 */

package chapter5

import java.util.*

data class Team(
        val teamIndex: Int,
        val leaderIndex: Int,
        val memberIndices: List<Int>,
        val properties: IncentiveSettings
) {
    companion object {
        const val maxMemberCount = 5
    }
}

enum class IncentiveSettings {
    Even, FindersKeepers, LeaderOnly
}

data class Member(
        val index: Int,
        val name: String,
        val job: Job,
        val years: Int
)

enum class Job {
    Coder, Designer, Planner, QA
}

class Test {
    private val members = createMembers()
    private val teams = createTeams(members)

    fun run() {
        val 총_멤버_3명_이상 = { team: Team -> team.memberIndices.size > 3 }
        val 팀_셋팅_LeaderOnly_아님 = { team: Team -> team.properties != IncentiveSettings.LeaderOnly }
        val 리더_Planner_아님 = { team: Team -> members[team.leaderIndex].job != Job.Planner }
        val 팀_셋팅이_Even = { team: Team -> team.properties != IncentiveSettings.Even }
        val 최소_경력_5년_이상 = { team: Team ->
            team.memberIndices.asSequence()
                    .map { index -> members[index] }
                    .all { it.years >= 5 }
        }
        val 평균_경력_10년_이상이고_Designer_한명 = { team: Team ->
            val memberList = mutableListOf<Member>()
            team.memberIndices.forEach { index -> memberList.add(members[index]) }
            memberList.sumBy { it.years } / memberList.size.toFloat() > 10
                    && memberList.count { it.job == Job.Designer } == 1
        }

        //1.
        //총 멤버는 3 명이상 이어야함
        //모든 멤버들중 Designer 한명이 있어야함
        //모든 멤버들의 평균 경력이 10 년 이상 이어야함
        //팀 셋팅은 LeaderOnly 가 아니어야함
        val firstTeams: List<Team> = teams.asSequence()
                .filter(총_멤버_3명_이상)
                .filter(평균_경력_10년_이상이고_Designer_한명)
                .filter(팀_셋팅_LeaderOnly_아님)
                .toList()

        //출력 firstTeams
        println("Found ${firstTeams.size} teams!")

        //2.
        //총 멤버는 3 명이상 이어야함
        //리더가 Planner 면 안됨
        //멤버의 최소 경력이 5 년 이상
        //Incentive 는 Even 이어야함
        val secondTeams: List<Team> = teams.asSequence()
                .filter(총_멤버_3명_이상)
                .filter(리더_Planner_아님)
                .filter(최소_경력_5년_이상)
                .filter(팀_셋팅이_Even)
                .toList()

        //출력 secondTeams
        println("Found ${secondTeams.size} teams!")

        //3.
        //1. 과 2. 의 조건을 충족하고
        //총 경력이 가장 높은 팀
        val finalTeam: Team? = teams.asSequence()
                .filter(팀_셋팅_LeaderOnly_아님)
                .filter(팀_셋팅이_Even)
                .filter(리더_Planner_아님)
                .filter(총_멤버_3명_이상)
                .filter(최소_경력_5년_이상)
                .filter(평균_경력_10년_이상이고_Designer_한명)
                .firstOrNull()

        //출력 finalTeam
        println("[Found the perfect Team!]")
        finalTeam?.apply {
            memberIndices.map { index -> members[index] }
                    .forEach { member -> println("[${member.name}] Worked as ${member.job} for ${member.years} yrs") }
        }
    }
}

fun main(args: Array<String>) {
    Test().run()
}

////////////////////////////////////////////////////멤버 + 팀 생성 코드////////////////////////////////////////////////
fun createMembers(): List<Member> {
    return (0 until 10000).map {
        Member(
                it,
                "Mr.$it",
                Job.values()[(0 until Job.values().size).random()],
                (1..20).random()
        )
    }
}

fun createTeams(members: List<Member>): List<Team> {
    return members.chunked(Team.maxMemberCount)
            .map {
                it.take((1..it.size).random())
            }
            .mapIndexed { index, filteredMembers ->
                Team(
                        index,
                        filteredMembers[0].index,
                        filteredMembers.map { it.index },
                        IncentiveSettings.values()[(0 until IncentiveSettings.values().size).random()]
                )
            }
}

fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) + start

/* output ex)
Found 125 teams!
Found 164 teams!
[Found the perfect Team!]
[Mr.205] Worked as Designer for 7 yrs
[Mr.206] Worked as QA for 9 yrs
[Mr.207] Worked as QA for 10 yrs
[Mr.208] Worked as Planner for 15 yrs

Found 109 teams!
Found 139 teams!
[Found the perfect Team!]
[Mr.775] Worked as Coder for 15 yrs
[Mr.776] Worked as Planner for 15 yrs
[Mr.777] Worked as QA for 14 yrs
[Mr.778] Worked as Designer for 17 yrs
[Mr.779] Worked as QA for 14 yrs
 */