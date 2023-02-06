package it.unipi.gardenfit.screen.zone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ZoneView(
    zone: Zone,
    modifier: Modifier = Modifier,
    onClick: () -> Unit //todo: serve?
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(GardenFitTheme.colors.iconInteractive, RoundedCornerShape(2.dp))
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_eco_24),
            contentDescription = "Zone's field",
            tint = GardenFitTheme.colors.tornado1[0],
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .size(32.dp)
        )
        Text(
            zone.name!!,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(8.dp)
        )
    }
}

/*
@Preview
@Composable
fun HomePreview() {
    GardenFitTheme {
        ZoneView()
    }
}*/