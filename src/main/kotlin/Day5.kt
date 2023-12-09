import kotlin.io.path.Path
import kotlin.io.path.readLines

data class Mapping(val destinationStart: Long, val sourceStart: Long, val size: Long)

fun getSection(contents: String, sectionName: String) =
    Regex("`$sectionName map:`(.+?)(`[a-z]|$)")
        .findAll(contents)
        .map { it.groupValues[1] }
        .first()
        .split("`")
        .map { it.split(" ").map(String::toLong).toList() }
        .map { Mapping(it[0], it[1], it[2]) }
        .toList()

fun getLocation(seed: Long, transformations: List<List<Mapping>>): Long =
    transformations.fold(seed) { num, mappings ->
        mappings
            .filter { num >= it.sourceStart && num <= it.sourceStart + it.size }
            .map { it.destinationStart + (num - it.sourceStart) }
            .firstOrNull() ?: num
    }

fun main(args: Array<String>) {
    val contents = Path(args[0]).readLines()
    val seeds = contents.first().split(": ").last().split(" ").map(String::toLong).toSet()
    val normalizedContents = contents.filter(String::isNotEmpty).joinToString("`")
    val transformations =
        listOf(
            getSection(normalizedContents, "seed-to-soil"),
            getSection(normalizedContents, "soil-to-fertilizer"),
            getSection(normalizedContents, "fertilizer-to-water"),
            getSection(normalizedContents, "water-to-light"),
            getSection(normalizedContents, "light-to-temperature"),
            getSection(normalizedContents, "temperature-to-humidity"),
            getSection(normalizedContents, "humidity-to-location"),
        )
    val part1 = seeds.minOf { getLocation(it, transformations) }
    val part2 =
        seeds
            .windowed(2)
            .parallelStream() // 15$ for this
            .map { range ->
                var min = Long.MAX_VALUE
                var iteration = 0
                println(range)
                for (i in generateSequence(0L) { if (it == range[1]) null else it + 1 }) {
                    iteration += 1

                    if (iteration % 10_000_000 == 0) {
                        val percent = 100 * i.toDouble() / range[1].toDouble()
                        println("$percent\t:${range[0]}\t${range[1]}")
                        iteration = 0
                    }

                    min = min.coerceAtMost(getLocation(range[0] + i, transformations))
                }

                min
            }
            .toList()
            .min()

    println(part1)
    println(part2)
}
