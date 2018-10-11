package com.frogsm.kotlininaction

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
    class WinGame(winner: Person?, size: Int = 0) : SevenFindGame() {
        init {
            winner?.apply {
                cash = if (cash == 0) DEFAULT_PRICE else cash * 2
                cash += accumulatedPrice
                accumulatedPrice += cash
            }
            if (size != 0)
                startGame(size, PersonGenerator.generatePeople(size, winner))
        }
    }
    class LoseGame(losers: List<Person>) : SevenFindGame() {
        init {
            println("loser: ${losers.filter { it.number == 7 }}")
        }
    }

    companion object {
        private var accumulatedPrice: Int = 0 // 축적된 값
        private const val DEFAULT_PRICE = 1000 // 디폴트 상금 추가
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