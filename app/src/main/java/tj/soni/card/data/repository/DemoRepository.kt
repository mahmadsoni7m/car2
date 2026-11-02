package tj.soni.card.data.repository

import tj.soni.card.data.models.DemoTransaction

class DemoRepository {
    val transactions = listOf(
        DemoTransaction("SC-1001", "Маош", "Воридшавӣ · Demo", 2450.0, true, "02.11.2026", "income"),
        DemoTransaction("SC-1002", "Супермаркет", "Харид · Demo", 186.5, false, "01.11.2026", "shopping"),
        DemoTransaction("SC-1003", "Интиқоли demo", "Аз ҳамён · Demo", 320.0, true, "31.10.2026", "transfer"),
        DemoTransaction("SC-1004", "Пардохти интернет", "Хизматрасонӣ · Demo", 75.0, false, "30.10.2026", "bills"),
        DemoTransaction("SC-1005", "Қаҳвахона", "Хӯрок · Demo", 48.0, false, "29.10.2026", "food"),
        DemoTransaction("SC-1006", "Бонус", "Мукофот · Demo", 12.5, true, "28.10.2026", "income")
    )
    val balance = 4820.50
}
