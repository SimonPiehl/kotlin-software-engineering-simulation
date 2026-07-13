package de.unisaarland.cs.se.selab.model

/**
 * Statistic
 *
 * @constructor Create Statistic
 */
class Statistic {
    private var potatoAmount = 0
    private var wheatAmount = 0
    private var oatAmount = 0
    private var pumpkinAmount = 0
    private var appleAmount = 0
    private var almondAmount = 0
    private var cherryAmount = 0
    private var grapeAmount = 0

    /**
     * Compute combined harvest
     *
     * @return
     */
    fun computeCombinedHarvest(): Int =
        potatoAmount + wheatAmount + oatAmount +
            pumpkinAmount + appleAmount + almondAmount +
            cherryAmount + grapeAmount

    /**
     * Get potato amount
     *
     * @return
     */
    fun getPotatoAmount(): Int = potatoAmount

    /**
     * Get wheat amount
     *
     * @return
     */
    fun getWheatAmount(): Int = wheatAmount

    /**
     * Get oat amount
     *
     * @return
     */
    fun getOatAmount(): Int = oatAmount

    /**
     * Get pumpkin amount
     *
     * @return
     */
    fun getPumpkinAmount(): Int = pumpkinAmount

    /**
     * Get apple amount
     *
     * @return
     */
    fun getAppleAmount(): Int = appleAmount

    /**
     * Get almond amount
     *
     * @return
     */
    fun getAlmondAmount(): Int = almondAmount

    /**
     * Get cherry amount
     *
     * @return
     */
    fun getCherryAmount(): Int = cherryAmount

    /**
     * Get grape amount
     *
     * @return
     */
    fun getGrapeAmount(): Int = grapeAmount

    /**
     * Add plant amount
     *
     * @param type
     * @param amount
     */
    fun addPlantAmount(type: PlantType, amount: Int) {
        when (type) {
            PlantType.POTATO -> potatoAmount += amount
            PlantType.WHEAT -> wheatAmount += amount
            PlantType.OAT -> oatAmount += amount
            PlantType.PUMPKIN -> pumpkinAmount += amount
            PlantType.APPLE -> appleAmount += amount
            PlantType.ALMOND -> almondAmount += amount
            PlantType.CHERRY -> cherryAmount += amount
            PlantType.GRAPE -> grapeAmount += amount
        }
    }
}
