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

    fun Team.isMemeberSizeEqualOrOver(size: Int): Boolean {
        return this.memberIndices.size >= size
    }

    fun Team.isInJob(job: Job): Boolean {
        return toMemeber().firstOrNull { it.job == job }?.let { true } ?: false
    }

    fun Team.toMemeber(): List<Member> {
        return this.memberIndices.map { members[it] }
    }

    fun Team.avgYear(): Int {
        return toMemeber().sumBy { it.years }
    }

    fun Team.leader(): Member {
        return members[this.leaderIndex]
    }

    fun run() {
        //1.
        //총 멤버는 3 명이상 이어야함
        //모든 멤버들중 Designer 한명이 있어야함
        //모든 멤버들의 평균 경력이 10 년 이상 이어야함
        //팀 셋팅은 LeaderOnly 가 아니어야함

        val firstTeams: List<Team> = teams
                .asSequence()
                .filter { it.isMemeberSizeEqualOrOver(3) }
                .filter { it.isInJob(Job.Designer) }
                .filter { it.properties != IncentiveSettings.LeaderOnly }
                .toList()

//        val firstTeams: List<Team> = teams.filter { it ->
//            val team = it
//            val members = team.memberIndices.map { members[it] }
//            val designer = members.filter { it.job == Job.Designer }
//            val years = members.sumBy { it.years }
//            val yearsAvg = years / members.size
//            members.size >= 3 && designer.isNotEmpty() && yearsAvg>=10 && team.properties != IncentiveSettings.LeaderOnly
//        }

        //출력 firstTeams

        //2.
        //총 멤버는 3 명이상 이어야함
        //리더가 Planner 면 안됨
        //멤버의 최소 경력이 5 년 이상
        //Incentive 는 Even 이어야함

        val secondTeams: List<Team> = teams
                .asSequence()
                .filter { it.isMemeberSizeEqualOrOver(3) }
                .filter { it.leader().job!=Job.Planner }
                .filter { it -> !it.toMemeber().none { it.years >=5 } }
                .filter { it.properties == IncentiveSettings.Even }
                .toList()

//        val secondTeams: List<Team> = teams.filter { it ->
//            val team = it
//            val members = team.memberIndices.map { members[it] }.filter { it.years >= 5 }
//            val leader = this.members[team.leaderIndex]
//            members.size >= 3 && leader.job != Job.Planner && team.properties == IncentiveSettings.Even
//        }
        //출력 secondTeams

        //3.
        //1. 과 2. 의 조건을 충족하고
        //총 경력이 가장 높은 팀
        val finalTeam: Team? = teams
                .asSequence()
                .filter { it.isMemeberSizeEqualOrOver(3) }
                .filter { it.isInJob(Job.Designer) }
                .filter { it.properties != IncentiveSettings.LeaderOnly }
                .filter { it.leader().job!=Job.Planner }
                .filter { it -> !it.toMemeber().none { it.years >=5 } }
                .filter { it.properties == IncentiveSettings.Even }
                .maxBy { it.toMemeber().sumBy { it.years } }

        //출력 finalTeam


        println("firstTeams--------------")
        firstTeams.take(5) .forEach {
            println(it)
        }

        println("secondTeams--------------")
        secondTeams.take(3).forEach {
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
