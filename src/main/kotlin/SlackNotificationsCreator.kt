import org.json.JSONArray
import org.json.JSONObject

class SlackNotificationsCreator(arguments: Array<String>) {
    private val appName = "ThiveMarketTest"

    init {
        readAndProcessArguments(arguments)
    }

    private fun readAndProcessArguments(arguments: Array<String>) {
        if (arguments.size < 4) {
            return
        }
        val action = arguments[1]
        val state = when (arguments[2].toLowerCase()) {
            "success" -> PipelineState.SUCCESS
            "failed" -> PipelineState.FAILED
            "aborted" -> PipelineState.ABORTED
            else -> PipelineState.ERRORED
        }
        val message = arguments[3]
        val linkToBuild = arguments[4]
        val pathFile = arguments[5]
        val slackFormatJSON = createAttachmentsJSON(action, state, message, linkToBuild)
        writeFile(slackFormatJSON, pathFile)
    }

    private fun createAttachmentsJSON(
        action: String?,
        state: PipelineState?,
        message: String?,
        link: String?
    ): JSONArray {
        val attachments = JSONArray()
        val blocks = JSONArray()
        val headerContent = generateMarkdownJSON("*$appName*") // AppName
        val header = JSONObject().apply {
            put("type", "section")
            put("text", headerContent)
        }
        val fields = JSONArray().apply {
            put(generateMarkdownJSON("*Job:*\n$action"))
            put(generateMarkdownJSON("*State:*\n${state?.name}"))
            put(generateMarkdownJSON("*Message:*\n$message"))
            put(generateMarkdownJSON("*Version:*\n2.0"))
        }
        val dataContent = JSONObject().apply {
            put("type", "section")
            put("fields", fields)
        }
        val footerContent = JSONArray().apply {
            put(generateMarkdownJSON(link))
        }
        val footer = JSONObject().apply {
            put("type", "context")
            put("elements", footerContent)
        }
        blocks.apply {
            put(header)
            put(dataContent)
            put(footer)
        }
        attachments.apply {
            val content = JSONObject().apply {
                put("color", state?.rgbColor)
                put("blocks", blocks)
            }
            put(content)
        }
        println(attachments.toString())
        return attachments
    }

    private fun generateMarkdownJSON(message: String?) = JSONObject().apply {
        put("type", "mrkdwn")
        put("text", message)
    }

    private fun writeFile(slackContent: JSONArray, pathFile: String) = with(FileManager()) {
        writeFile(slackContent.toString(), pathFile)
    }
}