package com.zzz.feature_trip.overview.domain


/**
 * @param code Currency code e.g. USD
 * @param symbol e.g. $
 * @param emoji Country flag emoji
 */
internal data class UICurrency(
    val code : String="USD",
    val symbol : String = "$",
    val emoji : String = ""
)

/**
 * Has about 30 currencies and their symbols.
 *
 * Maps name to their symbols
 */
internal val currencySymbolMap = mapOf(
    "USD" to "$",
    "EUR" to "€",
    "GBP" to "£",
    "INR" to "₹",
    "JPY" to "¥",
    "CNY" to "¥",
    "KRW" to "₩",
    "AUD" to "A$",
    "CAD" to "C$",
    "CHF" to "CHF",
    "SEK" to "kr",
    "NOK" to "kr",
    "DKK" to "kr",
    "NZD" to "NZ$",
    "SGD" to "S$",
    "HKD" to "HK$",
    "ZAR" to "R",
    "BRL" to "R$",
    "MXN" to "MX$",
    "RUB" to "₽",
    "TRY" to "₺",
    "AED" to "د.إ",
    "SAR" to "﷼",
    "IDR" to "Rp",
    "THB" to "฿",
    "MYR" to "RM",
    "VND" to "₫",
    "NGN" to "₦",
    "PKR" to "₨",
    "EGP" to "E£"
)