package com.ledger.task

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.lifecycleScope
import com.ledger.task.ui.navigation.AppNavigation
import com.ledger.task.ui.theme.LocalThemeMode
import com.ledger.task.ui.theme.ThemeManager
import com.ledger.task.ui.theme.TaskLedgerTheme
import com.ledger.task.update.VersionChecker
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化全局主题状态
        val themeModeState = ThemeManager.initThemeMode(this)

        // 自动检查更新（后台执行，不阻塞 UI）
        autoCheckUpdate()

        setContent {
            // 提供全局主题状态
            CompositionLocalProvider(LocalThemeMode provides themeModeState) {
                TaskLedgerTheme {
                    AppNavigation()
                }
            }
        }
    }

    private fun autoCheckUpdate() {
        val versionChecker = VersionChecker(this)
        if (versionChecker.shouldAutoCheck()) {
            lifecycleScope.launch {
                versionChecker.checkUpdate()
                versionChecker.recordCheckTime()
            }
        }
    }
}
