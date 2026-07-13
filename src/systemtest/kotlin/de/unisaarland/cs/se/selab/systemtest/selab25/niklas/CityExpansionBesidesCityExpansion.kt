package de.unisaarland.cs.se.selab.systemtest.selab25.niklas

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.util.YearTicks

/**
 * A city expansion happens that is not besides a city during initialization
 * but before this city expansion happens another city expansion happens besides it, so the second city expansion
 * should be vaild
 */
class CityExpansionBesidesCityExpansion : ExampleSystemTestExtension() {
    override val name = "CityExpansionBesidesCityExpansion Niklas"
    override val description = "A city expansion happens that is not besides a city during initialization" +
        "but before this city expansion happens another city expansion happens besides it, " +
        "so the second city expansion should be valid "

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "example/farms.json"
    override val scenario = "niklas/scenarioCityExpansionBesidesCityExpansion.json"
    override val map = "niklas/mapCityExpansionBesidesCityExpansion.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = YearTicks.EARLY_MARCH

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: mapCityExpansionBesidesCityExpansion.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] " +
                "Initialization Info: farms.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] " +
                "Initialization Info: scenarioCityExpansionBesidesCityExpansion.json successfully parsed and validated."
        )
    }
}
