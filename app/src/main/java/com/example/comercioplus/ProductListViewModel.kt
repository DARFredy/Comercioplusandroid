package com.example.comercioplus

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comercioplus.model.Product
import com.example.comercioplus.model.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Estado de la UI para la pantalla de lista de productos (la tienda)
data class ProductListUiState(
    val storeName: String = "Mi Tienda Online",
    val logoUri: Uri? = null,
    val coverUri: Uri? = null,
    val products: List<Product> = emptyList(),
    val productCount: Int = 0
)

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)
    private val productRepository = ProductRepository.getInstance()

    // Combina los flujos de datos de los repositorios en un único estado para la UI
    val uiState: StateFlow<ProductListUiState> = combine(
        settingsRepository.generalSettings,
        settingsRepository.appearanceSettings,
        productRepository.products
    ) { general, appearance, products ->
        ProductListUiState(
            storeName = general.storeName,
            logoUri = appearance.logoUri,
            coverUri = appearance.coverUri,
            products = products,
            productCount = products.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductListUiState() // Estado inicial mientras se cargan los datos
    )
}
