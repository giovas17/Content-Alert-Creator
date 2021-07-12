const val SLACK = "Slack"
const val APP_CENTER = "AppCenter"

fun main(args: Array<String>) {
    if (args.isEmpty()) return
    // Check if we want to generate Slack file for CI
    if (args[0].equals(SLACK, false)) {
        SlackNotificationsCreator(args)
    }
    for (arg in args) {
        println(arg)
    }
}