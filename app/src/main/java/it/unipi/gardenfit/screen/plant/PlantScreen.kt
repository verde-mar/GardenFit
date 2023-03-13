package it.unipi.gardenfit.screen.plant

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import it.unipi.gardenfit.R
import it.unipi.gardenfit.bluetooth.BluetoothProxy
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Plant
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.GardenFitSurface
import it.unipi.gardenfit.util.LargeTitle
import it.unipi.gardenfit.util.Up
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)
private const val TAG = "PlantScreen"

/**
 * Function that builds the plant screen
 *
 * @param plantName
 * @param upPress
 */
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun PlantScreen(
    plantName: String,
    upPress: () -> Unit
) {
    val viewmodel = PlantViewModel(FirestoreProxy())
    val plants = viewmodel.plants.collectAsStateWithLifecycle(initialValue = emptyList())
    val context = LocalContext.current

    plants.value.forEach { plant ->
        if(plant.name == plantName){
            // Receiver needed for the bluetooth connection
            // It will be removed from this Screen as future improvement
            val bluetoothReceiver = object : BroadcastReceiver() {
                @SuppressLint("MissingPermission")
                override fun onReceive(context: Context, intent: Intent) {
                    when(intent.action) {
                        BluetoothDevice.ACTION_FOUND -> {
                            // Discovery has found a device. Get the BluetoothDevice
                            // object and its info from the Intent.
                            val device: BluetoothDevice? =
                                intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            val deviceName = device?.name
                            val deviceHardwareAddress = device?.address // MAC address
                            if(deviceName!!.contains(plantName))
                                viewmodel.updateMacAddress(plantName, deviceHardwareAddress!!)
                        }
                    }
                }
            }


            Box(Modifier.fillMaxSize()) {
                val scroll = rememberScrollState(0)
                Header()
                Body(scroll, plant, bluetoothReceiver, context, viewmodel)
                Title(plantName) { scroll.value }
                PhotoPlant(plant) { scroll.value }
                Up(upPress = upPress)
            }


        }
    }
}

/**
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

/**
 * Plant's body, included all its data
 *
 * @param scroll
 * @param plant
 */
@RequiresApi(Build.VERSION_CODES.M)
@Composable
private fun Body(
    scroll: ScrollState,
    plant: Plant,
    receiver: BroadcastReceiver,
    context: Context,
    viewmodel: PlantViewModel
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
                    Spacer(Modifier.height(115.dp))
                    Spacer(Modifier.height(128.dp))

                    if (plant.connected != null) {
                        if (plant.connected == "true") {
                            Text(
                                text = "I'm paired",
                                style = MaterialTheme.typography.body1,
                                color = GardenFitTheme.colors.textPrimary,
                                modifier = HzPadding,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            val bluetoothProxy = BluetoothProxy()
                            Button(
                                onClick = {
                                    bluetoothProxy.enabler(context)
                                },
                                modifier = HzPadding,
                            ) {
                                Text(
                                    text = "Not paired",
                                    style = MaterialTheme.typography.body1,
                                    color = GardenFitTheme.colors.textInteractive
                                )

                                //todo: funziona? funziona come onClick?

                                bluetoothProxy.LookingForThePlant(
                                    plant = plant,
                                    context = context,
                                    receiver = receiver)

                                Toast.makeText(context, "Trying to connect", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Log.e(TAG, "Value 'connected' of plant ${plant.name} is null")
                    }

                    Spacer(Modifier.height(8.dp))

                    if (plant.toMoisturize != null) {
                        createChannel(
                            stringResource(R.string.plant_channel),
                            stringResource(R.string.plant),
                            context
                        )
                        if (plant.toMoisturize == "true") {

                            Text(
                                text = "Moisturize me!",
                                style = MaterialTheme.typography.body1,
                                color = GardenFitTheme.colors.error,
                                modifier = HzPadding.padding(4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            // Plant was moisturized
                            plant.name?.let { viewmodel.updateMoisturized(it, 0, context, Date()) }
                            Text(
                                text = "Moisturized",
                                style = MaterialTheme.typography.body1,
                                color = GardenFitTheme.colors.gradient22[1],
                                modifier = HzPadding.padding(4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                    } else {
                        Log.e(TAG, "Value 'toMoisturize' of plant ${plant.name} is null")
                    }

                    Spacer(Modifier.height(8.dp))

                    if(plant.lastSeen != null ) {
                        Text(
                            text = "Last time you watered me: ${plant.lastSeen}",
                            style = MaterialTheme.typography.body1,
                            color = GardenFitTheme.colors.textHelp,
                            modifier = HzPadding
                        )
                    } else {
                        Log.e(TAG, "Value 'lastSeen' of plant ${plant.name} is null")
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 56.dp)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Plant's title
 *
 * @param plantName Plant's name
 * @param scrollProvider Scroll
 */
@Composable
private fun Title(plantName: String, scrollProvider: () -> Int) {
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
        LargeTitle(title = plantName)
    }
}

/**
 * Plant's image
 *
 * @param plant Current plant
 * @param scrollProvider Scroll
 */
@Composable
private fun PhotoPlant(
    plant: Plant,
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
        // Displays plant's photo
        PlantImage(
            modifier = Modifier.fillMaxSize(),
            plant
        )
    }
}

/**
 * Plant's photo
 *
 * @param modifier Modifier
 * @param plant Current plant
 */
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
                    color = androidx.compose.ui.graphics.Color.Gray,
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

/**
 * Collapse image layout
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

/**
 * Creates channel for notifications
 *
 * @param channelId Channel id
 * @param channelName Channel name
 * @param context Context
 */
private fun createChannel(channelId: String, channelName: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Moisturizing reminder"

        getSystemService(
            context,
            NotificationManager::class.java
    )?.createNotificationChannel(notificationChannel)

    }
}