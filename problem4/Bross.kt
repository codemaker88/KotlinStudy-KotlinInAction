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

        val firstTeams: List<Team> = teams.filter { it ->
            val team = it
            val members = team.memberIndices.map { members[it] }
            val designer = members.filter { it.job == Job.Designer }
            val years = members.sumBy { it.years }
            val yearsAvg = years / members.size
            yearsAvg >= 10 && designer.isNotEmpty() && members.size >= 3
        }

        //출력 firstTeams

        //2.
        //총 멤버는 3 명이상 이어야함
        //리더가 Planner 면 안됨
        //멤버의 최소 경력이 5 년 이상
        //Incentive 는 Even 이어야함
        val secondTeams: List<Team> = teams.filter { it ->
            val team = it
            val members = team.memberIndices.map { members[it] }.filter { it.years >= 5 }
            val leader = this.members[team.leaderIndex]
            members.size >= 3 && leader.job != Job.Planner && team.properties == IncentiveSettings.Even
        }
        //출력 secondTeams

        //3.
        //1. 과 2. 의 조건을 충족하고
        //총 경력이 가장 높은 팀
        val finalTeam: Team? = teams.filter {
            val team = it
            val members = team.memberIndices.map { members[it] }.filter { it.years >= 5 }
            val leader = this.members[team.leaderIndex]
            val designer = members.filter { it.job == Job.Designer }
            val years = members.sumBy { it.years }
            val yearsAvg = try{years / members.size} catch (e:ArithmeticException){0}
            (members.size >= 3 && leader.job != Job.Planner && team.properties == IncentiveSettings.Even) && (yearsAvg >= 10 && designer.isNotEmpty() && members.size >= 3)
        }.maxBy {it
            val team = it
            val members = team.memberIndices.map { members[it] }
            val yearSum = members.map { it.years }.sum()
            yearSum
        }
        //출력 finalTeam


        println("firstTeams--------------")
        firstTeams.forEach {
            println(it)
        }
        println("secondTeams--------------")
        secondTeams.forEach {
            println(it)
        }
        println("finalTeam--------------")
        println(finalTeam)
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
