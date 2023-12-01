import java.io.File

fun getFirstAndLastDigitsFromString(input: String): Pair<Int, Int>? {
    val digits = input.filter { it.isDigit() }.map { it.digitToInt() }

    return if (digits.isNotEmpty()) {
        digits.first() to digits.last()
    } else {
        null
    }
}

fun getFirstAndLastDigitsFromStringWithWords(input: String): Pair<Int, Int>? {
    var first: Int? = null
    var last: Int? = null
    val numWords =
        listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    input.fold(0) { index, char ->
        if (char.isDigit()) {
            first = first ?: char.digitToInt()
            last = char.digitToInt()
        } else {
            numWords
                .withIndex()
                .firstOrNull {
                    it.value.startsWith(char) &&
                            index + it.value.length <= input.length &&
                            input.substring(index, index + it.value.length) == it.value
                }
                ?.let {
                    first = first ?: it.index
                    last = it.index
                }
        }

        index + 1
    }

    return first?.let { f -> last?.let { l -> f to l } }
}

fun main(args: Array<String>) {
    val sum =
        File(args[0])
            .readLines()
            .mapNotNull { getFirstAndLastDigitsFromStringWithWords(it) }
            .sumOf { 10 * it.first + it.second }

    println(sum)
}
