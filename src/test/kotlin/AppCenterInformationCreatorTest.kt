import org.junit.Test
import kotlin.test.assertTrue

class AppCenterInformationCreatorTest {

    private val typeAlert = "AppCenter"
    private val filePath = "src/test/resources/app_center/test.md"
    private val prInformationPath = "src/test/resources/app_center/pr-information.json"
    private val manager = FileManager()

    @Test
    fun defaultParamsCreation() {
        AppCenterInformationCreator(mockArgumentsForAppCenterInfoDefault(prInformation = prInformationPath))
        val resultFileContent = manager.readFileAsStringResult(filePath)
        val expectedFileContent =
            manager.readFileAsStringResult("src/test/resources/app_center/app-center-test-default.md")
        assertTrue(
            expectedFileContent.equals(resultFileContent, true),
            "The content of the AppCenter info is not the same"
        )
    }

    @Test
    fun productionParamsCreation() {
        val environment = "Production"
        val optimizedTesting = "'Here goes the optimized testing text'"
        val amplitudeTesting = "'Here goes the amplitude testing text'"
        val additionalInfo = "'Here goes the additional info text'"
        AppCenterInformationCreator(
            mockArgumentsForAppCenterInfoDefault(
                environment,
                optimizedTesting,
                amplitudeTesting,
                additionalInfo,
                prInformationPath
            )
        )
        val resultFileContent = manager.readFileAsStringResult(filePath)
        val expectedFileContent =
            manager.readFileAsStringResult("src/test/resources/app_center/app-center-test-full-info.md")
        assertTrue(
            expectedFileContent.equals(resultFileContent, true),
            "The content of the AppCenter info is not the same"
        )
    }

    private fun mockArgumentsForAppCenterInfoDefault(
        environment: String = "",
        optimizedTesting: String = "",
        amplitudeTesting: String = "",
        additionalInfo: String = "",
        prInformation: String
    ) = arrayOf(
        typeAlert,
        "environment=$environment",
        "optimized_testing=$optimizedTesting",
        "pr_information=$prInformation",
        "amplitude_testing=$amplitudeTesting",
        "additional_info=$additionalInfo",
        "file=$filePath"
    )

}