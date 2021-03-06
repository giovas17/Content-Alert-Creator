import org.json.JSONArray

class PrInformationManager(jsonContent: String) {

    var number: String? = null
    var title: String? = null
    var url: String? = null
    var sourceBranch: String? = null
    var commitNumber: String? = null
    var destinationBranch: String? = null
    var destinationLastCommitNumber: String? = null
    var message: String? = null
    var author: String? = null
    var authorEmail: String? = null
    var state: String? = null
    private lateinit var prInformation: JSONArray
    private var info: HashMap<String, String> = HashMap()

    init {
        processInformation(jsonContent)
    }

    private fun processInformation(jsonContent: String) {
        prInformation = JSONArray(jsonContent)
        for (index in 0 until prInformation.length()) {
            val obj = prInformation.getJSONObject(index)
            info[obj.getString("name")] = obj.optString("value", "")
        }
        number = findValueFor("pr")
        title = findValueFor("title")
        url = findValueFor("url")
        sourceBranch = findValueFor("head_name")
        commitNumber = findValueFor("head_sha")
        destinationBranch = findValueFor("base_name")
        destinationLastCommitNumber = findValueFor("base_sha")
        message = findValueFor("message")
        author = findValueFor("author")
        authorEmail = findValueFor("author_email")
        state = findValueFor("state")
    }

    private fun findValueFor(key: String) : String = info[key].orEmpty()

    fun getTitlePr(): String {
        val indexDelimiter = title?.indexOf(delimiter) ?: -1
        return if ((indexDelimiter != -1) && title?.startsWith("AN") == true) {
            title?.substring(indexDelimiter + 3, title?.length ?: 0).orEmpty().trim()
        } else {
            title.orEmpty()
        }
    }

    fun getJiraTicketNumber(): String {
        val indexDelimiter = title?.indexOf(delimiter) ?: -1
        return if ((indexDelimiter != -1) && title?.startsWith("AN") == true) {
            title?.substring(0, indexDelimiter).orEmpty().trim()
        } else {
            ""
        }
    }

    private companion object {
        const val delimiter = " - "
    }
}