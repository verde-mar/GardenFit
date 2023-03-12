package it.unipi.gardenfit.data

/**
 * This data class represents a zone inside GardenFit
 */
data class Zone(
    // The zone's name
    var name: String? = null,
    // The plants names list
    var plants: List<String>? = null
)

