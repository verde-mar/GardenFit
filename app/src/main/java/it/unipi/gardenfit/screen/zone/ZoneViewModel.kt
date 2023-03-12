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
    val zones = proxy.zns
    var newName by mutableStateOf("")
        private set

    fun updateNewName(input: String){
        newName = input
    }

    fun addNewZone(newName: String){
        proxy.addZone(Zone(newName, listOf()))
    }

    fun deleteZone(zoneName: String) {
        //todo: prima devi eliminare le piante
        proxy.delDocReference("zones", listOf(zoneName))
    }
}



