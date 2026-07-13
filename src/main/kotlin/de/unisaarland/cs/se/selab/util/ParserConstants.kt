package de.unisaarland.cs.se.selab.util

/**
 * object holding all JSON key constants used in the parsing process.
 */
object ParserConstants {
    const val KEY_ID = "id"

    // For TileParser
    const val KEY_CATEGORY = "category"
    const val KEY_COORDINATES = "coordinates"
    const val KEY_X = "x"
    const val KEY_Y = "y"
    const val KEY_AIRFLOW = "airflow"
    const val KEY_DIRECTION = "direction"
    const val KEY_SHED = "shed"
    const val KEY_FARM = "farm"
    const val KEY_CAPACITY = "capacity"
    const val KEY_PLANT = "plant"
    const val KEY_POSSIBLE_PLANTS = "possiblePlants"

    // For IncidentParser
    const val KEY_TYPE = "type"
    const val KEY_TICK = "tick"
    const val KEY_LOCATION = "location"
    const val KEY_RADIUS = "radius"
    const val KEY_DURATION = "duration"
    const val KEY_AMOUNT = "amount"
    const val KEY_EFFECT = "effect"
    const val KEY_MACHINE_ID = "machineId"

    // For FarmParser
    const val ID = "id"
    const val FIELDS = "fields"
    const val MAX_DURATION = 14
    const val FARM_KEY_SET_SIZE = 7
    const val MACHINE_KEY_SET_SIZE = 6
    const val SOWING_PLAN_FIELDS_TYPE_KEY_SET_SIZE = 4
    const val SOWING_PLAN_RADIUS_TYPE_KEY_SET_SIZE = 5
}
