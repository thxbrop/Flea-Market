package com.linku.im.ui.theme

import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.linku.im.vm


private val LightColors = lightColorScheme(
    surfaceTint = md_theme_light_surfaceTint,
    onErrorContainer = md_theme_light_onErrorContainer,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    tertiary = md_theme_light_tertiary,
    error = md_theme_light_error,
    outline = md_theme_light_outline,
    onBackground = md_theme_light_onBackground,
    background = md_theme_light_background,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    surface = md_theme_light_surface,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    secondary = md_theme_light_secondary,
    inversePrimary = md_theme_light_inversePrimary,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    primary = md_theme_light_primary,
)


private val DarkColors = darkColorScheme(
    surfaceTint = md_theme_dark_surfaceTint,
    onErrorContainer = md_theme_dark_onErrorContainer,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    tertiary = md_theme_dark_tertiary,
    error = md_theme_dark_error,
    outline = md_theme_dark_outline,
    onBackground = md_theme_dark_onBackground,
    background = md_theme_dark_background,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    surface = md_theme_dark_surface,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    secondary = md_theme_dark_secondary,
    inversePrimary = md_theme_dark_inversePrimary,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    primary = md_theme_dark_primary,
)

val supportDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    enableDynamic: Boolean = supportDynamic,
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme)
        if (enableDynamic and supportDynamic) dynamicLightColorScheme(LocalContext.current)
        else LightColors
    else
        if (enableDynamic and supportDynamic) dynamicDarkColorScheme(LocalContext.current)
        else DarkColors

    val navController = rememberAnimatedNavController()
    val expandColor = ExpandColor(
        divider = if (!useDarkTheme) md_theme_light_divider else md_theme_dark_divider
    )
    val animatedSpec = tween<Color>(200, 0, FastOutSlowInEasing)

    val containerColor by animateColorAsState(
        if (vm.readable.isDarkMode) colors.surface
        else colors.primary,
        animatedSpec
    )
    val onContainerColor by animateColorAsState(
        if (vm.readable.isDarkMode) colors.onSurface
        else colors.onPrimary,
        animatedSpec
    )
    val backgroundColor by animateColorAsState(
        colors.background,
        animatedSpec
    )
    val onBackgroundColor by animateColorAsState(
        colors.onBackground,
        animatedSpec
    )
    val surfaceColor by animateColorAsState(
        colors.surface,
        animatedSpec
    )
    val onSurfaceColor by animateColorAsState(
        colors.onSurface,
        animatedSpec
    )
    val animatedColor = AnimatedColor(
        containerColor = containerColor,
        onContainerColor = onContainerColor,
        backgroundColor = backgroundColor,
        onBackgroundColor = onBackgroundColor,
        surfaceColor = surfaceColor,
        onSurfaceColor = onSurfaceColor
    )
    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalNavController provides navController,
        LocalExpandColor provides expandColor,
        LocalAnimatedColor provides animatedColor
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content,
            typography = MaterialTheme.typography.copy(
                titleLarge = MaterialTheme.typography.titleLarge.withDefaultFontFamily(),
                titleMedium = MaterialTheme.typography.titleMedium.withDefaultFontFamily(),
                titleSmall = MaterialTheme.typography.titleSmall.withDefaultFontFamily(),
                bodyLarge = MaterialTheme.typography.bodyLarge.withDefaultFontFamily(),
                bodyMedium = MaterialTheme.typography.bodyMedium.withDefaultFontFamily(),
                bodySmall = MaterialTheme.typography.bodySmall.withDefaultFontFamily(),
                displayLarge = MaterialTheme.typography.displayLarge.withDefaultFontFamily(),
                displayMedium = MaterialTheme.typography.displayMedium.withDefaultFontFamily(),
                displaySmall = MaterialTheme.typography.displaySmall.withDefaultFontFamily(),
                headlineLarge = MaterialTheme.typography.headlineLarge.withDefaultFontFamily(),
                headlineMedium = MaterialTheme.typography.headlineMedium.withDefaultFontFamily(),
                headlineSmall = MaterialTheme.typography.headlineSmall.withDefaultFontFamily(),
                labelLarge = MaterialTheme.typography.labelLarge.withDefaultFontFamily(),
                labelMedium = MaterialTheme.typography.labelMedium.withDefaultFontFamily(),
                labelSmall = MaterialTheme.typography.labelSmall.withDefaultFontFamily()
            )
        )
    }
}
