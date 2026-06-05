package com.zus.assesment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zus.assesment.ui.menu.MenuScreen
import com.zus.assesment.ui.theme.ZusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZusTheme {
                MenuScreen()
            }
        }
    }
}
