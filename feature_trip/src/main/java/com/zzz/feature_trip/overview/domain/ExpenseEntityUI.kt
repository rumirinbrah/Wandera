package com.zzz.feature_trip.overview.domain

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.zzz.data.trip.model.ExpenseEntity
import com.zzz.feature_trip.R

/**
 * Entity for UI
 */
data class ExpenseEntityUI(
    val id : Long,
    val amount : Int = 0,
    val currencySymbol : String = "$",
    val title : String? = null,
    val splitInto : Int? = null,
    val expenseType : String = "other",
    @DrawableRes
    val icon : Int,
    val iconBackground : Int,
    val timestamp : Long
)

fun ExpenseEntity.toUIEntity() : ExpenseEntityUI{
    return ExpenseEntityUI(
        id = id,
        amount = amount,
        currencySymbol = currencySymbol,
        title = title,
        splitInto = splitInto,
        expenseType = expenseType,
        icon = expenseType.toDrawableRes(),
        iconBackground = getIconBackground(expenseType),
        timestamp = timestamp
    )
}

/**
 * Maps short titles to drawables
 */
private val iconMap = mapOf(
    "ff" to R.drawable.fastfood,
    "fl" to R.drawable.flight,
    "tr" to R.drawable.taxi,
    "sh" to R.drawable.shopping_bag,
    "ht" to R.drawable.hotel,
    "med" to R.drawable.hospital,
    "rt" to R.drawable.cycle,
)
/**
 * Maps short titles to colors
 */
private val iconBackgroundMap = mapOf(
    "ff" to Color(0xFFCEBE36).toArgb(),
    "fl" to Color(0xFFCE382D).toArgb(),
    "tr" to Color(0xFFD79637).toArgb(),
    "sh" to Color(0xFF2EA4D9).toArgb(),
    "ht" to Color(0xFF348CD2).toArgb(),
    "med" to Color(0xFF29BD2F).toArgb(),
    "rt" to Color(0xFF3639CE).toArgb(),
)

private fun String.toDrawableRes():Int{
    return iconMap[this] ?: R.drawable.dollar
}
private fun getIconBackground(shortTitle : String):Int{
    return iconBackgroundMap[shortTitle] ?: Color(0xFFB08C2C).toArgb()
}
