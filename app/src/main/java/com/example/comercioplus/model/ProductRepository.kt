package com.example.comercioplus.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Repositorio para gestionar los productos. Ahora usa el modelo Product de su propio archivo.
class ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        // Datos de ejemplo iniciales
        _products.value = listOf(
            Product("1", "Casco de moto", "Casco de seguridad para motociclistas", 99.99, "https://picsum.photos/id/10/400", "Accesorios"),
            Product("2", "Guantes de cuero", "Guantes de cuero para motociclistas", 29.99, "https://picsum.photos/id/11/400", "Accesorios"),
            Product("3", "Chaqueta de moto", "Chaqueta de protección con armadura", 199.99, "https://picsum.photos/id/12/400", "Ropa"),
            Product("4", "Llantas Michelin", "Juego de llantas para moto deportiva", 300.00, "https://picsum.photos/id/13/400", "Repuestos"),
            Product("5", "Filtro de aire", "Filtro de aire de alto rendimiento", 45.50, "https://picsum.photos/id/14/400", "Repuestos")
        )
    }

    fun getProductById(productId: String): Product? {
        return _products.value.find { it.id == productId }
    }

    fun addProduct(product: Product) {
        val newId = (_products.value.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
        _products.value = _products.value + product.copy(id = newId.toString())
    }

    fun updateProduct(product: Product) {
        _products.value = _products.value.map {
            if (it.id == product.id) product else it
        }
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filter { it.id != productId }
    }

    // Singleton para acceder al repositorio desde cualquier lugar de la app
    companion object {
        @Volatile private var instance: ProductRepository? = null

        fun getInstance(): ProductRepository {
            return instance ?: synchronized(this) {
                instance ?: ProductRepository().also { instance = it }
            }
        }
    }
}