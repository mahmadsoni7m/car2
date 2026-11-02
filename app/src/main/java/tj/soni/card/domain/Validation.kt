package tj.soni.card.domain

object Validation {
    fun phone(value: String): Boolean = value.filter(Char::isDigit).length in 9..12
    fun name(value: String): Boolean = value.trim().length >= 2
    fun search(value: String): String = value.trim().take(60)
}
