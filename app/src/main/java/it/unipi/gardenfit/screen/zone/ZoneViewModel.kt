package it.unipi.gardenfit.screen.zone

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipi.gardenfit.data.FirestoreProxy
import javax.inject.Inject

//todo: qui serve un rememberstate della pianta e del suo innaffiamento
@HiltViewModel
class ZoneViewModel @Inject constructor(
    private val proxy: FirestoreProxy
) : ViewModel() {
    val zones = proxy.zns
}



