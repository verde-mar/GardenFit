package it.unipi.gardenfit.data

/**
 * This data class represents a plant inside GardenFit
 */
data class Plant(
    var zone: String? = null,
    val name: String? = null,
    val connected: String? = null,
    val toMoisturize: String? = null,
    val lastSeen: String? = null
)