import org.junit.Test
import kotlin.test.assertTrue

class AppCenterInformationCreatorTest {

    private val typeAlert = "AppCenter"
    private val filePath = "src/test/resources/app_center/test.md"
    private val manager = FileManager()

    @Test
    fun defaultParamsCreation() {
        val title = "'This is the test title'"
        val ticketNumber = "AN-13111"

        AppCenterInformationCreator(mockArgumentsForAppCenterInfoDefault(title, ticketNumber))
        val expectedFileContent = manager.readFileAsStringResult("src/test/resources/app_center/app-center-test-default.md")
        val resultFileContent = manager.readFileAsStringResult(filePath)
        assertTrue(expectedFileContent.equals(resultFileContent, true),
            "The content of the AppCenter info is not the same")
    }

    private fun mockArgumentsForAppCenterInfoDefault(title: String, ticket: String) = arrayOf(
        typeAlert, "title=$title", "jira=$ticket", "file=$filePath"
    )

}