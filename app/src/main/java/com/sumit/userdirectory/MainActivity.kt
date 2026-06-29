package com.sumit.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sumit.userdirectory.navigation.AppNavHost
import com.sumit.userdirectory.theme.UserDirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserDirectoryTheme {
                AppNavHost()
            }
        }
    }
}
