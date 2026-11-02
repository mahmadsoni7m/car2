package tj.soni.card.navigation

object Routes {
    const val DASHBOARD = "dashboard"
    val all = listOf("splash", "onboarding", "welcome", "phone_login", "sms_verification", "pin_login", "biometric", "registration", DASHBOARD, "cards", "card_details", "add_card", "transfers", "transfer_confirmation", "payments", "qr_scanner", "top_up", "withdraw", "history", "transaction_details", "statistics", "notifications", "profile", "security", "settings", "language", "support", "faq", "about")
    fun title(route: String): String = route.split('_').joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}
