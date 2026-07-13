package de.unisaarland.cs.se.selab.model

/**
 * Farm
 *
 * @property id
 * @property name
 * @property machines List of all machines the cloud owns
 * @property plans All sowing plans the farm wants to sow during the sim
 * @property statistic the harvest statistic of the farm
 * @constructor
 */
class Farm(
    private val id: Int,
    private val name: String,
    private val machines: List<Machine>,
    private val plans: MutableList<SowingPlan>,
    private val statistic: Statistic
) {

    constructor(id: Int, name: String, machines: List<Machine>, plans: MutableList<SowingPlan>) :
        this(id, name, machines, plans, Statistic())

    /**
     * Remove used Plans
     */
    fun removePlan(plan: SowingPlan) {
        this.plans.remove(plan)
    }

    /**
     * Get machines by action
     *
     * @param action
     * @return
     */
    fun getMachinesByAction(action: Action): List<Machine> {
        return machines.filter { m -> m.getPossibleActions().contains(action) }
    }

    /**
     * Get all machines that can do a certain action on a certain plan
     *
     * @param plant
     * @param action
     * @return
     */
    fun getMachinesByPlantAndAction(plant: PlantType, action: Action): List<Machine> {
        return machines.filter { m ->
            m.getPossiblePlants().contains(plant) &&
                m.getPossibleActions().contains(action)
        }.sortedBy { it.getID() }
    }

    /**
     * Add the harvest of a given Tile to the farms statistic
     *
     * @param type
     * @param amount
     */
    fun addToStatistic(type: PlantType, amount: Int) {
        statistic.addPlantAmount(type, amount)
    }

    /**
     * Get ID of farm
     *
     * @return
     */
    fun getID(): Int = this.id

    /**
     * Get name
     *
     * @return
     */
    fun getName(): String = this.name

    /**
     * Get plans
     *
     * @return
     */
    fun getPlans(): List<SowingPlan> {
        return plans.sortedWith(compareBy({ it.getStartTick() }, { it.getID() }))
    }

    /**
     * Get statistic
     *
     * @return
     */
    fun getStatistic(): Statistic = this.statistic

    /**
     * Get machine by ID
     *
     * @param machineId
     * @return
     */
    fun getMachineByID(machineId: Int): Machine? {
        return machines.firstOrNull { it.getID() == machineId }
    }

    /**
     * Get all machines of the farm
     *
     * @return
     */
    fun getMachines(): List<Machine> {
        return machines.sortedBy { it.getID() }
    }
}
