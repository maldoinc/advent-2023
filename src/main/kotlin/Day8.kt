import java.util.function.Predicate
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun getSteps(
    instructions: String,
    navigation: Map<String, Pair<String, String>>,
    start: String,
    endPredicate: Predicate<String>
): Int {
    var location = start
    var locationIndex = 0
    var steps = 0

    while (!endPredicate.test(location)) {
        val next = navigation[location]!!
        location =
            if (instructions[locationIndex] == 'L') {
                next.first
            } else {
                next.second
            }
        locationIndex += 1
        locationIndex %= instructions.length
        steps += 1
    }

    return steps
}

fun getPart2(instructions: String, navigation: Map<String, Pair<String, String>>): List<Int> {
    return navigation.keys
        .filter { it.endsWith('A') }
        .map { it -> getSteps(instructions, navigation, it) { it.endsWith('Z') } }
}

fun main(input: Array<String>) {
    val lines = Path(input[0]).readLines()
    val instructions = lines.removeFirst()
    lines.removeFirst()
    val navigation =
        lines
            .map { it.split(" = ") }
            .map { it[0] to it[1].replace("(", "").replace(")", "").split(", ") }
            .associate { it.first to (it.second[0] to it.second[1]) }

    println(getSteps(instructions, navigation, "AAA") { it == "ZZZ" })
    println(getPart2(instructions, navigation))
    // python3 -c 'import math; print(math.lcm(*$parts))'. Yeah...
}
