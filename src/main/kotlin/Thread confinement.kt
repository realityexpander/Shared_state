import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() {

    // Bad solution
    println("Bad solution, gives wrong answer:")
    runBlocking {
//        val counterContext = newSingleThreadContext("CounterContext")
        var counter = 0
//        withContext(counterContext) {
        withContext(Dispatchers.Default) {
            massiveRunThreads {
//                withContext(counterContext) {
                counter++
            }
        }
        println("Counter = $counter")
    }

    println("\nWithout context switching, runs in single thread and context (ie: coarse grained):")
    runBlocking {
        val counterContext = newSingleThreadContext("CounterContext")
        var counter = 0
        withContext(counterContext) {
//        withContext(Dispatchers.Default) {  // without context switching its much faster (coarse grained)
            massiveRunThreads {
//                withContext(counterContext) { // without context switching its much faster
                    counter++
                }
            }
        println("Counter = $counter")
    }


    println("\nWith context switching, runs in parallel (ie: fine grained):")
    runBlocking {
        val counterContext = newSingleThreadContext("CounterContext")
        var counter = 0
        withContext(Dispatchers.Default) {  // context switching is slower (Fine grained)
            massiveRunThreads {
                withContext(counterContext) {  //  context switching is slower
                    counter++
                }
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