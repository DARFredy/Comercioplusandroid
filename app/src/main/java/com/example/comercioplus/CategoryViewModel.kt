package com.example.comercioplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Category(val id: String, val name: String, val slug: String)

class CategoryViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        viewModelScope.launch {
            _categories.value = listOf(
                Category("1", "Calcomanía", "calcomania"),
                Category("2", "Accesorios", "accesorios"),
                Category("3", "Repuestos", "repuestos"),
                Category("4", "Llantas", "llantas"),
                Category("5", "Frenos", "frenos")
            )
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val newId = (_categories.value.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
            val newCategory = Category(
                id = newId.toString(),
                name = name,
                slug = name.lowercase().replace(" ", "-")
            )
            _categories.value = _categories.value + newCategory
        }
    }

    fun updateCategory(id: String, newName: String) {
        viewModelScope.launch {
            _categories.update { currentCategories ->
                currentCategories.map {
                    if (it.id == id) {
                        it.copy(name = newName, slug = newName.lowercase().replace(" ", "-"))
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            _categories.update { currentCategories ->
                currentCategories.filter { it.id != id }
            }
        }
    }
}
