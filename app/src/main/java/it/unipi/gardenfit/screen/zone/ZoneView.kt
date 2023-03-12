package it.unipi.gardenfit.screen.zone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.unipi.gardenfit.R
import it.unipi.gardenfit.data.Zone
import it.unipi.gardenfit.ui.theme.GardenFitTheme

/**
 * This functions displays a zone in Home Screen
 */
@Composable
fun ZoneView(
    zone: Zone,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
    ) {
        // Zone's icon
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_eco_24),
            contentDescription = "Zone's icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(32.dp),
            tint = GardenFitTheme.colors.gradient31[0]
        )
        // Zone's name
        Text(
            zone.name!!,
            style = MaterialTheme.typography.body1,
            modifier = modifier.padding(8.dp)
        )
    }
}
