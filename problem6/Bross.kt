
object Problem {
    fun run() {
        var person = PersonRepository.get("1233")
        println(person)
        person.salary = 2000

        person = PersonRepository.get("1234")
        println(person)
        person.salary = 3000
    }
}


interface Repository<T> {
    fun get(): T {
        throw RuntimeException("Not declare get() function !")
    }

    fun get(id: String): T {
        throw RuntimeException("Not declare get(id:String) function !")
    }
}

object EmployeeRepository : Repository<List<Employee>> {
    private val employee = listOf(
            Employee("1233", "김인혁", 1000, null),
            Employee("1234", "김상현", 1000, Vacation(LocalDate.now().minusDays(6), LocalDate.now().minusDays(3))),
            Employee("1235", "김지환", 1000, null),
            Employee("1236", "황인규", 1000, Vacation(LocalDate.now().minusDays(4), LocalDate.now().minusDays(2))),
            Employee("1237", "전경주", 1000, null),
            Employee("1238", "박귀남", 1000, Vacation(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))),
            Employee("1239", "강영길", 1000, Vacation(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)))
    )

    override fun get(): List<Employee> {
        return employee
    }
}

object PersonRepository : Repository<Person> {
    override fun get(id: String): Person {
        return Person(id)
    }
}


data class Person(val id: String) {
    val name: String by Delegatable.NameDelegate
    var vacation: Vacation? by Delegatable.VacationDelegate
    var salary: Int by Delegatable.SalaryDelegate

    override fun toString(): String {
        return "이름 : ${name} / 사번 : ${id} / 급여 : ${salary}" +
                if (vacation != null) " / 휴가 : $vacation" else ""
    }
}

data class Vacation(val startDate: LocalDate, val endDate: LocalDate) {
    override fun toString(): String {
        return "$startDate ~ $endDate"
    }
}

data class Employee(val id: String, var name: String, var salary: Int, var vacation: Vacation?)


interface ReadWriteProperty<in R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}

sealed class Delegatable<R, T> : ReadWriteProperty<R, T> {

    object NameDelegate : Delegatable<Person, String>() {

        override fun getValue(thisRef: Person, property: KProperty<*>): String {
            return EmployeeRepository.get().single {
                it.id == thisRef.id
            }.name
        }

        override fun setValue(thisRef: Person, property: KProperty<*>, value: String) {
            EmployeeRepository.get().single {
                it.id == thisRef.id
            }.apply {
                println("${this.name}님의 ID가 ${this.id} 에서 $value 으로 변경되었습니다.")
            }.name = value
        }

    }

    object SalaryDelegate : Delegatable<Person, Int>() {
        override fun getValue(thisRef: Person, property: KProperty<*>): Int {
            return EmployeeRepository.get().single {
                it.id == thisRef.id
            }.salary
        }

        override fun setValue(thisRef: Person, property: KProperty<*>, value: Int) {
            EmployeeRepository.get().single {
                it.id == thisRef.id
            }.apply {
                println("${this.name}님의 급여가 ${this.salary} 에서 $value 으로 변경되었습니다.")
            }.salary = value
        }
    }

    object VacationDelegate : Delegatable<Person, Vacation?>() {
        override fun getValue(thisRef: Person, property: KProperty<*>): Vacation? {
            return EmployeeRepository.get().single {
                it.id == thisRef.id
            }.vacation
        }

        override fun setValue(thisRef: Person, property: KProperty<*>, value: Vacation?) {
            EmployeeRepository.get().single {
                it.id == thisRef.id
            }.apply {
                println("${this.name}님의 휴가가 ${this.vacation} 에서 $value 으로 변경되었습니다.")
            }.vacation = value
        }
    }

}


fun main(args: Array<String>) {
    Problem.run()
}
