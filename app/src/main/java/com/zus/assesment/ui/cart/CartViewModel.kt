package com.zus.assesment.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zus.assesment.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CartViewModel
    @Inject
    constructor(
        private val cartRepository: CartRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CartUiState())
        val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                cartRepository.snapshot.collect { snapshot ->
                    _uiState.update { current ->
                        val lines = snapshot.lines
                        if (lines.isEmpty() && current.checkoutStatus == CheckoutStatus.Error) {
                            current.copy(
                                lines = lines,
                                checkoutStatus = CheckoutStatus.Idle,
                                checkoutMessage = null,
                            )
                        } else {
                            current.copy(lines = lines)
                        }
                    }
                }
            }
        }

        fun increment(itemId: String) {
            val line = _uiState.value.lines.find { it.item.id == itemId } ?: return
            cartRepository.add(line.item)
        }

        fun decrement(itemId: String) {
            cartRepository.decrement(itemId)
        }

        fun placeOrder() {
            val state = _uiState.value
            if (state.isEmpty || state.checkoutStatus == CheckoutStatus.Loading) return
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        checkoutStatus = CheckoutStatus.Loading,
                        checkoutMessage = null,
                    )
                }
                delay(1500.milliseconds)
                val shouldFail = _uiState.value.lines.size % 3 == 0
                if (shouldFail) {
                    _uiState.update {
                        it.copy(
                            checkoutStatus = CheckoutStatus.Error,
                            checkoutMessage = "Checkout failed. Please try again.",
                        )
                    }
                } else {
                    cartRepository.clear()
                    _uiState.update {
                        it.copy(
                            checkoutStatus = CheckoutStatus.Success,
                            checkoutMessage = "Order placed successfully!",
                        )
                    }
                }
            }
        }

        fun dismissCheckoutMessage() {
            _uiState.update {
                it.copy(
                    checkoutStatus = CheckoutStatus.Idle,
                    checkoutMessage = null,
                )
            }
        }
    }
