package com.zzz.wandera.di

import com.zzz.core.presentation.permission.PermissionViewModel
import com.zzz.wandera.data.local.ThemePreferences
import com.zzz.wandera.ui.ThemeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        ThemePreferences(androidContext())
    }
    viewModel {
        ThemeViewModel(
            themePreferences = get()
        )
    }
    viewModel {
        PermissionViewModel()
    }
}