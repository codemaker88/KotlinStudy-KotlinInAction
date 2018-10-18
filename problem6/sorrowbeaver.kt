import java.time.LocalDate
import kotlin.reflect.KProperty

object Problem {
  fun run() {
    var person = PersonRepository.getPerson("1233")
    println(person)
    person.salary = 2000

    person = PersonRepository.getPerson("1234")
    println(person)
    person.salary = 3000
  }
}

object PersonRepository {
  fun getPerson(id: String): Person {
    return Person(id)
  }
}

class Person(val id: String) {
  val name: String? by NameDelegate(EmployeeRepository)
  var salary: Int? by SalaryDelegate(EmployeeRepository)
  var vacation: Vacation? by VacationDelegate(EmployeeRepository)

  // 이렇게도 해볼 수 있지 않을까요 ㅎ.ㅎ
  val employee: Employee? by EmployeeDelegate(EmployeeRepository)

  override fun toString(): String {
    return "이름 : ${name} / 사번 : ${id} / 급여 : ${salary}" +
      if (vacation != null) " / 휴가 : $vacation" else ""
  }
}

class Employee(val id: String, var name: String, var salary: Int?, var vacation: Vacation?)

object EmployeeRepository {
  val employees = listOf(
    Employee("1233", "김인혁", 1000,null),
    Employee("1234", "김상현", 1000, Vacation(LocalDate.now().minusDays(6), LocalDate.now().minusDays(3))),
    Employee("1235", "김지환", 1000, null),
    Employee("1236", "황인규", 1000, Vacation(LocalDate.now().minusDays(4), LocalDate.now().minusDays(2))),
    Employee("1237", "전경주", 1000, null),
    Employee("1238", "박귀남", 1000, Vacation(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))),
    Employee("1239", "강영길", 1000, Vacation(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)))
  ).associateBy { it.id }.toMutableMap()
}

// 이러면 repository를 골라서 줄 수 있음!
class NameDelegate(
  private val repository: EmployeeRepository
) {
  operator fun getValue(person: Person, property: KProperty<*>): String? {
    return repository.employees[person.id]?.name
  }

  operator fun setValue(person: Person, property: KProperty<*>, name: String) {
    repository.employees[person.id]?.name = name
  }

}


class Vacation(val startDate:LocalDate, val endDate: LocalDate) {
  override fun toString(): String {
    return "$startDate ~ $endDate"
  }
}

class VacationDelegate(
  private val repository: EmployeeRepository
) {
  operator fun getValue(person: Person, property: KProperty<*>): Vacation? {
    return repository.employees[person.id]?.vacation
  }

  operator fun setValue(person: Person, property: KProperty<*>, vacation: Vacation?) {
    repository.employees[person.id]?.vacation = vacation
  }
}

class SalaryDelegate(
  private val repository: EmployeeRepository
) {
  operator fun getValue(person: Person, property: KProperty<*>): Int? {
    return repository.employees[person.id]?.salary
  }


  operator fun setValue(person: Person, property: KProperty<*>, salary: Int?) {
    val employee = repository.employees[person.id]
    println("${person.name}님의 급여가 ${person.salary}에서 ${salary}로 변경되었습니다")
    employee?.salary = salary
  }
}

class EmployeeDelegate(
  private val repository: EmployeeRepository
) {

  operator fun getValue(person: Person, property: KProperty<*>): Employee? {
    return repository.employees[person.id]
  }
  operator fun setValue(person: Person, property: KProperty<*>, employee: Employee?) {
    employee?.let { repository.employees[person.id] = it }
  }
}

fun main(args: Array<String>) {
  Problem.run()
}