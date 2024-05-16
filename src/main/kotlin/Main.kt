fun main() {
    var previousMonthlyTransfers = 0

    while (true) {
        println("Введите сумму для перевода (для выхода введите 0):")
        val transferAmount = readLine()!!.toInt()

        if (transferAmount == 0) {
            break
        }

        val result = calculateCommission("Mastercard", previousMonthlyTransfers, transferAmount)
        println("Перевод ${result.first}. Комиссия ${result.second}")

        previousMonthlyTransfers += transferAmount
    }
}

fun calculateCommission(cardType: String = "Мир", previousMonthlyTransfers: Int, transferAmount: Int): Pair<Int, Int> {
    if (isTransferBlocked(transferAmount, previousMonthlyTransfers)) {
        println("Операция заблокирована из-за превышения лимита")
        return Pair(0, 0)
    }

    val commission = when (cardType) {
        "Mastercard" -> calculateMastercardCommission(previousMonthlyTransfers, transferAmount)
        "Visa" -> calculateVisaCommission(transferAmount)
        "Мир" -> 0
        else -> throw IllegalArgumentException("Не верный тип карты")
    }

    return Pair(transferAmount - commission, commission)
}

fun isTransferBlocked(transferAmount: Int, previousMonthlyTransfers: Int): Boolean =
    transferAmount > 150000 || previousMonthlyTransfers + transferAmount > 600000

fun calculateMastercardCommission(previousMonthlyTransfers: Int, transferAmount: Int): Int {
    val limit =  75000
    val exceededAmount = (previousMonthlyTransfers + transferAmount) - limit
    return  if (exceededAmount > 0) {
        val commissionAmount = if (exceededAmount > transferAmount) transferAmount else exceededAmount
        (commissionAmount * 0.006).toInt() + 20
    } else {
        0
    }

}


fun calculateVisaCommission(transferAmount: Int): Int {
    val baseCommission = (transferAmount * 0.0075).toInt()
    return if (baseCommission < 35) 35 else baseCommission
}