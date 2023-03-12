package it.unipi.gardenfit.screen.plant

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipi.gardenfit.bluetooth.BluetoothProxy
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Plant
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val proxy: FirestoreProxy
) : ViewModel() {

    // Plants stored in the firestore
    val plants = proxy.plnts


    // The name of the new plant inside the dialog 'Add Plant'
    var newName by mutableStateOf("")
        private set

    /**
     * Updates the name of the plant
     *
     * @param input The final name of the new plant
     */
    fun updateNewName(input: String){
        newName = input
    }

    /**
     * Adds a new plant
     *
     * @param newName
     * @param zoneName
     */
    fun addNewPlant(newName: String, zoneName: String){
        proxy.addPlant(Plant(
            zonename = zoneName,
            name = newName,
            connected = "false",
            toMoisturize = "false"
        ))
    }

    /**
     * Deletes a certain plant
     *
     * @param zoneName The zone to whom the plant belongs
     * @param plantName Name of the plant that will be deleted
     */
    fun deletePlant(zoneName: String, plantName: String) {
        proxy.delDocReference("plants", listOf(plantName))
        proxy.delPlantinZone(zoneName, plantName)
        val socket = BluetoothProxy.bluetoothSocket
        BluetoothProxy().cancel(socket!!)
    }

    /**
     * Updates the uri's plant
     *
     * @param plantName Plant's name
     * @param newUri The new plant's uri
     */
    fun updateUriPlant(plantName: String, newUri: Uri){
        proxy.updateUriPlant(plantName, newUri.toString())
    }

    /**
     * Updates the MAC address' plant
     *
     * @param plantName Plant's name
     * @param newMac New MAC address
     */
    fun updateMacAddress(plantName: String, newMac: String){
        proxy.updateMacAddress(plantName, newMac)
    }

    fun updateConnection(plantName: String){
        proxy.updateConnectedPlant(plantName, "true")
    }

    fun updateMoisturized(plantName: String, value: Int){
        if(value == 1){
            proxy.updateToBeMoisturizedPlant(plantName, "true")
        } else {
            proxy.updateToBeMoisturizedPlant(plantName, "false")
        }

    }

}
