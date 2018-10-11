package chapter6

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
    class WinGame(winner: Person, size: Int = 0) : SevenFindGame() {
        init {
            winnerCash = if (winnerCash == 0) 10000 else winnerCash * 2
            winner.cash = winnerCash

            try {
                startGame(size)
            } catch (e: RuntimeException) {
                //e.printStackTrace()
            }
        }
    }

    class LoseGame(people: List<Person>) : SevenFindGame()

    companion object {
        var winnerCash = 0

        fun startGame(size: Int) {
            val people: List<Person> = PersonGenerator.generatePeople(size)
            startGame(size, people)
        }

        private fun startGame(size: Int, people: List<Person>) {
            val winner = people.findNumber(7)
            winner?.let {
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
}

fun main(args: Array<String>) {
    SevenFindGame.startGame(100) // 7이상의 수
}
