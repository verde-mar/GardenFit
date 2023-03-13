package it.unipi.gardenfit.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat.*
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Plant
import it.unipi.gardenfit.screen.plant.PlantViewModel
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
class BluetoothProxy @Inject constructor() {

    companion object {
        // Tag used while interacting with the LOG
        private const val TAG = "BluetoothProxy"
        // Current UUID
        val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        // Plant's viewmodel
        val viewmodel = PlantViewModel(FirestoreProxy())
        // Plant's socket for communicating with the app
        var bluetoothSocket:BluetoothSocket? = null
    }

    @Singleton
    fun adapter() : BluetoothAdapter? {
        return BluetoothAdapter.getDefaultAdapter();
    }

    fun enabler(context: Context){
        try {
            if (!adapter()!!.isEnabled) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(context, enableBluetoothIntent, null)
            }
        } catch(e: Exception){
            Log.e(TAG, "adapter(context) is null")
        }
    }

    /*fun setsDiscoverability(context: Context){
        val requestCode = 1;
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        startActivityForResult(discoverableIntent, requestCode)

    }*/

    @SuppressLint("MissingPermission")
    @Composable
    fun LookingForThePlant(plant: Plant, context: Context, receiver: BroadcastReceiver){
        // Starts discovery
        if(adapter()!!.startDiscovery()) {
            // Register for broadcasts when a device is discovered.
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(
                context,
                receiver,
                filter,
                RECEIVER_NOT_EXPORTED)

            LaunchedEffect(Unit) {
                connectorInBackground(plant, context)
            }
        }
    }

    private suspend fun connectorInBackground(plant: Plant, context: Context) = withContext(Dispatchers.IO)  {
        val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            adapter()?.listenUsingInsecureRfcommWithServiceRecord("GardenFit", SPP_UUID)
        }
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Log.e(TAG, "Socket's accept() method failed", e)
                shouldLoop = false
                null
            }

            bluetoothSocket = socket?.also {
                reader(it, plant, context)
                viewmodel.updateConnection(plant.name!!)
                shouldLoop = false
                disconnect(mmServerSocket!!)
            }

        }
    }

    private suspend fun reader(socket: BluetoothSocket, plant: Plant, context: Context) = coroutineScope {
        val inStream: InputStream = socket.inputStream
        val value = ByteArray(1)
        while(true){
            try {
                withContext(Dispatchers.IO) {
                    val input = inStream.read(value)
                    viewmodel.updateMoisturized(plant.name!!, input, context, Date())
                }
            } catch (e: IOException) {
                Log.d(TAG, "Input stream was disconnected")
                break
            }
        }
    }

    fun cancel(socket: BluetoothSocket){
        try {
            socket.close()
        } catch(e: IOException){
            Log.d(TAG, "Cannot close the communication socket")
        }
    }

    /**
     * The app cannot receive more connections
     */
    private fun disconnect(mmServerSocket: BluetoothServerSocket){
        try {
            mmServerSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}