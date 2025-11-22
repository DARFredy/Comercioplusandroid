package com.example.comercioplus.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Modelo de datos para una Categoría
data class Category(
    val id: String = "",
    val name: String = "",
    val slug: String = "",
    val productCount: Int = 0 // Se podría calcular en un futuro
)

// Repositorio para gestionar las categorías
class CategoryRepository {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        // Datos de ejemplo iniciales
        _categories.value = listOf(
            Category(id = "1", name = "Accesorios", slug = "accesorios", productCount = 2),
            Category(id = "2", name = "Ropa", slug = "ropa", productCount = 1),
            Category(id = "3", name = "Repuestos", slug = "repuestos", productCount = 2),
            Category(id = "4", name = "Calcomanías", slug = "calcomanias", productCount = 0)
        )
    }

    fun addCategory(name: String) {
        val newId = (_categories.value.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
        val newCategory = Category(
            id = newId.toString(),
            name = name,
            slug = name.lowercase().replace(" ", "-"),
            productCount = 0
        )
        _categories.value = _categories.value + newCategory
    }

    fun updateCategory(category: Category) {
        _categories.value = _categories.value.map {
            if (it.id == category.id) category else it
        }
    }

    fun deleteCategory(categoryId: String) {
        _categories.value = _categories.value.filter { it.id != categoryId }
    }

    // Singleton para acceder al repositorio
    companion object {
        @Volatile private var instance: CategoryRepository? = null

        fun getInstance(): CategoryRepository {
            return instance ?: synchronized(this) {
                instance ?: CategoryRepository().also { instance = it }
            }
        }
    }
}
