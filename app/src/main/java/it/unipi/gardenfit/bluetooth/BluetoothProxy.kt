package it.unipi.gardenfit.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
        return BluetoothAdapter.getDefaultAdapter()
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

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    fun ensureDiscoverable(context: Context) {
        if(adapter()!!.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            startActivity(context, discoverableIntent, null)
        }
    }


    fun lookingForThePlant(plantName: String?, context: Context) {
        Log.e(TAG, "Starting discovery")

        Toast.makeText(context, "Trying to find the device", Toast.LENGTH_LONG).show()

        val devices = adapter()!!.bondedDevices
        devices.forEach( ){ device ->
            Log.e(TAG, "DEVICE NAME: ${device.name}")
                if(device.name.contains(plantName!!)){
                    viewmodel.updateMacAddress(plantName, device.address)
                    viewmodel.updateConnection(plantName)
                    return
                }
        }

        // Starts discovery
        adapter()!!.startDiscovery()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun accepterInBackground(plant: Plant, context: Context) = withContext(Dispatchers.IO)  {
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

            if(socket != null) {
                bluetoothSocket = socket.also {
                    reader(it, plant, context)
                    viewmodel.updateConnection(plant.name!!)
                    shouldLoop = false
                    disconnect(mmServerSocket!!)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
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