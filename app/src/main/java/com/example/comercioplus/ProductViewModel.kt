package com.example.comercioplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comercioplus.model.Product
import com.example.comercioplus.model.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository.getInstance()

    // Expone el flujo de productos directamente desde el repositorio
    val products: StateFlow<List<Product>> = repository.products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    fun findProductById(productId: String) {
        viewModelScope.launch {
            _selectedProduct.value = repository.getProductById(productId)
        }
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun addProduct(name: String, description: String, price: Double, imageUrl: String, category: String) {
        viewModelScope.launch {
            val newProduct = Product(
                name = name,
                description = description,
                price = price,
                imageUrl = imageUrl,
                category = category
            )
            repository.addProduct(newProduct)
        }
    }

    fun updateProduct(id: String, name: String, description: String, price: Double, imageUrl: String, category: String) {
        viewModelScope.launch {
            val updatedProduct = Product(id, name, description, price, imageUrl, category)
            repository.updateProduct(updatedProduct)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }
}
