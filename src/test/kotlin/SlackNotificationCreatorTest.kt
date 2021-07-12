import org.json.JSONArray
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode

class SlackNotificationCreatorTest {

    private val typeAlert = "Slack"
    private val filePath = "src/test/resources/slack/test.txt"
    private val manager = FileManager()

    @Test
    fun buildFileCreation() {
        val typeBuild = "Build"
        val statePipeline = "Success"
        val message = "'This is a test message for the alert'"
        val linkForBuild = "https://www.example.com"

        SlackNotificationsCreator(mockArgumentsToSlackCreation(typeBuild, statePipeline, message, linkForBuild))
        val contentBuildJson = JSONArray(manager.readFileAsStringResult("src/test/resources/slack/slack-build-success.json"))
        val contentResultFile = JSONArray(manager.readFileAsStringResult(filePath))
        JSONAssert.assertEquals(contentBuildJson, contentResultFile, JSONCompareMode.LENIENT)
    }

    @Test
    fun buildFileCreationWithError() {
        val typeBuild = "Build"
        val statePipeline = "Errored"
        val message = "'This is a test error message for the alert'"
        val linkForBuild = "https://www.example.com/error"

        SlackNotificationsCreator(mockArgumentsToSlackCreation(typeBuild, statePipeline, message, linkForBuild))
        val contentBuildJson = JSONArray(manager.readFileAsStringResult("src/test/resources/slack/slack-build-error.json"))
        val contentResultFile = JSONArray(manager.readFileAsStringResult(filePath))
        JSONAssert.assertEquals(contentBuildJson, contentResultFile, JSONCompareMode.LENIENT)
    }

    private fun mockArgumentsToSlackCreation(typeBuild: String, state: String, message: String, link: String) = arrayOf(
        typeAlert, typeBuild, state, message, link, filePath
    )
}