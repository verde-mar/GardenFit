package it.unipi.gardenfit.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipi.gardenfit.data.FirestoreProxy
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    proxy: FirestoreProxy
) : ViewModel(){
    // Stored zones in Firestore
    val zones = proxy.zns
}













