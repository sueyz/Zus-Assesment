package com.zus.assesment.data.repository

import com.zus.assesment.domain.model.CartLine
import com.zus.assesment.domain.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class CartSnapshot(
    val lines: List<CartLine> = emptyList(),
)

@Singleton
class CartRepository
    @Inject
    constructor() {
        private val _snapshot = MutableStateFlow(CartSnapshot())
        val snapshot: StateFlow<CartSnapshot> = _snapshot.asStateFlow()

        fun add(item: MenuItem) {
            if (!item.isAvailable) return
            _snapshot.update { current ->
                val index = current.lines.indexOfFirst { it.item.id == item.id }
                val updatedLines =
                    if (index >= 0) {
                        current.lines.toMutableList().apply {
                            val line = this[index]
                            this[index] = line.copy(quantity = line.quantity + 1)
                        }
                    } else {
                        current.lines + CartLine(item = item, quantity = 1)
                    }
                current.copy(lines = updatedLines)
            }
        }

        fun decrement(itemId: String) {
            _snapshot.update { current ->
                val index = current.lines.indexOfFirst { it.item.id == itemId }
                if (index < 0) return@update current
                val line = current.lines[index]
                val updatedLines =
                    if (line.quantity <= 1) {
                        current.lines.filterNot { it.item.id == itemId }
                    } else {
                        current.lines.toMutableList().apply {
                            this[index] = line.copy(quantity = line.quantity - 1)
                        }
                    }
                current.copy(lines = updatedLines)
            }
        }

        fun clear() {
            _snapshot.value = CartSnapshot()
        }
    }
