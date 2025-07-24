package com.zzz.feature_trip.di

import com.zzz.feature_trip.create.presentation.viewmodel.CreateViewModel
import com.zzz.feature_trip.create.presentation.viewmodel.DayViewModel
import com.zzz.feature_trip.home.presentation.HomeViewModel
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pager.expense_tracker.viewmodel.ExpenseTrackerViewModel
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createModule = module {
    viewModel {
        CreateViewModel(
            tripSource = get() ,
            daySource = get() ,
            docSource = get() ,
            expenseNoteSource = get() ,
            checklistSource = get()
        )
    }
    viewModel {
        DayViewModel(
            daySource = get() ,
            todoSource = get()
        )
    }
    viewModel {
        UpdateTripViewModel(
            tripSource = get() ,
            daySource = get() ,
            todoSource = get() ,
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
            tripSource = get() ,
            daySource = get() ,
            todoSource = get() ,
            docSource = get() ,
            notesSource = get() ,
            checklistSource = get() ,
            expenseSource = get() ,
            context = androidContext()
        )
    }
    viewModel() {
        ExpenseTrackerViewModel(
            dataSource = get()
        )
    }

    viewModel {
        ShareTripViewModel(
            tripSource = get() ,
            daySource = get() ,
            todoSource = get() ,
            noteSource = get(),
            context = androidContext()
        )
    }
}
