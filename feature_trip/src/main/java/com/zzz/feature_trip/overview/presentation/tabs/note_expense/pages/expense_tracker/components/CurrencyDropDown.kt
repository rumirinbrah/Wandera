package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zzz.feature_trip.overview.domain.UICurrency
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.currency.CurrencyViewModel

@Composable
fun CurrencySelectorPage(
    modifier: Modifier = Modifier ,
    onClick: (symbol: String, code: String) -> Unit
) {
    val currencyViewModel = viewModel<CurrencyViewModel>()
    val state by currencyViewModel.state.collectAsStateWithLifecycle()


    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .padding(16.dp)
    ) {
        LazyColumn(
            Modifier.fillMaxWidth() ,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.currencies){currency->
                CurrencyItem(
                    currency = currency,
                    onClick = {symbol: String,code : String ->
                        //currencyViewModel.setDefaultCurrency(code)
                        onClick(symbol,code)
                    }
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    modifier: Modifier = Modifier ,
    currency: UICurrency ,
    onClick: (symbol: String,code :String) -> Unit ,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.SpaceBetween ,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                enabled = enabled
            ) {
                onClick(currency.symbol,currency.code)
            }
            .padding(16.dp)
    ) {
        Text(
            text = currency.symbol ,
            fontWeight = FontWeight.Medium ,
            fontSize = 25.sp,
            //modifier = Modifier.width(60.dp)
        )
        Text(
            text = currency.code ,
            fontSize = 25.sp,
        )
        Text(
            text = currency.emoji,
            fontSize = 25.sp,
        )
    }
}
