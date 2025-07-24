package com.zzz.feature_settings.presentation.viewmodel

internal sealed class SettingsAction {

    data class SetTicketContainer(val isTicket : Boolean) : SettingsAction()
    data class SetChecklistContainer(val isTrapezium : Boolean) : SettingsAction()

}