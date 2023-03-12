package it.unipi.gardenfit.screen.zone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Zone
import javax.inject.Inject

@HiltViewModel
class ZoneViewModel @Inject constructor(
    private val proxy: FirestoreProxy
) : ViewModel() {
    // Zones stored in firestore
    val zones = proxy.zns

    // The name of the new zone inside the dialog 'Add Zone'
    var newName by mutableStateOf("")
        private set

    /**
     * Updates the name of the zone
     *
     * @param input The final name of the new zone
     */
    fun updateNewName(input: String){
        newName = input
    }

    /**
     * Adds a new zone
     *
     * @param newName The zone's name
     */
    fun addNewZone(newName: String){
        proxy.addZone(Zone(newName, listOf()))
    }

    /**
     * Deletes a zone
     *
     * @param zoneName The name of the zone that will be deleted
     */
    fun deleteZone(zoneName: String) {
        proxy.delDocReference("zones", listOf(zoneName))
    }
}



