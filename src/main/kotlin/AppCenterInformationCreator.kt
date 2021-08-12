class AppCenterInformationCreator(arguments: Array<String>) {

    private lateinit var prInfo: PrInformationManager

    init {
        readAndProcessArguments(arguments)
    }

    private fun readAndProcessArguments(arguments: Array<String>) {
        val platform = arguments.find { it.toLowerCase().startsWith(platformParam) }?.substring(platformParam.length)
            ?: defaultPlatform
        val prInformationPath = arguments.find { it.toLowerCase().startsWith(prParam) }?.substring(prParam.length)
        val prInformation = FileManager().readFileAsStringResult(prInformationPath.orEmpty())
        prInfo = PrInformationManager(prInformation)
        val buildTitle = getTitlePr()
        val jiraTicket = getJiraTicketNumber()
        val filePath = arguments.find { it.toLowerCase().startsWith(fileParam) }?.substring(fileParam.length)
        val environment = (arguments.find { it.toLowerCase().startsWith(environmentParam) }
            ?.substring(environmentParam.length) ?: defaultEnvironment).ifEmpty { defaultEnvironment }
        val optimizedTesting = (arguments.find { it.toLowerCase().startsWith(optimizedTestingParam) }
            ?.substring(optimizedTestingParam.length)?.replace("'", "") ?: noneValue).ifEmpty { noneValue }
        val amplitudeTesting = (arguments.find { it.toLowerCase().startsWith(amplitudeTestingParam) }
            ?.substring(amplitudeTestingParam.length)?.replace("'", "") ?: noneValue).ifEmpty { noneValue }
        val additionalInfo = (arguments.find { it.toLowerCase().startsWith(additionalInfoParam) }
            ?.substring(additionalInfoParam.length)?.replace("'", "") ?: noneValue).ifEmpty { noneValue }
        processInformation(
            platform, buildTitle, jiraTicket, environment, optimizedTesting,
            amplitudeTesting, additionalInfo, filePath
        )
    }

    private fun getTitlePr(): String {
        val indexDelimiter = prInfo.title?.indexOf(delimiter) ?: -1
        return if ((indexDelimiter != -1) && prInfo.title?.startsWith("AN") == true) {
            prInfo.title?.substring(indexDelimiter + 3, prInfo.title?.length ?: 0).orEmpty().trim()
        } else {
            prInfo.title.orEmpty()
        }
    }

    private fun getJiraTicketNumber(): String {
        val indexDelimiter = prInfo.title?.indexOf(delimiter) ?: -1
        return if ((indexDelimiter != -1) && prInfo.title?.startsWith("AN") == true) {
            prInfo.title?.substring(0, indexDelimiter).orEmpty().trim()
        } else {
            ""
        }
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
            append("What's included in this build: ").append("[$linkToJira$jiraTicket]")
                .append("($linkToJira$jiraTicket)")
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
        const val prParam = "pr_information="
        const val environmentParam = "environment="
        const val optimizedTestingParam = "optimized_testing="
        const val amplitudeTestingParam = "amplitude_testing="
        const val additionalInfoParam = "additional_info="
        const val fileParam = "file="
        const val noneValue = "None"
        const val defaultEnvironment = "Staging"
        const val defaultPlatform = "Android"
        const val linkToJira = "https://thrivemarket.atlassian.net/browse/"
        const val delimiter = " - "
    }
}