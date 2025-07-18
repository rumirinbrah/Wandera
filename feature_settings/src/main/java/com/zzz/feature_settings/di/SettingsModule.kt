package com.zzz.feature_settings.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.zzz.feature_settings.presentation.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel(
            context = androidContext()
        )
    }
}