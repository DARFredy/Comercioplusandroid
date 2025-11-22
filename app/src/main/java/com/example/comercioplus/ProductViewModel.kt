package com.example.comercioplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comercioplus.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    init {
        // Datos de ejemplo iniciales
        viewModelScope.launch {
            _products.value = listOf(
                Product("1", "Casco de moto", "Casco de seguridad para motociclistas", 99.99, "https://picsum.photos/id/10/400"),
                Product("2", "Guantes de cuero", "Guantes de cuero para motociclistas", 29.99, "https://picsum.photos/id/11/400"),
                Product("3", "Chaqueta de moto", "Chaqueta de protección con armadura", 199.99, "https://picsum.photos/id/12/400")
            )
        }
    }

    fun findProductById(productId: String) {
        viewModelScope.launch {
            _selectedProduct.value = _products.value.find { it.id == productId }
        }
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun addProduct(name: String, description: String, price: Double, imageUrl: String) {
        viewModelScope.launch {
            val newProduct = Product(
                id = (_products.value.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0).plus(1).toString(),
                name = name,
                description = description,
                price = price,
                imageUrl = imageUrl
            )
            _products.value = _products.value + newProduct
        }
    }

    fun updateProduct(id: String, name: String, description: String, price: Double, imageUrl: String) {
        viewModelScope.launch {
            val updatedList = _products.value.map {
                if (it.id == id) {
                    it.copy(name = name, description = description, price = price, imageUrl = imageUrl)
                } else {
                    it
                }
            }
            _products.value = updatedList
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _products.value = _products.value.filter { it.id != productId }
        }
    }
}
