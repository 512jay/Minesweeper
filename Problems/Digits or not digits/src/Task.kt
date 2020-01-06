import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val input = scanner.nextLine()
    val output= mutableListOf<Boolean>()

    for (i in input.indices) {
        if(input[i] == ' ') continue
        output.add(input[i].isDigit())
    }
    for (i in 0..3) {
        print(output[i])
        if (i == 3) return
        print("\\")
    }
}
