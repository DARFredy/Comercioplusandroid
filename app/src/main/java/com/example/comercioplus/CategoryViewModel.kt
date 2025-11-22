package com.example.comercioplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comercioplus.model.Category
import com.example.comercioplus.model.CategoryRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val repository = CategoryRepository.getInstance()

    // Expone el flujo de categorías directamente desde el repositorio
    val categories: StateFlow<List<Category>> = repository.categories

    fun addCategory(name: String) {
        viewModelScope.launch {
            repository.addCategory(name)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
        }
    }
}
