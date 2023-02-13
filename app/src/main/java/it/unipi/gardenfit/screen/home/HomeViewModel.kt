package it.unipi.gardenfit.screen.home


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipi.gardenfit.data.FirestoreProxy
import javax.inject.Inject

//todo: ci sono da aggiungere i listener
//todo: https://firebase.blog/posts/2022/10/using-coroutines-flows-with-Firebase-on-android

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val proxy: FirestoreProxy
) : ViewModel(){
    val zones = proxy.zns
}













