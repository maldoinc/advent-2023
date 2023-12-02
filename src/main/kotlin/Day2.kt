import java.io.File

data class DeckSize(val red: Int, val green: Int, val blue: Int)

fun getColorValues(draw: String, color: String): Sequence<Int> =
    Regex("(\\d+) $color").findAll(draw).map { it.groupValues[1].toInt() }

fun getColorSum(draw: String, color: String): Int =
    getColorValues(draw, color).sum()

fun getLargestColor(draw: String, color: String): Int =
    getColorValues(draw, color).max()

fun isGamePossible(game: String, deckSize: DeckSize): Boolean =
    game.split(';').firstOrNull {
        getColorSum(it, "red") > deckSize.red ||
                getColorSum(it, "green") > deckSize.green ||
                getColorSum(it, "blue") > deckSize.blue
    } == null

fun findLargestDrawSize(draws: String): DeckSize =
    DeckSize(
        red = getLargestColor(draws, "red"),
        green = getLargestColor(draws, "green"),
        blue = getLargestColor(draws, "blue")
    )

fun main(args: Array<String>) {
    val deck = DeckSize(red = 12, green = 13, blue = 14)
    val part1 =
        File(args[0])
            .readLines()
            .map { it.substring(5) }
            .map { it.split(':') }
            .filter { isGamePossible(it[1], deck) }
            .sumOf { it[0].toInt() }

    val part2 =
        File(args[0])
            .readLines()
            .map { findLargestDrawSize(it) }
            .sumOf { it.red * it.green * it.blue }

    println("Part1: $part1")
    println("Part2: $part2")
}
