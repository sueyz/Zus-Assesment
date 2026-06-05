package com.zus.assesment.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zus.assesment.data.repository.CartRepository
import com.zus.assesment.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val menuRepository: MenuRepository = MenuRepository(),
    private val cartRepository: CartRepository = CartRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState(isLoading = true))
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.quantities.collect { quantities ->
                _uiState.update { it.copy(cartQuantities = quantities) }
            }
        }
        loadCategories()
    }

    fun selectCategory(categoryId: String) {
        if (categoryId == _uiState.value.selectedCategoryId) return
        val category = _uiState.value.categories.find { it.id == categoryId } ?: return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedCategoryId = categoryId,
                    isLoading = true,
                    errorMessage = null,
                )
            }
            runCatching { menuRepository.getItems(category) }
                .onSuccess { items ->
                    _uiState.update {
                        it.copy(items = items, isLoading = false, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load menu items",
                        )
                    }
                }
        }
    }

    fun setFilter(filter: ItemFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun addToCart(itemId: String) {
        val item = _uiState.value.items.find { it.id == itemId } ?: return
        if (!item.isAvailable) return
        cartRepository.add(itemId)
    }

    fun decrementFromCart(itemId: String) {
        cartRepository.decrement(itemId)
    }

    fun retry() {
        val state = _uiState.value
        if (state.categories.isEmpty()) {
            loadCategories()
        } else {
            state.selectedCategoryId?.let { selectCategory(it) }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { menuRepository.getCategories() }
                .onSuccess { categories ->
                    if (categories.isEmpty()) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "No categories found")
                        }
                        return@launch
                    }
                    _uiState.update {
                        it.copy(categories = categories, isLoading = false, errorMessage = null)
                    }
                    selectCategory(categories.first().id)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load categories",
                        )
                    }
                }
        }
    }
}
