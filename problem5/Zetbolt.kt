package com.example.hyeok.myapplication.problem5

import com.github.javafaker.Faker
import java.util.*

class Person(val name: String, val number: Int, var cash: Int = 0) {
    override fun toString(): String {
        return "$name $cash"
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
    class WinGame(val winner: Person?, val size: Int = 0) : SevenFindGame() {
        init {
            if (size >= 7) {
                startGame(size, PersonGenerator.generatePeople(size, winner))
            }
        }
    }
    class LoseGame(looser:List<Person>): SevenFindGame()

    companion object {
        var lastWinner: Person? = null
        val DEFAULT_PRICE = 1000
        fun startGame(size: Int) {
            val people: List<Person> = PersonGenerator.generatePeople(size)
            startGame(size, people)
        }

        private fun startGame(size: Int, people: List<Person>) {
            val winner = people.findNumber(7)!!
            // 돈이 없는 사람은 기본상금만 받고
            // 돈이 있으면 가진 돈의 두배를 받고
            if (winner.cash == 0) {
                winner.cash += DEFAULT_PRICE
            } else { // winner.cash > 0
                winner.cash += winner.cash * 2
            }
            // 이전 승자의 상금을 받는다 (몰수?)
            val lastPrice = lastWinner?.cash ?: 0
            winner.cash += if (winner != lastWinner) lastPrice else 0
            lastWinner = winner

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
