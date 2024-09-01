package cat.app.bicleaner.util

import java.io.File

class FileHelper(dir: File, filename: String) {

    private val file = File(dir, filename)

    private fun create() {
        file.createNewFile()
    }

    private fun append(s: String) {
        if (!file.exists()) create()
        file.appendText(s, Charsets.UTF_8)
    }

    private fun line(s: String): Boolean {
        if (s.isBlank()) return false
        append(s + System.lineSeparator())
        return true
    }

    fun clear() {
        if (file.exists()) file.delete()
        file.createNewFile()
    }


    fun lines(strings: List<String>): Int {
        var cnt = 0
        strings.forEach { if (line(it)) cnt++ }
        return cnt
    }

    fun read(): String {
        if (!file.exists()) return ""
        return file.readText(Charsets.UTF_8)
    }

    fun reads(): List<String> {
        if (!file.exists()) return emptyList()
        return file.readLines(Charsets.UTF_8).filter(String::isNotBlank)
    }

}