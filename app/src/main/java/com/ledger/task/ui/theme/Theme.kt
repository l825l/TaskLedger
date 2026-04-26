package com.ledger.task.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

/**
 * 主题模式
 */
enum class ThemeMode(val displayName: String) {
    SYSTEM("跟随系统"),
    LIGHT("亮色模式"),
    DARK("深色模式")
}

/**
 * 全局主题模式状态
 */
val LocalThemeMode = staticCompositionLocalOf<MutableState<ThemeMode>> {
    mutableStateOf(ThemeMode.SYSTEM)
}

/**
 * 主题设置管理器
 */
object ThemeManager {
    private const val PREFS_NAME = "theme_settings"
    private const val KEY_THEME_MODE = "theme_mode"

    // 内存中的主题状态
    private var _currentThemeMode: MutableState<ThemeMode>? = null

    /**
     * 获取保存的主题模式
     */
    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val modeIndex = prefs.getInt(KEY_THEME_MODE, 0)
        return ThemeMode.entries.getOrElse(modeIndex) { ThemeMode.SYSTEM }
    }

    /**
     * 保存主题模式
     */
    fun saveThemeMode(context: Context, mode: ThemeMode) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_THEME_MODE, mode.ordinal)
            .apply()
        // 立即更新内存状态，实现即时切换
        _currentThemeMode?.value = mode
    }

    /**
     * 初始化主题状态（在 Activity.onCreate 中调用）
     */
    fun initThemeMode(context: Context): MutableState<ThemeMode> {
        val mode = getThemeMode(context)
        _currentThemeMode = mutableStateOf(mode)
        return requireNotNull(_currentThemeMode) { "Theme mode must be initialized" }
    }
}

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color(0xFFFFFFFF),
    secondary = AccentDim,
    onSecondary = TextPrimaryLight,
    tertiary = PriorityMid,
    background = DeepBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceBackgroundLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = ElevatedBackgroundLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderDimLight,
    outlineVariant = BorderBrightLight,
    error = PriorityHigh,
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Color(0xFFFFFFFF),
    secondary = AccentDim,
    onSecondary = TextPrimaryDark,
    tertiary = PriorityMid,
    background = DeepBackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceBackgroundDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = ElevatedBackgroundDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDimDark,
    outlineVariant = BorderBrightDark,
    error = PriorityHigh,
    onError = Color(0xFFFFFFFF)
)

/**
 * 应用主题，从 LocalThemeMode 获取当前主题模式
 */
@Composable
fun TaskLedgerTheme(
    content: @Composable () -> Unit
) {
    // 从 CompositionLocal 获取主题模式
    val themeMode = LocalThemeMode.current.value

    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TaskLedgerTypography,
        content = content
    )
}

/**
 * 判断当前是否为深色主题
 */
@Composable
private fun isDarkTheme(): Boolean {
    val themeMode = LocalThemeMode.current.value
    return when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
}

/**
 * 获取当前主题的背景颜色
 */
@Composable
fun getDeepBackground() = if (isDarkTheme()) DeepBackgroundDark else DeepBackgroundLight

@Composable
fun getSurfaceBackground() = if (isDarkTheme()) SurfaceBackgroundDark else SurfaceBackgroundLight

@Composable
fun getElevatedBackground() = if (isDarkTheme()) ElevatedBackgroundDark else ElevatedBackgroundLight

@Composable
fun getTextPrimary() = if (isDarkTheme()) TextPrimaryDark else TextPrimaryLight

@Composable
fun getTextSecondary() = if (isDarkTheme()) TextSecondaryDark else TextSecondaryLight

@Composable
fun getTextMuted() = if (isDarkTheme()) TextMutedDark else TextMutedLight

@Composable
fun getBorderDim() = if (isDarkTheme()) BorderDimDark else BorderDimLight
