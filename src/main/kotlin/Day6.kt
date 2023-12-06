import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun waysToWin(time: Long, distance: Long): Long {
    val x1 = 0.5 * (time - sqrt(time * time - 4.0 * distance))
    val x2 = 0.5 * (time + sqrt(time * time - 4.0 * distance))

    return (ceil(x2) - floor(x1)).toLong() - 1
}

fun main(args: Array<String>) {
    val lines = Path(args[0])
        .readLines()
        .map { it.split(":").last() }
        .map { it.trim() }

    val nums = lines
        .map { it.split(" ")
            .filter(String::isNotEmpty)
            .map(String::toLong) }
        .toList()

    val part1 =
        nums
            .first()
            .zip(nums.last())
            .map { waysToWin(time = it.first, distance = it.second) }
            .fold(1L) { acc, i -> acc * i }

    val part2 = lines
        .map { it.replace(" ", "") }
        .map { it.toLong() }
        .toList()

    println(part1)
    println(waysToWin(part2.first(), part2.last()))
}
