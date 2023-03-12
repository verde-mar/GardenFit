package it.unipi.gardenfit.screen.plant

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Plant

@Composable
fun PlantImage(modifier: Modifier, plant: Plant) {
    var hasImage by remember {
        mutableStateOf(false)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )
    // Current context
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(20.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .height(100.dp)
                .width(100.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = CircleShape
                )
        ) {
            // If the photo was taken
            if (plant.uri != null) {
                AsyncImage(
                    model = Uri.parse(plant.uri),
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Selected image",
                )
            }
            // If the photo wasn't taken
            else if(!hasImage){
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        val uri = ComposeFileProvider.getImageUri(plant.name!!, context)
                        cameraLauncher.launch(uri)
                        PlantViewModel(FirestoreProxy()).updateUriPlant(plant.name, uri)

                    },
                ) {
                    Text(
                        text = "Take photo"
                    )
                }
            }
        }
    }
}