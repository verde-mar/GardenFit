package it.unipi.gardenfit.data

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//todo: https://github.com/firebase/snippets-android/blob/3b2cb1ac473f15bbb502de51ee79a1e23b3b4ceb/firestore/app/src/main/java/com/google/example/firestore/kotlin/DocSnippets.kt#L110-L110
//todo: fare la move di una pianta
//todo: aggiornare la funzione di eliminazione: quando si elimina una zona si eliminano tutte le piante, e quando si elimina una pianta, la si deve eliminare anche dalla zona

@Singleton
class FirestoreProxy @Inject constructor() {
    companion object {
        private const val TAG = "FirestoreProxy"
    }

    /**
     * Get the firestore database
     */
    @Singleton
    fun db(): FirebaseFirestore {
        val db = Firebase.firestore

        // Enable persistence
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

        return db
    }

    /* Zones stored in Firestore */
    val zns: Flow<List<Zone>>
        get() = db().collection("zones").snapshots().map { snapshot ->
                    snapshot.toObjects(Zone::class.java)
                }

    /* Plants stored in Firestore */
    val plnts: Flow<List<Plant>>
        get() = db().collection("plants").snapshots().map { snapshot ->
            snapshot.toObjects(Plant::class.java)
        }



    /**
     * Get document by cache
     */
    fun getDocumentByCache(collection: String, reference: String) {
        val docRef = db().collection(collection).document(reference)
        val source = Source.CACHE

        // Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result
                Log.d(TAG, "Cached document data: ${document?.data}")

            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
        }
    }

    /**
     * Delete one or more documents
     */
    fun delDocReference(collection: String, reference: List<String>) {
        /*if (collection == "zones"){
            for(ref in reference) {
                val plnts = db().collection("plants").whereEqualTo("zonename", ref).get()
                if(plnts.isComplete)
                    for (p in plnts.result.documents) {
                        p.reference
                            .delete()
                            .addOnSuccessListener {
                                Log.d(TAG,
                                    "DocumentSnapshot successfully deleted!")
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
            }
        } else if(collection == "plants"){
            for(ref in reference) {
                val plnt = db().collection("zones").whereArrayContains("plants", ref).get()
                if(plnt.isComplete)
                    for (p in plnt.result.documents) {

                    }
            }
        }*/

        for(ref in reference)
            db().collection(collection).document(ref)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }


    }

    /**
     * Return the document reference
     */
    fun docReference(collection: String, reference: String): DocumentReference {
        return db().document("$collection/$reference")
    }

    /**
     * Add a zone
     */
    fun addZone(zone: Zone) {
        val z = hashMapOf(
            "name" to zone.name,
            "plants" to zone.plants
        )
        db().collection("zones").document(zone.name!!).set(z)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Update zone name
     */
    fun updateZoneName(zone: Zone, newDocumentName: String) {
        delDocReference("zones", listOf(zone.name!!))
        addZone(Zone(newDocumentName, zone.plants))
    }

    /**
     * Update plant's name
     */
    fun updatePlantName(plant: Plant, newDocumentName: String){
        delDocReference("plants", listOf(plant.name!!))
        addPlant(Plant(plant.zone, newDocumentName, plant.connected, plant.toMoisturize, plant.lastSeen))
    }

    /**
     * Update the plants list in the zone
     */
    fun updateZonePlants(zone: Zone, plants: List<String>) {
        val zoneRef = db().collection("zones").document(zone.name!!)

        zoneRef
            .update("plants", plants)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    /**
     * Add a plant in the zone
     */
    fun addPlantInZone(zone: Zone, newPlant: String){
        val zoneRef = db().collection("zones").document(zone.name!!)

        zoneRef
            .update("plants", FieldValue.arrayUnion(newPlant))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    /**
     * Add a plant
     */
    fun addPlant(plant: Plant){
        val z = hashMapOf(
            "zonename" to plant.zone,
            "name" to plant.name,
            "connected" to plant.connected,
            "toMoisturize" to plant.toMoisturize,
            "lastSeen" to plant.lastSeen
        )
        db().collection("plants").document(plant.name!!).set(z)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with name: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        addZone(Zone(plant.zone, listOf(plant.name)))
    }

}

@Singleton
@Composable
fun Alias(proxy: FirestoreProxy){
    val zones = proxy.zns.collectAsStateWithLifecycle(initialValue = emptyList())
    val plants = proxy.plnts.collectAsStateWithLifecycle(initialValue = emptyList())
}