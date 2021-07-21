class AppCenterInformationCreator(arguments: Array<String>) {
    init {
        readAndProcessArguments(arguments)
    }

    private fun readAndProcessArguments(arguments: Array<String>) {
        val platform = arguments.find { it.toLowerCase().startsWith(platformParam) }?.substring(platformParam.length)
            ?: defaultPlatform
        val buildTitle = arguments.find { it.toLowerCase().startsWith(titleParam) }?.substring(titleParam.length)
            ?.replace("'", "")
        val jiraTicket = arguments.find { it.toLowerCase().startsWith(ticketParam) }?.substring(ticketParam.length)
        val filePath = arguments.find { it.toLowerCase().startsWith(fileParam) }?.substring(fileParam.length)
        val environment = arguments.find { it.toLowerCase().startsWith(environmentParam) }
            ?.substring(environmentParam.length) ?: defaultEnvironment
        val optimizedTesting = arguments.find { it.toLowerCase().startsWith(optimizedTestingParam) }
            ?.substring(optimizedTestingParam.length) ?: noneValue
        val amplitudeTesting = arguments.find { it.toLowerCase().startsWith(amplitudeTestingParam) }
            ?.substring(amplitudeTestingParam.length) ?: noneValue
        val additionalInfo = arguments.find { it.toLowerCase().startsWith(additionalInfoParam) }
            ?.substring(additionalInfoParam.length) ?: noneValue
        processInformation(platform, buildTitle, jiraTicket, environment, optimizedTesting,
            amplitudeTesting, additionalInfo, filePath)
    }

    private fun processInformation(
        platform: String,
        buildTitle: String?,
        jiraTicket: String?,
        environment: String,
        optimizedTesting: String,
        amplitudeTesting: String,
        additionalInfo: String,
        filePath: String?
    ) {
        val buffer = StringBuffer()
        with(buffer) {
            append(platform).append(" - ").append(buildTitle)
            append("\n\n")
            append("What's included in this build: ").append("[$linkToJira$jiraTicket]").append("($linkToJira$jiraTicket)")
            append("\n\n")
            append("Environment: ").append(environment)
            append("\n\n")
            append("Optimized Testing: ").append(optimizedTesting)
            append("\n\n")
            append("Amplitude Testing: ").append(amplitudeTesting)
            append("\n\n")
            append("Additional Info: ").append(additionalInfo)
        }
        filePath?.let { createMDFile(buffer.toString(), it) }
    }

    private fun createMDFile(content: String, pathFile: String) = with(FileManager()) {
        writeFile(content, pathFile)
    }

    private companion object {
        const val platformParam = "platform="
        const val titleParam = "title="
        const val ticketParam = "jira="
        const val environmentParam = "environment="
        const val optimizedTestingParam = "optimized_testing="
        const val amplitudeTestingParam = "amplitude_testing="
        const val additionalInfoParam = "additional_info="
        const val fileParam = "file="
        const val noneValue = "None"
        const val defaultEnvironment = "Staging"
        const val defaultPlatform = "Android"
        const val linkToJira = "https://thrivemarket.atlassian.net/browse/"
    }
}