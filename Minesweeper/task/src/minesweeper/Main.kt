package minesweeper

import java.util.Scanner

fun main() {
    val mine = 'X'
    val free = '.'
    val height = 9
    val width = 9
    var firstFreeCheckDone = false
    var gameFailed = false

    print("How many mines do you want on the field? ")
    val mines = getInput().toInt()
    println()

    val mineField = CharArray(height * width) {'/'}
    val answerField = CharArray(height * width) {free}
    val lookUpField = CharArray(height * width) {free}
    var gameField = CharArray(height * width) {free}

    while(!checkForWin(mineField, gameField, firstFreeCheckDone)) {

//        showGameMap(mineField, height, width)
//        println("mine field")
//        showGameMap(lookUpField, height, width)
//        println("lookup Field")
//        showGameMap(answerField, height, width)
//        println("answer field")
        showGameMap(gameField, height, width)
        // println("game field")

        print("Set/unset mines marks or claim a cell as free: ")

        val coordinates = getInput().split(' ')
        val targetCell = (coordinates[1].toInt() - 1) * width  + coordinates[0].toInt() - 1
        // println("Target Cell = $targetCell")

        if (coordinates[2] == "free") {
            if (!firstFreeCheckDone) {
                firstFreeCheckDone = true



                // Create mineField
                while (mines != charCount(mineField)) {
                    val random = (Math.random() * mineField.size).toInt()
                    if (random != targetCell && gameField[targetCell] != '*'){
                        mineField[random] = mine
                        lookUpField[random] = mine
                        answerField[random] = mine
                        // gameField[random] = '.'
                    }
                }

                for (i in lookUpField.indices) { // setup lookup field
                    if (mineField[i] == mine) continue
                    lookUpField[i] = cellType(i,height, width)
                    mineField[i] = checkSurrounding(lookUpField[i], i, width,mineField)
                }
                // gameField[targetCell] = mineField[targetCell]
                gameField = checkRight(targetCell, gameField, mineField)
                gameField = checkLeft(targetCell, gameField, mineField)


            } else {
                if (mineField[targetCell] == mine) {
                    println("You failed by hitting a mine!")
                    gameFailed = true
                    break
                } else {
                    gameField = checkRight(targetCell, gameField, mineField)
                    gameField = checkLeft(targetCell, gameField, mineField)
                }
            }


        } else if (coordinates[2] == "mine") {
            if (gameField[targetCell] == '.') {
                gameField[targetCell] = '*' // toggle flagged for a mine
            } else if (gameField[targetCell] == '*') {
                gameField[targetCell] = '.'
            }
        }
    }
    if (gameFailed) {
        showGameMap(mineField, height, width)
    } else {
        showGameMap(gameField, height, width)
    }
}

/*
Given a field with a '/' i need to write a function that checks surrounding fields until it dose not
neighbor a '.'
 */
fun checkRight(cell: Int, gf: CharArray, mf: CharArray): CharArray {
    for (i in cell..gf.lastIndex){
        if (mf[i] == 'X') {
            continue}
        gf[i] = mf[i]
    }
    return gf
}
fun checkLeft(cell: Int, gf: CharArray, mf: CharArray): CharArray {
    for (i in cell downTo 0){
        if (mf[i] == 'X') continue
        gf[i] = mf[i]
    }
    return gf
}

fun showGameMap(af: CharArray, height: Int, width: Int) {
    var y = -1
    var x = -2
    var fieldPos = 0
    val w = width + 3
    val h = height + 3
    val showMap = CharArray(h * w) {'.'}
    for (i in showMap.indices) {
        x++
        if (y == 0) showMap[i] = '—'
        if (y == height + 1) showMap[i] = '—'
        if ((i + 1) % w == 0) {
            showMap[i] = '|'
            y++
        } // far right line
        if (i % w == 0) showMap[i] = if (y in 1..height) numToChar(y) else if (y == -1) ' ' else '-'
        if ((i - 1) % w == 0) showMap[i] = '|'
        if (y == -1 && x in 1..width) showMap[i] = numToChar(x)
        if (showMap[i] == '.' ) {
            showMap[i] = af[fieldPos]
            fieldPos++
        }

    }
    for (i in showMap.indices) {
        if (i % w == 0) print("\n")
        print(showMap[i])
    }
    println()
}

fun checkForWin(mf: CharArray,gf: CharArray, firstFreeCheckDone: Boolean): Boolean {
    val mines = charCount(mf)
    val stars = charCount(gf, '*')

    if (mines == stars && firstFreeCheckDone) {
        for (i in mf.indices) {
            if (mf[i] == 'X') {
                if (gf[i] != '*') {
                    return false
                }
            }
        }
        println("Congratulations! You found all mines!")
        return true
    }
    return false
}

fun checkSurrounding(type: Char, cellNum: Int, width: Int = 9, mf: CharArray): Char {

    var bombs = 0
    val cellChar = mf[cellNum]
    if (cellChar == 'X') return '.'
    when(type){
        'o' -> {
            val left = mf[cellNum - 1]
            val right = mf[cellNum + 1]
            val topLeft = mf[cellNum - width - 1]
            val topMiddle = mf[cellNum - width]
            val topRight = mf[cellNum - width + 1]
            val bottomLeft = mf[cellNum + width - 1]
            val bottomMiddle = mf[cellNum + width]
            val bottomRight = mf[cellNum + width + 1]
            if (isMine(left)) bombs++
            if (isMine(right)) bombs++
            if (isMine(topLeft)) bombs++
            if (isMine(topMiddle)) bombs++
            if (isMine(topRight)) bombs++
            if (isMine(bottomLeft)) bombs++
            if (isMine(bottomMiddle)) bombs++
            if (isMine(bottomRight)) bombs++
        }
        '1' -> {
            val right = mf[cellNum + 1]
            val bottomMiddle = mf[cellNum + width]
            val bottomRight = mf[cellNum + width + 1]
            if (isMine(right)) bombs++
            if (isMine(bottomMiddle)) bombs++
            if (isMine(bottomRight)) bombs++
        }
        '2' -> {
            val left = mf[cellNum - 1]
            val bottomLeft = mf[cellNum + width - 1]
            val bottomMiddle = mf[cellNum + width]
            if (isMine(left)) bombs++
            if (isMine(bottomLeft)) bombs++
            if (isMine(bottomMiddle)) bombs++
        }
        '3' -> {
            val topMiddle = mf[cellNum - width]
            val right = mf[cellNum + 1]
            val topRight = mf[cellNum - width + 1]
            if (isMine(topMiddle)) bombs++
            if (isMine(topRight)) bombs++
            if (isMine(right)) bombs++
        }
        '4' -> {
            val topMiddle = mf[cellNum - width]
            val topLeft = mf[cellNum - width - 1]
            val left = mf[cellNum - 1]
            if (isMine(topMiddle)) bombs++
            if (isMine(topLeft)) bombs++
            if (isMine(left)) bombs++
        }
        'L' -> {
            val right = mf[cellNum + 1]
            val topMiddle = mf[cellNum - width]
            val topRight = mf[cellNum - width + 1]
            val bottomMiddle = mf[cellNum + width]
            val bottomRight = mf[cellNum + width + 1]
            if (isMine(right)) bombs++
            if (isMine(topMiddle)) bombs++
            if (isMine(topRight)) bombs++
            if (isMine(bottomMiddle)) bombs++
            if (isMine(bottomRight)) bombs++
        }
        'R' -> {
            val left = mf[cellNum - 1]
            val topLeft = mf[cellNum - width - 1]
            val topMiddle = mf[cellNum - width]
            val bottomLeft = mf[cellNum + width - 1]
            val bottomMiddle = mf[cellNum + width]
            if (isMine(left)) bombs++
            if (isMine(topLeft)) bombs++
            if (isMine(topMiddle)) bombs++
            if (isMine(bottomLeft)) bombs++
            if (isMine(bottomMiddle)) bombs++
        }
        'B' -> {
            val left = mf[cellNum - 1]
            val right = mf[cellNum + 1]
            val topLeft = mf[cellNum - width - 1]
            val topMiddle = mf[cellNum - width]
            val topRight = mf[cellNum - width + 1]
            if (isMine(left)) bombs++
            if (isMine(right)) bombs++
            if (isMine(topLeft)) bombs++
            if (isMine(topMiddle)) bombs++
            if (isMine(topRight)) bombs++
        }
        'T' -> {
            val left = mf[cellNum - 1]
            val right = mf[cellNum + 1]
            val bottomLeft = mf[cellNum + width - 1]
            val bottomMiddle = mf[cellNum + width]
            val bottomRight = mf[cellNum + width + 1]
            if (isMine(left)) bombs++
            if (isMine(right)) bombs++
            if (isMine(bottomLeft)) bombs++
            if (isMine(bottomMiddle)) bombs++
            if (isMine(bottomRight)) bombs++
        }
    }
        if (bombs == 0) return '/'
        return numToChar(bombs)
}

fun numToChar(num: Int): Char {
    return when (num) {
        0 -> '.'
        1 -> '1'
        2 -> '2'
        3 -> '3'
        4 -> '4'
        5 -> '5'
        6 -> '6'
        7 -> '7'
        8 -> '8'
        9 -> '9'
        else -> '.'
    }
}

fun cellType (cell: Int, height: Int = 9, width: Int = 9): Char {
    val size = height * width - 1
    if (cell == 0) return '1'
    if (cell == width - 1) return '2'
    if (cell < width) return 'T'
    if (cell == size - width + 1) return '3'
    if (cell == size) return '4'
    if (cell % width == 0) return 'L'
    if ((cell + 1)  % width == 0 ) return 'R'
    if (cell > size - width) return 'B'
    return 'o'
}

fun charCount(mf: CharArray, char: Char = 'X'): Int {
    var count = 0
    for (area in mf) {
        if (isMine(area, char)) {
            count++
        }
    }
    return count
}

fun isMine(char: Char, mine: Char = 'X'): Boolean {
    if (char == mine)
        return true
    return false
}

fun getInput(): String {
    val scanner = Scanner(System.`in`)
    return scanner.nextLine()
}