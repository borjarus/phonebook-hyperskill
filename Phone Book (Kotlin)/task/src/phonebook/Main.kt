package phonebook

import java.io.File
import kotlin.system.measureTimeMillis

data class Entry(val number: String, val name: String)

fun parseDirectoryLine(line: String): Entry? =
    line.split(" ", limit = 2)
        .takeIf { it.size == 2 }
        ?.let { return Entry(it[0], it[1]) }

fun loadDirectory(path: String): List<Entry> =
    File(path)
        .useLines { seq -> seq.mapNotNull(::parseDirectoryLine).toList() }

fun loadQueries(path: String): List<String> =
    File(path).readLines()

fun linearSearchCount(entries: List<Entry>, queries: List<String>): Int =
    queries.count { q ->
        entries.any { it.name == q }
    }

fun formatDuration(millis: Long): String {
    val minutes = millis / 60_000
    val seconds = (millis % 60_000) / 1_000
    val ms = millis % 1000
    return "$minutes min. $seconds sec. $ms ms."
}

fun main() {
    val directoryPath = "directory.txt"
    val findPath = "find.txt"

    val directory = loadDirectory(directoryPath)
    val queries = loadQueries(findPath)

    println("Start searching...")
    var foundCount: Int
    val time = measureTimeMillis {
        foundCount = linearSearchCount(directory, queries)
    }

    println("Found $foundCount / ${queries.size} entries. Time taken: ${formatDuration(time)}")
}
