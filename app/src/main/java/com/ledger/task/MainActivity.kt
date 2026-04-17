package com.ledger.task

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ledger.task.ui.navigation.AppNavigation
import com.ledger.task.ui.theme.TaskLedgerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskLedgerTheme {
                AppNavigation()
            }
        }
    }
}
