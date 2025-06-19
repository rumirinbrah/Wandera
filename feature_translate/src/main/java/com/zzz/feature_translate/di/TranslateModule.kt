package com.zzz.feature_translate.di

import com.zzz.feature_translate.manager.TranslationManager
import com.zzz.feature_translate.presentation.viewmodel.TranslationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val translateModule = module {
    viewModel {
        TranslationViewModel(
            dataSource = get(),
            translationManager = TranslationManager()
        )
    }
}