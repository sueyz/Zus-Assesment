package com.zus.assesment.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CartRepository {
    private val _quantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    val quantities: StateFlow<Map<String, Int>> = _quantities.asStateFlow()

    fun add(itemId: String) {
        _quantities.update { current ->
            current.toMutableMap().apply {
                this[itemId] = (this[itemId] ?: 0) + 1
            }
        }
    }

    fun decrement(itemId: String) {
        _quantities.update { current ->
            val quantity = current[itemId] ?: return@update current
            current.toMutableMap().apply {
                if (quantity <= 1) remove(itemId) else this[itemId] = quantity - 1
            }
        }
    }

    fun totalItems(): Int = _quantities.value.values.sum()
}
