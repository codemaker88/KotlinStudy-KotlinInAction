import com.github.javafaker.Faker
import java.util.*

class Person(val name: String, val number: Int, var cash: Int = 0) {
    override fun toString(): String {
        return "Person(name=$name, number=$number, cash=$cash)"
    }
}

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
    class WinGame: SevenFindGame {
        constructor(winner: Person?, size : Int) : super() {
            addTotalPrice(winner)
            startGame(size, PersonGenerator.generatePeople(size, winner))
        }

        constructor(winner: Person?) : super() {
            addTotalPrice(winner)
        }

        private fun addTotalPrice(winner: Person?) {
            winner?.apply {
                val winningPrice = if (cash > 0) cash * 2 else defaultPrice
                cash = winningPrice + prevPrice
                prevPrice += winningPrice
            }
        }
    }

    class LoseGame(person: List<Person>): SevenFindGame()

    companion object {
        const val defaultPrice = 1000
        var prevPrice = 0

        fun startGame(size: Int) {
            val people: List<Person> = PersonGenerator.generatePeople(size)
            startGame(size, people)
        }

        private fun startGame(size: Int, people: List<Person>) {
            val winner = people.findNumber(7)!!
            if (size / 2 > 7) {
                WinGame(winner, size / 2)
            } else {
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