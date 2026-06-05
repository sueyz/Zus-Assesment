package com.zus.assesment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zus.assesment.ui.cart.CartScreen
import com.zus.assesment.ui.menu.MenuScreen

@Composable
fun ZusNavHost() {
    val backStack = rememberNavBackStack(MenuRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<MenuRoute> {
                MenuScreen(
                    onCartClick = dropUnlessResumed {
                        backStack.add(CartRoute)
                    },
                )
            }
            entry<CartRoute> {
                CartScreen(
                    onBack = dropUnlessResumed {
                        backStack.removeLastOrNull()
                    },
                )
            }
        },
    )
}
