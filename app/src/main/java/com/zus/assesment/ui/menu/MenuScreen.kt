package com.zus.assesment.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zus.assesment.ui.components.SloganBannerShape
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.menu.components.CategorySidebar
import com.zus.assesment.ui.menu.components.FilterChips
import com.zus.assesment.ui.menu.components.MealCard
import com.zus.assesment.ui.theme.ZusBackground
import com.zus.assesment.ui.theme.ZusBlack
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusBlueDark
import com.zus.assesment.ui.theme.ZusGray
import com.zus.assesment.ui.theme.ZusWhite

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZusBackground),
    ) {
        MenuHeader(cartItemCount = uiState.cartItemCount)

        Row(modifier = Modifier.weight(1f)) {
            CategorySidebar(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = viewModel::selectCategory,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(ZusBackground)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                FilterChips(
                    selected = uiState.filter,
                    onSelected = viewModel::setFilter,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = ZusBlue)
                        }
                    }

                    uiState.errorMessage != null -> {
                        ErrorState(
                            message = uiState.errorMessage.orEmpty(),
                            onRetry = viewModel::retry,
                        )
                    }

                    uiState.visibleItems.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No items in this filter",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ZusGray,
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(
                                items = uiState.visibleItems,
                                key = { it.id },
                            ) { item ->
                                MealCard(
                                    item = item,
                                    quantity = uiState.cartQuantities[item.id] ?: 0,
                                    onAdd = { viewModel.addToCart(item.id) },
                                    onDecrement = { viewModel.decrementFromCart(item.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuHeader(cartItemCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ZusBlueDark),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(SloganBannerShape(slant = 18.dp))
                .background(ZusWhite)
                .border(2.dp, ZusBlack, SloganBannerShape(slant = 18.dp))
                .padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Text(
                text = "ANYTIME, ZUS TIME.",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp,
                ),
                color = ZusBlack,
            )
        }

        Button(
            onClick = { },
            modifier = Modifier.padding(start = 10.dp),
            shape = SlantedShape(slant = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ZusBlue,
                contentColor = ZusWhite,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                disabledElevation = 0.dp,
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        ) {
            Text(
                text = if (cartItemCount > 0) "CART ($cartItemCount)" else "CART",
                style = MaterialTheme.typography.labelMedium,
            )
        }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = ZusBlueDark,
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = ZusBlue,
                contentColor = ZusWhite,
            ),
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("RETRY")
        }
    }
}
