package com.linku.im.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomTheme(
    val primary: Color,
    val onPrimary: Color,
    val primaryDisable: Color,
    val onPrimaryDisable: Color,
    val surface: Color,
    val onSurface: Color,
    val subSurface: Color,
    val onSubSurface: Color,
    val background: Color,
    val onBackground: Color,
    val pressed: Color,
    val onPressed: Color,
    val chatBackground: Color,
    val bubbleEnd: Color,
    val onBubbleEnd: Color,
    val bubbleStart: Color,
    val onBubbleStart: Color,
    val divider: Color,
    val error: Color,
    val onError: Color
)

val defaultLight = CustomTheme(
    primary = Color(0xff837fc9),
    onPrimary = Color(0xffeef7fb),
    surface = Color(0xFFeeeeee),
    onSurface = Color(0xFF191C1B),
    subSurface = Color(0xff837fc9),
    onSubSurface = Color(0xffeef7fb),
    chatBackground = Color(0xff7eb2a8),
    bubbleStart = Color(0xffefefef),
    onBubbleStart = Color(0xff000000),
    background = Color(0xfffefefe),
    onBackground = Color(0xff2a2a2a),
    pressed = Color(0xfff8f8f8),
    onPressed = Color(0xff323232),
    primaryDisable = Color(0xffc7c6cb),
    onPrimaryDisable = Color(0xfff6f5f9),
    bubbleEnd = Color(0xff5a91de),
    onBubbleEnd = Color(0xffdcf7fa),
    divider = Color(0xFFf0f0f0),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)
val defaultDark = CustomTheme(
    primary = Color(0xff42b6fe),
    surface = Color(0xff232325),
    onSurface = Color(0xFFffffff),
    subSurface = Color(0xff232325),
    onSubSurface = Color(0xFFffffff),
    chatBackground = Color(0xff141622),
    bubbleStart = Color(0xff202123),
    onBubbleStart = Color(0xffffffff),
    background = Color(0xff181818),
    onBackground = Color(0xffffffff),
    pressed = Color(0xff222222),
    onPressed = Color(0xff323232),
    primaryDisable = Color(0xff8e8e8e),
    onPrimaryDisable = Color(0xff8e8e8e),
    onPrimary = Color(0xffffffff),
    bubbleEnd = Color(0xff387ab4),
    onBubbleEnd = Color(0xffeef5f9),
    divider = Color(0xFF0A0A0A),
    error = Color(0xfff2b8b5),
    onError = Color(0xff601410)
)

val LocalTheme = staticCompositionLocalOf<CustomTheme> { error("no theme provided") }