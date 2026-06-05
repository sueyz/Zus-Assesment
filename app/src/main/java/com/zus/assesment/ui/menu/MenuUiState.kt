package com.zus.assesment.ui.menu

import com.zus.assesment.domain.model.MenuCategory
import com.zus.assesment.domain.model.MenuItem

enum class ItemFilter { ALL, AVAILABLE, SOLD_OUT }

data class MenuUiState(
    val categories: List<MenuCategory> = emptyList(),
    val selectedCategoryId: String? = null,
    val items: List<MenuItem> = emptyList(),
    val cartQuantities: Map<String, Int> = emptyMap(),
    val filter: ItemFilter = ItemFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val selectedCategory: MenuCategory?
        get() = categories.find { it.id == selectedCategoryId }

    val visibleItems: List<MenuItem>
        get() = when (filter) {
            ItemFilter.ALL -> items
            ItemFilter.AVAILABLE -> items.filter { it.isAvailable }
            ItemFilter.SOLD_OUT -> items.filter { !it.isAvailable }
        }

    val cartItemCount: Int
        get() = cartQuantities.values.sum()
}
