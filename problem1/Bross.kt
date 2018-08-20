//1-1

data class TeamMember(val name: String, val memberRequest: TeamLeader) {

    fun applyForALeave(requestDate: Long) {
        val memberNotify = memberRequest.teamMembers.map { "[Mail Of ${it.name}] ${this.name} Leave OK from.TeamLeader" }.joinToString("\n")
        val result = "[휴가 신청시 동료 직원에게 알려주기]\n$memberNotify"
        println(result)
    }

}


data class TeamLeader(val name: String, var teamMembers: MutableList<TeamMember> = mutableListOf())

fun main(args: Array<String>) {
    var currentDate: Long = 20180825
    val leader = TeamLeader(name = "TeamLeader").apply {
        teamMembers.add(TeamMember(name = "A", memberRequest = this)) // memberRequest is TeamLeader Reference.
        teamMembers.add(TeamMember(name = "B", memberRequest = this))
        teamMembers.add(TeamMember(name = "C", memberRequest = this))
        teamMembers.add(TeamMember(name = "D", memberRequest = this))
    }

    val me = leader.teamMembers[3] // 팀원 하나 선택
    me.applyForALeave(requestDate = currentDate)
}


//1-2 

enum class Status{
    Leave,Work;

    operator fun invoke(startDate: Long, endDate: Long): Status {
        this.startDate = startDate
        this.endDate = endDate
        return this
    }

    constructor(){
    }

    constructor(startDate:Long?,endDate:Long?){
        this.startDate = startDate
        this.endDate = endDate
    }

    var startDate : Long? = null
    var endDate : Long? = null

    fun confirm(requestDate: Long):String{
        return if(this.equals(Leave)){

            val start = startDate?.let { startDate }?:Long.MAX_VALUE
            val end = endDate?.let { endDate }?:Long.MAX_VALUE

            when(requestDate){
                in start..end ->{
                    "REJECT"
                }
                else -> "OK"
            }

        }else{
            "OK"
        }
    }
}


data class TeamMember(val name: String, val memberRequest: TeamLeader) {

    fun applyForALeave(requestDate: Long) {
        val memberNotify = memberRequest.teamMembers.map { "[Mail Of ${it.name}] ${this.name} Leave ${it.memberRequest.status.confirm(requestDate)} from.TeamLeader" }.joinToString("\n")
        val result = "[휴가 신청시 동료 직원에게 알려주기]\n$memberNotify"
        println(result)
    }

}


data class TeamLeader(val name: String, var teamMembers: MutableList<TeamMember> = mutableListOf(), var status: Status = Status.Work)

fun main(args: Array<String>) {
    var currentDate: Long = 20180825
    val leader = TeamLeader(name = "TeamLeader").apply {
        teamMembers.add(TeamMember(name = "A", memberRequest = this)) // memberRequest is TeamLeader Reference.
        teamMembers.add(TeamMember(name = "B", memberRequest = this))
        teamMembers.add(TeamMember(name = "C", memberRequest = this))
        teamMembers.add(TeamMember(name = "D", memberRequest = this))
    }

    val me = leader.teamMembers[3] // 팀원 하나 선택

    println("[팀장 상태에 따른 휴가 신청]")
    leader.status = Status.Leave(startDate = 20180825, endDate = 20180830)

    println("\n")
    println("1) 휴가 신청 날짜가 팀장 휴가 전일 때")
    currentDate = 20180823
    me.applyForALeave(requestDate = currentDate)

    println("\n")
    println("2) 휴가 신청 날짜가 팀장 휴가 중일 때")
    currentDate = 20180825
    me.applyForALeave(requestDate = currentDate)

    println("\n")
    println("3) 팀장 휴가가 끝난 뒤에 휴가 신청했을 때")
    leader.status = Status.Work
    me.applyForALeave(requestDate = currentDate)
}
