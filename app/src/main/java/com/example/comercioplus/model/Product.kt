package com.example.comercioplus.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String // URL de la imagen del producto
)
