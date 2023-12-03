import java.io.File
import kotlin.text.StringBuilder


fun collectDigitsAround(input: MutableList<MutableList<Char>>, y: Int, x: Int): Int {
    val parts: List<Int> = mutableListOf()

    for(dx in -1..1) {
        for(dy in -1..1) {
            if (dy == 0 && dx == 0) {
                continue
            }

            val xx = x + dx
            val yy = y + dy

            if (yy in input.indices && xx in input[y].indices && input[yy][xx].isDigit()) {
                val chars = StringBuilder()

                var cx = xx

                while (cx >= 0 && input[yy][cx].isDigit()) {
                    chars.append(input[yy][cx])
                    input[yy][cx] = '.'
                    cx -= 1
                }

                chars.reverse()

                cx = xx + 1

                while (cx < input[yy].size && input[yy][cx].isDigit()) {
                    chars.append(input[yy][cx])
                    input[yy][cx] = '.'
                    cx += 1
                }

                parts.addLast(chars.toString().toInt())

                if (parts.size > 2) {
                    return 0
                }
            }
        }
    }

    return if (parts.size == 2) { parts[0] * parts[1] } else { 0 }
}

fun isSymbol(c: Char) = !(c.isDigit() || c == '.')

fun getPart1Nums(input: List<String>): List<Int> {
    val res: List<Int> = mutableListOf()
    val current = StringBuilder()
    var symbolSeen = false

    for(y in input.indices) {
        for (x in input[y].indices) {
            val char = input[y][x]

            if (char == '.' || !char.isDigit()) {
                if (symbolSeen && current.isNotEmpty()) {
                    res.addLast(current.toString().toInt())
                    current.clear()
                }

                current.clear()
                symbolSeen = false

                continue
            }

            if (char.isDigit()) {
                current.append(char)

                for(dx in -1..1) {
                    for (dy in -1..1) {
                        val xx = x + dx
                        val yy = y + dy

                        if (yy in input.indices && xx in input[yy].indices) {
                            symbolSeen = symbolSeen or isSymbol(input[yy][xx])
                        }
                    }
                }
            }
        }
    }

    if (current.isNotEmpty()) {
        res.addLast(current.toString().toInt())
    }

    return res
}

fun getPart2Nums(input: List<String>): Int {
    var sum = 0
    val mutableInput = input.map { it.toMutableList() }.toMutableList()

    for(y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == '*') {
                sum += collectDigitsAround(mutableInput, y=y, x=x)
            }
        }
    }

    return sum
}


fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()

    println(getPart1Nums(lines).sum())
    println(getPart2Nums(lines))
}
