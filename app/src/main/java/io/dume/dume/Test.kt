package io.dume.dume

fun main() {
    var block: (String) -> Unit = {
        print(it)
    }
    "hello".joaa(block)


}

public inline fun <T, R> T.enam(block: (T) -> R): R {

    return block(this)
}

public inline fun <t, String> t.joaa(block: (t) -> String): kotlin.String {
    return "Hello From Enam"
}


data class Person(var name: String, var age: Int)