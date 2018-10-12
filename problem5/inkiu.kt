@file:Suppress("NonAsciiCharacters", "ObjectPropertyName")

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
        // subList 로 인해 전판 Winner 가 새로운 people 에 전달되지 않음
        return personList.subList(0, size - 2).shuffled()
    }
}

sealed class SevenFindGame {
    class WinGame(winner: Person, size: Int? = null) : SevenFindGame() {
        init {
            if (size != null) {
                if (누적상금 == 0) 누적상금 = 기본상금
                else 누적상금 *= 2
                val newPeople = PersonGenerator.generatePeople(size, winner)
                startGame(size, newPeople)
            } else {
                winner.cash = 누적상금
                누적상금 = 0
            }
        }

        companion object {
            const val 기본상금 = 1000
            var 누적상금 = 0
        }
    }

    class LoseGame(private val losers: List<Person>) : SevenFindGame()

    companion object {
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