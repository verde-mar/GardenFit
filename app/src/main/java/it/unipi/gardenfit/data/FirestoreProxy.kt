package it.unipi.gardenfit.data

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import it.unipi.gardenfit.register.RegisterActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreProxy @Inject constructor() {
    companion object {
        var username = RegisterActivity.username
    }
    // Tag used while interacting with the LOG
    private val TAG = "FirestoreProxy"

    /**
     * Gets the firestore database
     *
     * @return db The FirebaseFirestore instance of the default FirebaseApp
     */
    @Singleton
    fun db(): FirebaseFirestore {

        return Firebase.firestore
    }

    // Zones stored in the Firestore
    val zns: Flow<List<Zone>>
        get() = db().collection("${username}-zones").snapshots().map { snapshot ->
            snapshot.toObjects(Zone::class.java)
        }

    // Plants stored in the Firestore
    val plnts: Flow<List<Plant>>
        get() = db().collection("${username}-plants").snapshots().map { snapshot ->
            snapshot.toObjects(Plant::class.java)
        }

    /**
     * Deletes one or more objects: they can be plants or zones
     *
     * @param collection
     * @param reference
     */
    fun delDocReference(collection: String, reference: List<String>) {
        for (ref in reference)
            db().collection("${username}-${collection}").document(ref)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "Object $ref successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting object $ref", e) }
    }

    /**
     * Adds a zone
     *
     * @param zone The zone that is going to be added
     */
    fun addZone(zone: Zone) {
        // Creates the zone
        val z = hashMapOf(
            "name" to zone.name,
            "plants" to zone.plants
        )

        // Sends the zone to the Firestore
        db().collection("${username}-zones").document(zone.name!!).set(z)
            .addOnSuccessListener {
                Log.d(TAG, "Zone written with name: ${zone.name}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding zone", e)
            }
    }

    /**
     * Adds the plant's name in the plants list inside zone 'zoneName'
     *
     * @param zoneName The name of the zone
     * @param newPlant The name of the plant
     */
    private fun addPlantInZone(zoneName: String?, newPlant: String) {
        val zoneRef = db().collection("${username}-zones").document(zoneName!!)

        zoneRef
            .update("plants", FieldValue.arrayUnion(newPlant))
            .addOnSuccessListener { Log.d(TAG, "Zone successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating zone", e) }
    }

    /**
     * Adds a plant
     *
     * @param plant The plant that is going to be added
     */
    fun addPlant(plant: Plant) {
        // Creates the plant
        val z = hashMapOf(
            "zonename" to plant.zonename,
            "name" to plant.name,
            "connected" to plant.connected,
            "toMoisturize" to plant.toMoisturize
        )

        // Sends the plant to the Firestore:
        // 1 - it's going to be added in the 'plants' section
        db().collection("${username}-plants").document(plant.name!!).set(z)
            .addOnSuccessListener {
                Log.d(TAG, "Plant written with name: ${plant.name}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding plant", e)
            }

        // 2 - and to the 'zones' section
        addPlantInZone(plant.zonename.toString(), plant.name)
    }

    /**
     * Deletes a plant in the zone's plants' list
     *
     * @param zoneName Zone's name
     * @param plantName Plant's name
     */
    fun delPlantinZone(zoneName: String, plantName: String) {
        val zoneRef = db().collection("${username}-zones").document(zoneName)

        zoneRef
            .update("plants", FieldValue.arrayRemove(plantName))
            .addOnSuccessListener { Log.d(TAG, "Zone $zoneName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating zone $zoneName", e) }
    }

    /**
     * Updates a plant's uri
     *
     * @param plantName Plant's name
     * @param newUri New plant's uri
     */
    fun updateUriPlant(plantName: String, newUri: String){
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("uri", newUri)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }

    /**
     * Updates a plant's zone name
     *
     * @param plantName Plant's name
     * @param zoneName Zone's name
     */
    fun updateZoneNamePlant(plantName: String, zoneName: String){
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("zonename", zoneName)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }

    /**
     * Updates a plant's connection flag
     *
     * @param plantName Plant's name
     * @param connected Connected flag
     */
    fun updateConnectedPlant(plantName: String, connected: String){
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("connected", connected)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }

    /**
     * Updates a plant's toBeMoisturized flag
     *
     * @param plantName Plant's name
     * @param toBeMoisturized To be moisturized flag
     */
    fun updateToBeMoisturizedPlant(plantName: String, toBeMoisturized: String){
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("toBeMoisturized", toBeMoisturized)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }

    /**
     * Updates a plant's MAC address
     *
     * @param plantName Plant's name
     * @param macAddress Plant's mac address
     */
    fun updateMacAddress(plantName: String, macAddress: String){
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("mac", macAddress)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }

    /**
     * Updates a plant's last moisturizing
     *
     * @param plantName Plant's name
     * @param date When was the last time it was moisturized
     */
    fun updateLastSeen(plantName: String, date: String) {
        val plantRef = db().collection("${username}-plants").document(plantName)

        plantRef
            .update("lastSeen", date)
            .addOnSuccessListener { Log.d(TAG, "Plant $plantName successfully updated!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error updating plant $plantName", e) }
    }
}