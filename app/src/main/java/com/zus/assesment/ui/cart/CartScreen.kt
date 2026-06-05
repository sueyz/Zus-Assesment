package com.zus.assesment.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zus.assesment.ui.cart.components.CartItemCard
import com.zus.assesment.ui.components.SlantedShape
import com.zus.assesment.ui.theme.ZusBackground
import com.zus.assesment.ui.theme.ZusBlack
import com.zus.assesment.ui.theme.ZusBlue
import com.zus.assesment.ui.theme.ZusBlueDark
import com.zus.assesment.ui.theme.ZusGray
import com.zus.assesment.ui.theme.ZusWhite

@Composable
fun CartScreen(
    onBack: () -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPlacingOrder = uiState.checkoutStatus == CheckoutStatus.Loading

    DisposableEffect(Unit) {
        onDispose { viewModel.dismissCheckoutMessage() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZusBackground),
    ) {
        CartHeader(onBack = onBack)

        when {
            uiState.checkoutStatus == CheckoutStatus.Success -> {
                CartCenteredState(
                    title = "Order placed successfully!",
                    subtitle = "Thank you for your order.",
                    buttonText = "BACK TO MENU",
                    onButtonClick = onBack,
                )
            }

            uiState.isEmpty -> {
                CartCenteredState(
                    title = "Your cart is empty",
                    subtitle = "Add items from the menu to get started.",
                    buttonText = "BROWSE MENU",
                    onButtonClick = onBack,
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(uiState.lines, key = { it.item.id }) { line ->
                        CartItemCard(
                            line = line,
                            onIncrement = { viewModel.increment(line.item.id) },
                            onDecrement = { viewModel.decrement(line.item.id) },
                        )
                    }
                }

                CheckoutFooter(
                    total = uiState.total,
                    isPlacingOrder = isPlacingOrder,
                    checkoutStatus = uiState.checkoutStatus,
                    checkoutMessage = uiState.checkoutMessage,
                    onPlaceOrder = viewModel::placeOrder,
                    onDismissMessage = viewModel::dismissCheckoutMessage,
                )
            }
        }
    }
}

@Composable
private fun CartHeader(onBack: () -> Unit) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                onClick = onBack,
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
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    text = "BACK",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Text(
                text = "YOUR CART",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                ),
                color = ZusWhite,
            )
        }
    }
}

@Composable
private fun CartCenteredState(
    title: String,
    subtitle: String,
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = ZusBlueDark,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = ZusGray,
            modifier = Modifier.padding(top = 8.dp),
        )
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ZusBlue,
                contentColor = ZusWhite,
            ),
            modifier = Modifier.padding(top = 20.dp),
        ) {
            Text(buttonText)
        }
    }
}

@Composable
private fun CheckoutFooter(
    total: Double,
    isPlacingOrder: Boolean,
    checkoutStatus: CheckoutStatus,
    checkoutMessage: String?,
    onPlaceOrder: () -> Unit,
    onDismissMessage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ZusWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 24.dp),
    ) {
        if (checkoutStatus == CheckoutStatus.Error && checkoutMessage != null) {
            Text(
                text = checkoutMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = ZusBlueDark,
                modifier = Modifier.padding(bottom = 10.dp),
            )
            TextButton(onClick = onDismissMessage) {
                Text("DISMISS", color = ZusBlue)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "TOTAL",
                style = MaterialTheme.typography.labelMedium,
                color = ZusBlack,
            )
            Text(
                text = "RM %.2f".format(total),
                style = MaterialTheme.typography.titleMedium,
                color = ZusBlack,
            )
        }

        Button(
            onClick = onPlaceOrder,
            enabled = !isPlacingOrder && checkoutStatus != CheckoutStatus.Success,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
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
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (isPlacingOrder) {
                    CircularProgressIndicator(
                        color = ZusWhite,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(
                        text = "PLACE ORDER",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}
