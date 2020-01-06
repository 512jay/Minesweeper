import java.util.*
import kotlin.math.cos
import kotlin.math.sin

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val radians = scanner.nextLine().toDouble()
    val sine = sin(radians)
    val cosine = cos(radians)
    println(sine - cosine)
}