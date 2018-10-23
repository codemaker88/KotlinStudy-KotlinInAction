package problem6

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.time.LocalDate
import kotlin.reflect.KProperty

object Problem6 {
    fun run() {
        var person = PersonRepository.getPerson("1233")
        person.salary = 2000
        println(person)
        person = PersonRepository.getPerson("1234")
        person.salary = 3000
        println(person)
    }
}

open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)
    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }
}

object PersonRepository {
    fun getPerson(id: String): Person {
        return Person(id).apply {
            addPropertyChangeListener(PropertyChangeListener {
                println("${this.name}님의 급여가 ${it.oldValue}에서 ${it.newValue}로 변경되었습니다.")
            })
        }
    }
}

class Person(val id: String): PropertyChangeAware() {
    var name: String by NameDelegate

    var salary: Int by SalaryDelegate(changeSupport)

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

class SalaryDelegate(private val changeSupport: PropertyChangeSupport) {

    operator fun getValue(person: Person, property: KProperty<*>): Int {
        return EmployeeRepository.employees.single {
            it.id == person.id
        }.salary
    }

    operator fun setValue(person: Person, property: KProperty<*>, salary: Int) {
        val employee = EmployeeRepository.employees.single {
            it.id == person.id
        }
        val oldSalary = employee.salary
        employee.salary = salary
        changeSupport.firePropertyChange(property.name, oldSalary, salary)
    }
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

fun main(args: Array<String>) {
    Problem6.run()
}
