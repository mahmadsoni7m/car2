package tj.soni.card.data.models

data class DemoUser(val name: String = "Маҳмад", val phone: String = "+992 ** *** ** 21")
data class DemoTransaction(val id: String, val title: String, val subtitle: String, val amount: Double, val incoming: Boolean, val date: String, val category: String) {
    val signedAmount: String get() = (if (incoming) "+" else "−") + "%.2f TJS".format(amount)
}
enum class TxFilter { ALL, INCOME, EXPENSE }
