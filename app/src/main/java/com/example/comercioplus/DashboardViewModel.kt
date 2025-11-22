package com.example.comercioplus

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comercioplus.model.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Estado de la interfaz de usuario para el Dashboard
data class DashboardUiState(
    val storeName: String = "Mi Tienda Online",
    val productCount: Int = 0,
    val isStoreVisible: Boolean = true,
    val logoUri: Uri? = null // Añadido para mostrar el logo
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)
    private val productRepository = ProductRepository.getInstance()

    // Combina los flujos de datos de los repositorios en un único estado para la UI
    val uiState: StateFlow<DashboardUiState> = combine(
        settingsRepository.generalSettings,
        settingsRepository.appearanceSettings, // Añadido para obtener el logo
        productRepository.products
    ) { generalSettings, appearanceSettings, products ->
        DashboardUiState(
            storeName = generalSettings.storeName,
            productCount = products.size,
            isStoreVisible = generalSettings.isStoreVisible,
            logoUri = appearanceSettings.logoUri // Pasar el logo al estado
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState() // Valor inicial mientras se cargan los datos
    )
}
