package it.unipi.gardenfit.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import it.unipi.gardenfit.R
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.ui.theme.Neutral8

/**
 * Displays the back button
 *
 * @param upPress The function to invocate
 */
@Composable
fun Up(
    upPress: () -> Unit
) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = GardenFitTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.back_button)
        )
    }
}

/**
 * GardenFit surface
 */
@Composable
fun GardenFitSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = GardenFitTheme.colors.uiBackground,
    contentColor: Color = GardenFitTheme.colors.textSecondary,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .background(
                color = color,
                shape = shape
            )
            .clip(shape)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

/**
 * Display a title
 */
@Composable
fun LargeTitle (title: String) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .height(64.dp)

    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}