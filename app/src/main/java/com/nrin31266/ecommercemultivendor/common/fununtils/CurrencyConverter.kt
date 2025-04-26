package com.nrin31266.ecommercemultivendor.common.fununtils

import java.text.DecimalFormat

object CurrencyConverter {
    fun toVND(amount: Long): String {
        val formatter = DecimalFormat("#,###").apply {
            decimalFormatSymbols = decimalFormatSymbols.apply { groupingSeparator = '.' }
        }

        return "₫" + formatter.format(amount)
    }
}