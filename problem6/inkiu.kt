import kotlin.reflect.KProperty

open class SalaryDelegation(var value: Int) {
    operator fun getValue(person: AppPerson, property: KProperty<*>): Int = value
    operator fun setValue(person: AppPerson, property: KProperty<*>, newValue: Int) {
        val oldValue = value
        value = newValue
        println("${person.name}님의 급여가 ${oldValue}에서 ${newValue}로 변경되었습니다.")
        // TODO - updatePersonRepository.... 그러나 인스턴스를 어떻게 인젝션하지?
    }
}

data class AppPerson(
        val name: String,
        val employId: Int,
        val _salary: Int
) {
    var salary: Int by SalaryDelegation(_salary)

    override fun toString(): String {
        return "이름 : $name / 사번 : $employId / 급여 : $salary)"
    }
}

interface PersonRepository {
    fun getPerson(employId: Int): AppPerson
    fun updatePerson(person: AppPerson)
}

object PersonRepositoryImpl : PersonRepository {
    private val db = mutableMapOf(
            1 to PersonFromDB("김인혁", 1, 1000),
            2 to PersonFromDB("김상현", 2, 1000),
            3 to PersonFromDB("김지환", 3, 1000),
            4 to PersonFromDB("황인규", 4, 1000),
            5 to PersonFromDB("전경주", 5, 1000),
            6 to PersonFromDB("박귀남", 6, 1000),
            7 to PersonFromDB("강영길", 7, 1000)
    )

    private data class PersonFromDB(
            val name: String,
            val employId: Int,
            val salary: Int
    )

    override fun getPerson(employId: Int): AppPerson {
        val json = db[employId] ?: throw IllegalArgumentException()
        return mapPerson(json)
    }

    override fun updatePerson(person: AppPerson) {
        val dbPerson = db[person.employId] ?: throw IllegalArgumentException()
        db[dbPerson.employId] = dbPerson.copy(name = person.name, employId = person.employId, salary = person.salary)
    }

    private fun mapPerson(dbPerson: PersonFromDB): AppPerson {
        return AppPerson(dbPerson.name, dbPerson.employId, dbPerson.salary)
    }
}

fun main(args: Array<String>) {
    val e1 = PersonRepositoryImpl.getPerson(1)
    println(e1)
    e1.salary = 2000
}