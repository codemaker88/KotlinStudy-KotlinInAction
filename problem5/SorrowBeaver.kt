import com.github.javafaker.Faker
import java.util.*


data class Person(val name: String, val number: Int, var cash: Int = 0)

fun List<Person>.findNumber(number: Int): Person? {
  return this.firstOrNull { it.number == number }
}

object PersonGenerator {
  val faker = Faker(Locale("ko"))

  fun generatePeople(size: Int): List<Person> {
    return generatePeople(size, null)
  }

  fun generatePeople(size: Int, winner: Person?): List<Person> {
    if (size < 7) {
      throw RuntimeException("size는 7이상 이여야 합니다.")
    }
    val personList = mutableListOf<Person>()
    for (index in 1..size) {
      val name = faker.name().fullName()
      val number = index
      personList.add(Person(name, number))
    }
    winner?.let { personList.add(winner) }
    return personList.subList(0, size - 2).shuffled()
  }
}

sealed class SevenFindGame {
  class WinGame(
    winner: Person,
    size: Int? = null
  ) : SevenFindGame() {

    init {
      println("$winner $lastWinnerCash")
      winner.cash = if(winner.cash == 0) {
        minimumPrize
      } else {
        winner.cash * 2
      }
      winner.cash += lastWinnerCash

      lastWinnerCash = winner.cash
      println(lastWinnerCash)

      if(size != null) {
        val people: List<Person> = PersonGenerator.generatePeople(size, winner)
        startGame(size, people)
      }
    }

  }

  class LoseGame(
    val losers: List<Person>
  ) : SevenFindGame()
  // Losers always do nothing :(

  companion object {

    const val minimumPrize = 3000
    var lastWinnerCash = 0

    fun startGame(size: Int) {
      val people: List<Person> = PersonGenerator.generatePeople(size)
      startGame(size, people)
    }

    private fun startGame(size: Int, people: List<Person>) {
      val winner = people.findNumber(7)!!
      if (size / 2 > 7) {
        WinGame(winner, size / 2)
      } else { // This is last game
        WinGame(winner)
        println("winner : $winner")
      }
      LoseGame(people.filter { it != winner })
    }
  }
}


fun main(args: Array<String>) {
  SevenFindGame.startGame(100) // 7이상의 수
}