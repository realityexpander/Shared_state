import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val counterContext = newSingleThreadContext("CounterContext")
        var counter = 0
        withContext(counterContext) {
//        withContext(Dispatchers.Default) {
            massiveRunThreads {
//                withContext(counterContext) {
                    counter++
//                }
            }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRunThreads(action: suspend () -> Unit) {
    val n = 100
    val k = 1000
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}