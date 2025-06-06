package com.zzz.feature_trip.di

import com.zzz.feature_trip.create.presentation.CreateViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createModule = module {
    viewModel {
        CreateViewModel(
            tripSource = get()
        )
    }
}