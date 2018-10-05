## 문제 - Simple 7 Find Game

간단한 7 찾기 게임을 만들어 봅시다.
인원이 절반으로 줄어 7명 이하 일때까지 7을 찾는 게임은 계속 됩니다.
최후의 승자의 이름과 상금을 출력해주세요.

### 7 찾기
* 게임 시작의 인원은 7미만 일 수 없습니다.
* 7 미만일 경우 RuntimeException이 발생합니다.
* 각 사용자는 사이즈만큼 생성되며, 각각 고유한 번호를 부여 받습니다.
* 게임은 사용자가 7명 미만일 경우까지 진행됩니다.
* 게임에서 승리할 경우 돈이 없는 사용자의경우 기본금액을 받습니다.
* 게임에서 승리할 경우 돈이 있는 사용자의 경우 가진 금액의 2배를 받습니다.
* 각 게임에서 이전 승자의 상금은 이후 승자에게 전달됩니다.
* 사용자들을 생성하는 코드는 아래의 코드를 사용합니다. (https://github.com/DiUS/java-faker) //compile group: 'com.github.javafaker', name: 'javafaker', version: '0.16'

```kotlin
class Person(val name: String, val number: Int, var cash: Int = 0)

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
   class WinGame: SevenFindGame()
   class LoseGame: SevenFindGame()

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

```

### 출력 예시
```text
    winner : Person(name=강 우진, number=7, cash=80000)
```
