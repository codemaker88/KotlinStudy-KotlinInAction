## 문제 - 팀 찾기

회사에 긴급 프로젝트 하나가 들어 왔습니다.
회사는 시간을 아끼기 위해 가장 빠르게 만들수 있는 팀을 고르려고 합니다.
주어진 팀들 중 주어진 조건에 가장 적합한 팀을 골라 봅시다.

### 문제 1-1 디자이너가 있는 팀들을 찾기
* 총 멤버는 3 명이상 이어야함
* 모든 멤버들중 Designer 한명이 있어야함
* 모든 멤버들의 평균 경력이 10 년 이상 이어야함
* 팀 보너스 셋팅은 LeaderOnly 가 아니어야함

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val firstTeams: List<Team> =
    //출력 firstTeams
}
```

#### 출력
```text
[Found 1,000 teams!]
```

### 문제 1-2 기획자가 팀장이 아닌 팀 찾기
* 총 멤버는 3 명이상 이어야함
* 리더가 Planner 면 안됨
* 멤버의 최소 경력이 5 년 이상
* Incentive 는 Even 이어야함

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val secondTeams: List<Team> =
    //출력 secondTeams
}
```

#### 출력
```text
[Found 1,000 teams!]
```

### 문제 1-3 가장 이상적인 팀 찾기
* 1 과 2 의 조건을 모두 충족하고
* 총 경력이 가장 높은 팀

#### Test.run()
```kotlin
private val members = createMembers()
private val teams = createTeams(members)

fun run() {
    val finalTeam: Team? =
    //출력 finalTeam
}
```

#### 출력
```text
[Found the perfect Team!]
[Mr.1234] Worked as Coder for 10 yrs
[Mr.1222] Worked as Designer for 10 yrs
.
.
.
[Mr.1222] Worked as Planner for 1 yrs
```