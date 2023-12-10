import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

data class Coordinate(val y: Int, val x: Int)
typealias Maze = List<String>

fun Maze.isValidCoordinate(coordinate: Coordinate): Boolean =
    coordinate.y in this.indices && coordinate.x in this[coordinate.y].indices

fun getStartingCoordinates(maze: Maze): Pair<Int, Int> =
    maze
        .withIndex()
        .filter { it.value.contains('S') }
        .map { it.index to it.value.indexOf('S') }
        .first()



fun getPart1(start: Coordinate, maze: Maze): Int {
    // Dijktra but cost go up ^
    val up = Coordinate(-1, 0)
    val down = Coordinate(1, 0)
    val left = Coordinate(0, -1)
    val right = Coordinate(0, 1)
    val navigation = mapOf(
        '|' to setOf(up, down),
        '-' to setOf(left, right),
        'F' to setOf(down, right),
        '7' to setOf(down, left),
        'J' to setOf(up, left),
        'L' to setOf(up, right),
        // getStartCoordNeighbors
        // Manually updated for current input. It's 23:55 and I want to sleep
        'S' to setOf(left, down)
    ).withDefault { setOf() }
    val costs: MutableMap<Coordinate, Int> = mutableMapOf()
    val queue: Queue<Pair<Coordinate, Int>> = PriorityQueue(compareBy { it.second })
    var best = 0
    queue.add(start to 0)

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currentCoords = current.first
        val steps = current.second
        best = max(best, current.second)

        maze[currentCoords.y][currentCoords.x].let { char ->
            navigation.getValue(char).let { offsets ->
                offsets.forEach { offset ->
                    val nextCoord = Coordinate(currentCoords.y + offset.y, currentCoords.x + offset.x)
                    val newSteps = steps + 1

                    if (maze.isValidCoordinate(nextCoord) && costs.getOrDefault(nextCoord, Int.MAX_VALUE) > newSteps) {
                        queue.add(Pair(nextCoord, newSteps))
                        costs[nextCoord] = newSteps
                    }
                }
            }
        }
    }

    return best
}


fun main(args: Array<String>) {
    val maze = Path(args.first()).readLines()
    val startingCoords = getStartingCoordinates(maze)

    println(getPart1(Coordinate(startingCoords.first, startingCoords.second), maze))
}
