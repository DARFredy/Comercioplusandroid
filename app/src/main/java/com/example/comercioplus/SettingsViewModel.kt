package com.example.comercioplus

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GeneralSettingsState(
    val storeName: String = "Mi Tienda Online",
    val phone: String = "",
    val whatsapp: String = "",
    val reportEmail: String = "",
    val address: String = "",
    val city: String = "",
    val isStoreVisible: Boolean = true
)

// Actualizado para incluir la preferencia del modo oscuro
data class AppearanceSettingsState(
    val logoUri: Uri? = null,
    val coverUri: Uri? = null,
    val isDarkMode: Boolean = true // Por defecto, el modo oscuro está activado
)

data class PaymentsSettingsState(val instructions: String = "")
data class ShippingSettingsState(val radius: String = "", val baseCost: String = "")
data class TaxesSettingsState(val percentage: String = "", val pricesIncludeTax: Boolean = false)
data class NotificationsSettingsState(val email: String = "")

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    private val _generalState = MutableStateFlow(GeneralSettingsState())
    val generalState = _generalState.asStateFlow()

    private val _appearanceState = MutableStateFlow(AppearanceSettingsState())
    val appearanceState = _appearanceState.asStateFlow()

    private val _paymentsState = MutableStateFlow(PaymentsSettingsState())
    val paymentsState = _paymentsState.asStateFlow()

    private val _shippingState = MutableStateFlow(ShippingSettingsState())
    val shippingState = _shippingState.asStateFlow()

    private val _taxesState = MutableStateFlow(TaxesSettingsState())
    val taxesState = _taxesState.asStateFlow()

    private val _notificationsState = MutableStateFlow(NotificationsSettingsState())
    val notificationsState = _notificationsState.asStateFlow()

    init {
        repository.generalSettings.onEach { _generalState.value = it }.launchIn(viewModelScope)
        repository.appearanceSettings.onEach { _appearanceState.value = it }.launchIn(viewModelScope)
        repository.paymentsSettings.onEach { _paymentsState.value = it }.launchIn(viewModelScope)
        repository.shippingSettings.onEach { _shippingState.value = it }.launchIn(viewModelScope)
        repository.taxesSettings.onEach { _taxesState.value = it }.launchIn(viewModelScope)
        repository.notificationsSettings.onEach { _notificationsState.value = it }.launchIn(viewModelScope)
    }

    // General
    fun onStoreNameChange(value: String) { _generalState.update { it.copy(storeName = value) } }
    fun onPhoneChange(value: String) { _generalState.update { it.copy(phone = value) } }
    fun onWhatsappChange(value: String) { _generalState.update { it.copy(whatsapp = value) } }
    fun onReportEmailChange(value: String) { _generalState.update { it.copy(reportEmail = value) } }
    fun onAddressChange(value: String) { _generalState.update { it.copy(address = value) } }
    fun onCityChange(value: String) { _generalState.update { it.copy(city = value) } }
    fun onStoreVisibilityChange(isVisible: Boolean) { _generalState.update { it.copy(isStoreVisible = isVisible) } }
    fun saveGeneralSettings() = viewModelScope.launch {
        repository.saveGeneralSettings(generalState.value)
    }

    // Apariencia
    fun onLogoUriChange(uri: Uri?) { _appearanceState.update { it.copy(logoUri = uri) } }
    fun onCoverUriChange(uri: Uri?) { _appearanceState.update { it.copy(coverUri = uri) } }
    fun onDarkModeChange(isDarkMode: Boolean) { _appearanceState.update { it.copy(isDarkMode = isDarkMode) } } // Nueva función
    fun saveAppearanceSettings() = viewModelScope.launch {
        repository.saveAppearanceSettings(appearanceState.value)
    }

    // Pagos
    fun onInstructionsChange(value: String) { _paymentsState.update { it.copy(instructions = value) } }
    fun savePaymentsSettings() = viewModelScope.launch {
        repository.savePaymentsSettings(paymentsState.value)
    }

    // Envíos
    fun onRadiusChange(value: String) { _shippingState.update { it.copy(radius = value) } }
    fun onBaseCostChange(value: String) { _shippingState.update { it.copy(baseCost = value) } }
    fun saveShippingSettings() = viewModelScope.launch {
        repository.saveShippingSettings(shippingState.value)
    }

    // Impuestos
    fun onPercentageChange(value: String) { _taxesState.update { it.copy(percentage = value) } }
    fun onPricesIncludeTaxChange(include: Boolean) { _taxesState.update { it.copy(pricesIncludeTax = include) } }
    fun saveTaxesSettings() = viewModelScope.launch {
        repository.saveTaxesSettings(taxesState.value)
    }

    // Notificaciones
    fun onNotificationsEmailChange(value: String) { _notificationsState.update { it.copy(email = value) } }
    fun saveNotificationsSettings() = viewModelScope.launch {
        repository.saveNotificationsSettings(notificationsState.value)
    }
}