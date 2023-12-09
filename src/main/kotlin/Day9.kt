import kotlin.io.path.Path
import kotlin.io.path.readLines

fun findScorePart1(records: List<List<Int>>): Int =
    records.foldRight(0) { ints, acc -> acc + ints.last() }

fun findScorePart2(records: List<List<Int>>): Int =
    records.foldRight(0) { ints, acc -> ints.first() - acc }

fun main(args: Array<String>) {
    val records =
        Path(args.first())
            .readLines()
            .map { it.split(" ").map(String::toInt) }
            .map { it ->
                val rows: List<List<Int>> = mutableListOf(it)

                while (!rows.last().all { it == 0 }) {
                    val last = rows.last()
                    val row = (1 ..< last.size).map { last[it] - last[it - 1] }

                    if (row.isEmpty()) {
                        break
                    }

                    rows.addLast(row)
                }

                rows
            }

    println(records.sumOf { findScorePart1(it) })
    println(records.sumOf { findScorePart2(it) })
}
