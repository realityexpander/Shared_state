import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        var counter = AtomicInteger(0)  // Special type of Integer class that can only be updated one at a time, makes threads wait
        withContext(Dispatchers.Default) {
            massiveRunAtomic { counter.incrementAndGet() }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRunAtomic(action: suspend () -> Unit) {
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