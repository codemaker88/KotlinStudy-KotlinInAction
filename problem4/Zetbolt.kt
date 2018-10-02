package problem4

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
        //1.
        //총 멤버는 3 명이상 이어야함
        //모든 멤버들중 Designer 한명이 있어야함
        //모든 멤버들의 평균 경력이 10 년 이상 이어야함
        //팀 셋팅은 LeaderOnly 가 아니어야함
        val firstTeams: List<Team> =
                teams
                        .asSequence()
                        .filter {
                            it.memberIndices.size >= 3
                        }.filter {
                            it.memberIndices
                                    .map { index ->
                                        members[index]
                                    }.singleOrNull {
                                        it.job == Job.Designer
                                    } != null
                        }.filter {
                            it.memberIndices.sumBy { members[it].years } / it.memberIndices.size.toFloat() >= 10
                        }.filter {
                            it.properties != IncentiveSettings.LeaderOnly
                        }.toList()
        println("[Found ${firstTeams.size}!]")

        //출력 firstTeams

        //2.
        //총 멤버는 3 명이상 이어야함
        //리더가 Planner 면 안됨
        //멤버의 최소 경력이 5 년 이상
        //Incentive 는 Even 이어야함
        val secondTeams: List<Team> =
                teams
                        .asSequence()
                        .filter {
                            it.memberIndices.size >= 3
                        }.filter {
                            members[it.leaderIndex].job != Job.Planner
                        }.filter {
                            it.memberIndices.firstOrNull {
                                members[it].years < 5
                            } == null
                        }.filter {
                            it.properties == IncentiveSettings.Even
                        }.toList()

        println("[Found ${secondTeams.size}!]")

        //3.
        //1. 과 2. 의 조건을 충족하고
        //총 경력이 가장 높은 팀
        val finalTeam: Team? = firstTeams.filter {
            // ??? 교집합 구한건데 맞나???
            secondTeams.contains(it)
        }.maxBy {
            it.memberIndices.sumBy { members[it].years }
        }
        finalTeam?.let {
            println("${it.teamIndex}")
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