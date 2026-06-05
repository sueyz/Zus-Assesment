package com.zus.assesment.domain.model

data class CartLine(
    val item: MenuItem,
    val quantity: Int,
) {
    val lineTotal: Double
        get() = item.price * quantity
}
