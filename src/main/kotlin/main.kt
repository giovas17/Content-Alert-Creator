const val SLACK = "Slack"
const val APP_CENTER = "AppCenter"

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    // Check if we want to generate Slack file for CI
    if (args[0].equals(SLACK, false)) {
        SlackNotificationsCreator(args)
    } else if (args[0].equals(APP_CENTER, true)) {
        // Check if we want to generate AppCenter file for CI
        AppCenterInformationCreator(args)
    }
    for (arg in args) {
        println(arg)
    }
}