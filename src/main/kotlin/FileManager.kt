import java.io.File

class FileManager {
    fun writeFile(content: String, pathFile: String) {
        val file = File(pathFile)
        file.printWriter().use {
            it.println(content)
            it.flush()
        }
    }

    fun readFileAsStringResult(pathFile: String): String = File(pathFile).readText()
}