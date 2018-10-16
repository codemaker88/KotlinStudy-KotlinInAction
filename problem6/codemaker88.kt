package problem6

import java.time.LocalDate
import kotlin.properties.Delegates
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
    val name: String by NameDelegate

    //문제1
    //var salary: Int by SalaryDelegate

    //문제2 구현
    var salary: Int by Delegates.observable(EmployeeRepository.getEmployee(this).salary) {
        property: KProperty<*>, oldValue: Int, newValue: Int ->
        EmployeeRepository.getEmployee(this).salary = newValue
        println("${name}님의 급여가 ${oldValue}에서 ${newValue}로 변경되었습니다.")
    }

    var vacation: Vacation? by VacationDelegate

    override fun toString(): String {
        return "이름 : ${name} / 사번 : ${id} / 급여 : ${salary}" +
                if (vacation != null) " / 휴가 : $vacation" else ""
    }
}

//문제1 구현
object SalaryDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): Int {
        return EmployeeRepository.getEmployee(person).salary
    }

    operator fun setValue(person: Person, property: KProperty<*>, salary: Int) {
        EmployeeRepository.getEmployee(person).salary = salary
    }
}

/*
output >>
이름 : 김인혁 / 사번 : 1233 / 급여 : 1000
김인혁님의 급여가 1000에서 2000로 변경되었습니다.
이름 : 김상현 / 사번 : 1234 / 급여 : 1000 / 휴가 : 2018-10-10 ~ 2018-10-13
김상현님의 급여가 1000에서 3000로 변경되었습니다.
 */


class Employee(val id: String, var name: String, var salary: Int, var vacation: Vacation?)

object EmployeeRepository {
    val employees = listOf(
            Employee("1233", "김인혁", 1000, null),
            Employee("1234", "김상현", 1000, Vacation(LocalDate.now().minusDays(6), LocalDate.now().minusDays(3))),
            Employee("1235", "김지환", 1000, null),
            Employee("1236", "황인규", 1000, Vacation(LocalDate.now().minusDays(4), LocalDate.now().minusDays(2))),
            Employee("1237", "전경주", 1000, null),
            Employee("1238", "박귀남", 1000, Vacation(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))),
            Employee("1239", "강영길", 1000, Vacation(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)))
    )

    fun getEmployee(person: Person): Employee {
        return EmployeeRepository.employees.single {
            it.id == person.id
        }
    }
}

object NameDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): String {
        return EmployeeRepository.getEmployee(person).name
    }

    operator fun setValue(person: Person, property: KProperty<*>, name: String) {
        EmployeeRepository.getEmployee(person).name = name
    }
}

class Vacation(val startDate: LocalDate, val endDate: LocalDate) {
    override fun toString(): String {
        return "$startDate ~ $endDate"
    }
}

object VacationDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): Vacation? {
        return EmployeeRepository.getEmployee(person).vacation
    }

    operator fun setValue(person: Person, property: KProperty<*>, vacation: Vacation?) {
        EmployeeRepository.getEmployee(person).vacation = vacation
    }
}

fun main(args: Array<String>) {
    Problem.run()
}
