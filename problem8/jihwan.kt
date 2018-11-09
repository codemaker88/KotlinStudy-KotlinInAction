@file:Suppress("SpellCheckingInspection")

/* 1번 문제 - 아래 [ ]에 답을 채워주세요.

타입 A의 값이 필요한 모든 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면 타입 B는 타입 A의 [ 하위 타입 ]이다.

- Int는 Number의 [ 하위 타입 ] 이다.
- Int는 Number?의 [ 하위 타입 ] 이다.
- String?은 String의 [ 상위 타입 ] 이다.
- Any? 는 모든 타입의 [ 상위 타입 ] 이다.
- List<String>은 Any의 [ 하위 타입 ] 이다.

*/

//===================================================================================================

fun test(i: Number?) { }
/* 2번 문제 - 위의 함수를 보고 아래 [ ]에 답을 채워주세요.

1. test(1)
2. test("A")

val nInt: Int? = null
3. test(nInt)

val nString: String? = null
4. test(nString)

5. test(null)

- 1번은 컴파일이 성공 한다. 왜냐하면 파라미터로 넘기는 Int는 Number?의 하위 타입이기 때문이다.
- 2번은 컴파일이 (실패) 한다. 왜냐하면 [ 파라미터로 넘기는 String은 Number?의 하위 타입이 아니기 때문이다. ]
- 3번은 컴파일이 (성공) 한다. 왜냐하면 [ 파라미터로 넘기는 Int? Number?의 하위 타입이기 때문이다. ]
- 4번은 컴파일이 (실패) 한다. 왜냐하면 [ 파라미터로 넘기는 String?은 Number?의 하위 타입이 아니기 때문이다. ]
- 5번은 컴파일이 (성공) 한다. 왜냐하면 [ 파라미터로 넘기는 null은 Number?의 하위 타입이기 때문이다. ]

*/

//===================================================================================================

/* 3번 문제 - 아래 [ ] 에 답을 채우고, ( ) 안에서 답을 고르고, 컴파일이 가능하도록 수정하세요.

다음은 변성에 대해서 참고할만한 설명입니다.
변성(variance) 개념은 List<String>와 List<Any>와 같이,
기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념이다.
기저타입 : List<T> 의 List
타입인자 : List<T> 의 T

*/


class ItemConsumer<in T> {
    fun consume(item: T) {
        println(item.toString())
    }
}

class ItemProducer<out T> (private val item: T) {
    fun produce(): T {
        return item
    }
}

fun <T> produceAndConsume(consumer: ItemConsumer<in T>, producer: ItemProducer<out T>) {
    consumer.consume(producer.produce())
}

fun main(args: Array<String>) {
    val numberConsumer = ItemConsumer<Number>()
    val numberProducer = ItemProducer<Number>(3)
    produceAndConsume<Number>(numberConsumer, numberProducer)
    // It works!

    val anyConsumer = ItemConsumer<Any>()
    produceAndConsume<Number>(anyConsumer, numberProducer)
    // 함수 호출이 실패하는 이유 :
    // produceAndConsume 의 T는 아무런 키워드도 없으므로 (무공변성)이다.
    // 따라서 기저 타입인 ItemConsumer, ItemProducer 가 같아도, 타입 인자가 다르면 [ 상하위 관계가 아니다 ]
    // (공변성)을 만족하기 위해서는,
    // ItemConsumer 와 ItemProducer 의 타입인자가 각각 [ Number ], [ Number ] 이어야 한다.


    val intProducer = ItemProducer<Int>(3)
    produceAndConsume<Number>(numberConsumer, intProducer)
    // Number는 Int의 상위 타입이기 때문에,
    // Number 대신 Int를 Produce 해도 안전하다는 것이 보장된다.
    // 따라서 편의를 위해 ItemProducer<Int> 가 ItemProducer<Number> 의 (하위)타입이 되도록 만들고 싶다.
    // 이 관계는 (공변성)이고, 이를 표현하기 위해서 [ out ] 키워드를 사용하면 된다.
    // 위에 서술한대로 produceAndConsume 함수를 수정해 주세요.


    produceAndConsume<Int>(numberConsumer, intProducer)
    // Number는 Int의 상위 타입이기 때문에,
    // Number를 Consume 가능하다면, Int 또한 Consume이 가능하다는 것이 보장된다.
    // 따라서 편의를 위해 ItemConsumer<Number> 가 ItemConsumer<Int> 의 (상위)타입이 되도록 만들고 싶다.
    // 이 관계는 (반공변성)이고, 이를 표현하기 위해서 [ in ] 키워드를 사용하면 된다.
    // 위에 서술한대로 produceAndConsume 함수를 수정해 주세요.


    produceAndConsumeWithPrint<Number>(anyConsumer, intProducer)
    // 위에 서술한 관계들은 어떤 함수에서 사용하든 보장이 된다.
    // Producer는 (out) 위치의 함수만 가지고 있고,
    // Consumer는 (in) 위치의 함수만 가지고 있기 때문이다.
    // 따라서 함수마다 반복하지 않도록 (선언 지점 변성)을 사용하면 코드를 간결하게 할 수 있다.
    // produceAndConsumeWithPrint 함수의 수정 없이 컴파일이 가능하도록 만드세요.
}

fun <T> produceAndConsumeWithPrint(consumer: ItemConsumer<T>, producer: ItemProducer<T>) {
    val item = producer.produce()
    println(item)
    consumer.consume(item)
}
