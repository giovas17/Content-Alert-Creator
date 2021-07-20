import org.json.JSONArray
import org.json.JSONObject

class SlackNotificationsCreator(arguments: Array<String>) {
    private var appName = defaultAppName

    init {
        readAndProcessArguments(arguments)
    }

    private fun readAndProcessArguments(arguments: Array<String>) {
        if (arguments.size < 5) {
            return
        }
        appName = arguments.find { it.toLowerCase().startsWith(appNameParam) }?.substring(appNameParam.length)
            ?: defaultAppName
        val action = arguments.find { it.toLowerCase().startsWith(jobParam) }?.substring(jobParam.length)
        val stateStr = arguments.find { it.toLowerCase().startsWith(stateParam) }?.substring(stateParam.length)
        val state = when (stateStr?.toLowerCase()) {
            "success" -> PipelineState.SUCCESS
            "failed" -> PipelineState.FAILED
            "aborted" -> PipelineState.ABORTED
            else -> PipelineState.ERRORED
        }
        val message = arguments.find { it.toLowerCase().startsWith(messageParam) }?.substring(messageParam.length)
        val linkToBuild = arguments.find { it.toLowerCase().startsWith(linkParam) }?.substring(linkParam.length)
        val pathFile = arguments.find { it.toLowerCase().startsWith(fileParam) }?.substring(fileParam.length)
        val versionName = arguments.find { it.toLowerCase().startsWith(versionNameParam) }
            ?.substring(versionNameParam.length)
        val slackFormatJSON = createAttachmentsJSON(action, state, message, linkToBuild, versionName)
        pathFile?.let { path -> writeFile(slackFormatJSON, path) }
    }

    private fun createAttachmentsJSON(
        action: String?,
        state: PipelineState?,
        message: String?,
        link: String?,
        version: String?
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
            val icon = if (state == PipelineState.SUCCESS) {
                if (action?.equals("test", true) == true) {
                    ":dart:"
                } else {
                    ":confetti_ball:"
                }
            } else {
                ":rotating_light:"
            }
            put(generateMarkdownJSON("*Message:*\n$message $icon"))
            put(generateMarkdownJSON("*Version:*\n$version"))
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

    private companion object {
        const val defaultAppName = "Thrive Market"
        const val jobParam = "job="
        const val stateParam = "state="
        const val messageParam = "message="
        const val linkParam = "link="
        const val fileParam = "file="
        const val appNameParam = "app_name="
        const val versionNameParam = "version_name="
    }
}