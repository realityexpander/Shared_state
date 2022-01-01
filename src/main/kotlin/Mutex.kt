import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val mutex = Mutex()
        var counter = 0
        withContext(Dispatchers.Default) {
            massiveRunMutex {
                mutex.withLock {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }
}

suspend fun massiveRunMutex(action: suspend () -> Unit) {
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