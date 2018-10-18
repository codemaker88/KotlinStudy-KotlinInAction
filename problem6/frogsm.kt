package com.frogsm.kotlininaction

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
    val name: String by NameDelegate
    var salary: Int by SalaryDelegate
    var vacation: Vacation? by VacationDelegate

    override fun toString(): String {
        return "이름 : ${name} / 사번 : ${id} / 급여 : ${salary}" +
                if (vacation != null) " / 휴가 : $vacation" else ""
    }
}

class Employee(val id: String, var name: String, var salary: Int, var vacation: Vacation?)

object EmployeeRepository {
    val employees = listOf(
            Employee("1233", "김인혁", 1000,null),
            Employee("1234", "김상현", 1000, Vacation(LocalDate.now().minusDays(6), LocalDate.now().minusDays(3))),
            Employee("1235", "김지환", 1000, null),
            Employee("1236", "황인규", 1000, Vacation(LocalDate.now().minusDays(4), LocalDate.now().minusDays(2))),
            Employee("1237", "전경주", 1000, null),
            Employee("1238", "박귀남", 1000, Vacation(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))),
            Employee("1239", "강영길", 1000, Vacation(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)))
    )
}

object NameDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): String {
        return EmployeeRepository.employees.single {
            it.id == person.id
        }.name
    }

    operator fun setValue(person: Person, property: KProperty<*>, name: String) {
        EmployeeRepository.employees.single {
            it.id == person.id
        }.name = name
    }

}


class Vacation(val startDate:LocalDate, val endDate: LocalDate) {
    override fun toString(): String {
        return "$startDate ~ $endDate"
    }
}

object VacationDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): Vacation? {
        return EmployeeRepository.employees.single {
            it.id == person.id
        }.vacation
    }

    operator fun setValue(person: Person, property: KProperty<*>, vacation: Vacation?) {
        EmployeeRepository.employees.single {
            it.id == person.id
        }.vacation = vacation
    }
}

object SalaryDelegate {
    operator fun getValue(person: Person, property: KProperty<*>): Int {
        return EmployeeRepository.employees
                .single { it.id == person.id }
                .salary
    }

    operator fun setValue(person: Person, property: KProperty<*>, salary: Int) {
        EmployeeRepository.employees
                .single { it.id == person.id }
                .let {
                    val oldValue = it.salary
                    it.salary = salary
                    println("${it.name}님의 급여가 ${oldValue}에서 ${salary}로 변경되었습니다.")
                }
    }
}

fun main(args: Array<String>) {
    Problem.run()
}