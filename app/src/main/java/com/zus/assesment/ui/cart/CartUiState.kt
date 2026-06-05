package com.zus.assesment.ui.cart

import com.zus.assesment.domain.model.CartLine

enum class CheckoutStatus {
    Idle,
    Loading,
    Success,
    Error,
}

data class CartUiState(
    val lines: List<CartLine> = emptyList(),
    val checkoutStatus: CheckoutStatus = CheckoutStatus.Idle,
    val checkoutMessage: String? = null,
) {
    val total: Double
        get() = lines.sumOf { it.lineTotal }

    val isEmpty: Boolean
        get() = lines.isEmpty()
}
