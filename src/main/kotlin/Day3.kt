import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * For part 1, find numbers and for every digit added to the number
 * check if there was an adjacent symbol,
 * when done keep or discard depending on if a symbol was found.
 */
fun getPart1Nums(input: List<String>): List<Int> {
    val res: List<Int> = mutableListOf()
    val current = StringBuilder()
    var symbolSeen = false
    val isSymbol: (Char) -> Boolean = { !(it == '.' || it.isDigit()) }

    for (y in input.indices) {
        for (x in input[y].indices) {
            val char = input[y][x]

            if (char == '.' || !char.isDigit()) {
                if (symbolSeen && current.isNotEmpty()) {
                    res.addLast(current.toString().toInt())
                }

                current.clear()
                symbolSeen = false

                continue
            }

            if (char.isDigit()) {
                current.append(char)

                for (dx in -1..1) {
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


/**
 * Given a star location, find all digits around it and return their product
 * only if there's exactly two of them.
 */
fun findGearRatio(input: MutableList<MutableList<Char>>, y: Int, x: Int): Int {
    if (input[y][x] != '*') {
        return 0
    }

    val parts: List<Int> = mutableListOf()

    for (dx in -1..1) {
        for (dy in -1..1) {
            val xx = x + dx
            val yy = y + dy

            if (yy in input.indices && xx in input[y].indices && input[yy][xx].isDigit()) {
                val chars = buildString {
                    var cx = xx
                    while (cx >= 0 && input[yy][cx].isDigit()) {
                        append(input[yy][cx])
                        input[yy][cx] = '.'
                        cx -= 1
                    }

                    reverse()

                    cx = xx + 1
                    while (cx < input[yy].size && input[yy][cx].isDigit()) {
                        append(input[yy][cx])
                        input[yy][cx] = '.'
                        cx += 1
                    }
                }

                parts.addLast(chars.toInt())

                if (parts.size > 2) {
                    return 0
                }
            }
        }
    }

    return when (parts.size) {
        2 -> parts[0] * parts[1]
        else -> 0
    }
}

/**
 * Part 2 takes the opposite approach to part 1, for every star find all digits around it.
 */
fun getPart2Nums(input: MutableList<MutableList<Char>>): Int =
    input.flatMapIndexed { y, row ->
        List(row.size) { x -> findGearRatio(input, y, x) }
    }.sum()

fun main(args: Array<String>) {
    val lines = Path(args[0]).readLines()

    println(getPart1Nums(lines).sum())
    println(getPart2Nums(lines.map { it.toMutableList() }.toMutableList()))
}
