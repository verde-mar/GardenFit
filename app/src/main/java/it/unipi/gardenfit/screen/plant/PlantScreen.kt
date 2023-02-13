package it.unipi.gardenfit.screen.plant

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import it.unipi.gardenfit.data.Plant
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.GardenFitDivider
import it.unipi.gardenfit.util.GardenFitSurface
import it.unipi.gardenfit.util.Up
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

//todo: deve implementare il bottone per capire se e' connesso, ed eventualmente connettersi
//todo: deve implementare il cambio di scritta nel caso sia connesso o no,
//todo:  e anche nel caso in cui sia da innaffiare o meno
//todo: implementare la riga L'ULTIMA VOLTA IN CUI MI HAI INNAFFIATO: dd/mm
//todo: implementare il grafo


@Composable
fun PlantScreen(
    plantName: String,
    upPress: () -> Unit
) {

    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(scroll)
        Title(Plant("zona", "null", "false", "false", Date().toString())) { scroll.value }
        Image { scroll.value }
        Up(upPress = upPress)
    }
}

/*
 * Plant's header
 */
@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(GardenFitTheme.colors.tornado1))
    )
}

/*
 * Plant's body, included all its data
 */
@Composable
private fun Body(
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            GardenFitSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "am i connected",
                        style = MaterialTheme.typography.body1,
                        color = GardenFitTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "water me or dont",
                        style = MaterialTheme.typography.body1,
                        color = GardenFitTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "last time you watered me: WHOKNOWS",
                        style = MaterialTheme.typography.body1,
                        color = GardenFitTheme.colors.textHelp,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))
                    GardenFitDivider()
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "IMMA GOING TO BUILD A GRAPH IN HERE",
                        style = MaterialTheme.typography.body1,
                        color = GardenFitTheme.colors.textHelp,
                        modifier = HzPadding
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

/*
 * Plant's name and photo
 */
@Composable
private fun Title(plant: Plant, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = GardenFitTheme.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = plant.name!!,
            style = MaterialTheme.typography.h4,
            color = GardenFitTheme.colors.textSecondary,
            modifier = HzPadding
        )
        Spacer(Modifier.height(8.dp))
        GardenFitDivider()
    }
}


/*
 * Plant's photo
 */
@Composable
private fun Image(
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {

    }
}

/*
* Collapse the image while scrolling
*/
@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}


@Preview
@Composable
fun PlantPreview() {
    GardenFitTheme {
        //PlantScreen()
    }
}