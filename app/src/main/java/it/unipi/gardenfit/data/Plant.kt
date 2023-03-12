package it.unipi.gardenfit.data

/**
 * This data class represents a plant inside GardenFit
 */
data class Plant(
    // The zone's plant
    var zonename: String? = null,
    // The plant's name
    val name: String? = null,
    // Shows if the plant is paired to the phone
    var connected: String? = null,
    // Says if the plant is to be moisturized
    val toMoisturize: String? = null,
    // The last time the plant was moisturized
    val lastSeen: String? = null,
    // The image's uri of the plant
    var uri: String? = null,
    // Its MAC address
    val mac: String? = null
)