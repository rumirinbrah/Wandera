package com.zzz.feature_trip.di

import com.zzz.feature_trip.create.presentation.CreateViewModel
import com.zzz.feature_trip.home.presentation.HomeViewModel
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createModule = module {
    viewModel {
        CreateViewModel(
            tripSource = get(),
            daySource = get(),
            todoSource = get(),
            docSource = get()
        )
    }
    viewModel {
        UpdateTripViewModel(
            tripSource = get(),
            daySource = get(),
            todoSource = get(),
            docSource = get()
        )
    }
    viewModel {
        HomeViewModel(
            tripSource = get()
        )
    }
    viewModel {
        OverviewViewModel(
            tripSource = get(),
            daySource =get(),
            todoSource = get(),
            docSource = get(),
            context = androidContext()
        )
    }
}