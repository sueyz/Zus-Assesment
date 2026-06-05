package com.zus.assesment.domain.model

data class MenuItem(
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val isAvailable: Boolean,
    val tags: List<String>,
)
