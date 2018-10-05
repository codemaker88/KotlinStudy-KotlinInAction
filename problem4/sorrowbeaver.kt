import java.util.concurrent.ThreadLocalRandom

enum class Job {
  Designer, Programmer, Planner, QA, CAT
}

class Person(
  val index: Int,
  val name: String,
  val yearsOfCareer: Int,
  val job: Job
)

data class Team(
  val leaderIndex: Int,
  val members: List<Person>,
  val bonusPolicy: BonusPolicy
) {
  val leader = members.find { it.index == leaderIndex } ?: throw RuntimeException("There is no leader :(")
}

sealed class BonusPolicy(
  val money: Int,
  val predicate: (Team) -> List<Person>
) {

  class LeaderOnly(money: Int) : BonusPolicy(money, { listOf(it.leader) })
  class All(money: Int): BonusPolicy(money, { it.members })
  object NoBonus : BonusPolicy(0, { emptyList() })
}

val random: ThreadLocalRandom = ThreadLocalRandom.current()
fun createMembers(size: Int = 3000): List<Person> {
  val jobs = Job.values()

  return (0 until size).map {
    val name = "Mr.$it"
    val yearsOfCareer = random.nextInt(0, 100)
    val job = jobs[random.nextInt(0, 5)]

    Person(it, name, yearsOfCareer, job)
  }
}

fun createTeams(allMembers: List<Person>): List<Team> {
  val memberSequence = allMembers.iterator()

  return generateSequence {
    if(memberSequence.hasNext()) {
      val memberCount = random.nextInt(0, 100)
      val members = memberSequence.asSequence().take(memberCount).toList()
      val bonusPolicy = when(random.nextInt(0, 2)) {
        0 -> BonusPolicy.All(random.nextInt(0, 10000))
        1 -> BonusPolicy.LeaderOnly(random.nextInt(0, 10000))
        2 -> BonusPolicy.NoBonus
        else -> throw RuntimeException()
      }
      Team(members.first().index, members, bonusPolicy)
    } else {
      null
    }
  }.toList()
}


private val members = createMembers()
private val teams = createTeams(members)

fun main(args: Array<String>) {
  val firstTeams: List<Team> = teams.asSequence()
    .filter { it.members.size > 3 }
    .filter { team -> team.members.any { it.job == Job.Designer } }
    .filter { team -> team.members.all { it.yearsOfCareer >= 10 } }
    .filter { it.bonusPolicy !is BonusPolicy.LeaderOnly }
    .toList()
  //출력 firstTeams
  println(firstTeams)

  val secondTeams = teams.asSequence()
    .filter { it.members.size > 3 }
    .filter { it.leader.job != Job.Planner }
    .filter { team -> team.members.all { it.yearsOfCareer >= 5 } }
    .filter { team -> team.bonusPolicy.money % 2 == 0 }
    .toList()
  println(secondTeams)

  val finalTeam = firstTeams.intersect(secondTeams).maxBy { team -> team.members.sumBy { it.yearsOfCareer } }
  println(finalTeam)
}
