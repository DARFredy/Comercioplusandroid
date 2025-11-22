package com.example.comercioplus.model

// Modelo de datos único para un Producto
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "" // Aseguramos que la categoría siempre esté presente
)
