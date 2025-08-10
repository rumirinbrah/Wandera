package com.zzz.feature_trip.di

import com.zzz.feature_trip.create.presentation.viewmodel.CreateViewModel
import com.zzz.feature_trip.create.presentation.viewmodel.DayViewModel
import com.zzz.feature_trip.day_details.viewmodel.DayDetailsViewModel
import com.zzz.feature_trip.home.presentation.HomeViewModel
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.ExpenseTrackerViewModel
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.currency.CurrencyViewModel
import com.zzz.feature_trip.overview.presentation.viewmodel.OverviewViewModel
import com.zzz.feature_trip.recents.presentation.viewmodel.RecentsViewModel
import com.zzz.feature_trip.share.presentation.viewmodel.ShareTripViewModel
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createModule = module {

    //-------- CREATE ----------
    viewModel {
        CreateViewModel(
            tripSource = get() ,
            daySource = get() ,
            docSource = get() ,
            expenseNoteSource = get() ,
            checklistSource = get()
        )
    }
    //-------- DAY ----------
    viewModel {
        DayViewModel(
            daySource = get() ,
            todoSource = get()
        )
    }
    //-------- UPDATE ----------
    viewModel {
        UpdateTripViewModel(
            tripSource = get() ,
            daySource = get() ,
            todoSource = get() ,
            docSource = get()
        )
    }
    //-------- HOME ----------
    viewModel {
        HomeViewModel(
            tripSource = get(),
            context = androidContext()
        )
    }
    //-------- RECENTS ----------
    viewModel{
        RecentsViewModel(
            tripSource = get()
        )
    }
    //-------- OverivewViewModel ----------
    viewModel {
        OverviewViewModel(
            tripSource = get() ,
            daySource = get() ,
            docSource = get() ,
            notesSource = get() ,
            checklistSource = get() ,
            expenseSource = get() ,
            context = androidContext()
        )
    }
    //-------- DayViewModel ----------

    viewModel {
        DayDetailsViewModel(
            daySource = get(),
            todoSource = get()
        )
    }


    //-------- ExpenseTrackerViewModel ----------
    viewModel {
        ExpenseTrackerViewModel(
            dataSource = get(),
            context = androidContext()
        )
    }
    //-------- CurrencyViewModel ----------
    viewModel {
        CurrencyViewModel()
    }


    //-------- ShareTripViewModel ----------
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
