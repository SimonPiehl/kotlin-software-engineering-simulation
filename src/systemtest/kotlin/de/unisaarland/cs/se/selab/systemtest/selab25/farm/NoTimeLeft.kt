package de.unisaarland.cs.se.selab.systemtest.selab25.farm

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.CloudSystemExtension

const val AJXFRGML = "[IMPORTANT] Farm: Farm 0 starts its actions."
const val BBKHYSBZ = "[IMPORTANT] Farm: Farm 0 finished its actions."
const val UBWKQMQB = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 1 for 1 days."
const val FIAGBFDB = "[IMPORTANT] Farm Harvest: Machine 6 has collected 1115370 g of APPLE harvest."
const val SVJZYIZR = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 2 for 1 days."
const val SUSVSEOG = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 3 for 1 days."
const val QUMIKFKL = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 4 for 1 days."
const val OKZZZMTD = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 5 for 1 days."
const val KZTVGFKW = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 6 for 1 days."
const val SHABGYFL = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 7 for 1 days."
const val WGMEAEUB = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 8 for 1 days."
const val YBYJQOUU = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 9 for 1 days."
const val QXCQVHTK = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 10 for 1 days."
const val IMMMDGNH = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 11 for 1 days."
const val THDESVNL = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 12 for 1 days."
const val SMNODIHA = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 13 for 1 days."
const val ZGUONMRH = "[IMPORTANT] Farm Action: Machine 6 performs HARVESTING on tile 14 for 1 days."
const val DGDXSEHT = "[IMPORTANT] Farm Machine: Machine 6 is finished and returns to the shed at 0."
const val VOZNSUST = "[IMPORTANT] Farm Machine: Machine 6 unloads 15615180 g of APPLE harvest in the shed."
const val OYHLIGSF = "[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 15 for 3 days."
const val KOEHCERM = "[IMPORTANT] Farm Harvest: Machine 4 has collected 1115370 g of APPLE harvest."
const val LIFPRMBY = "[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 16 for 3 days."
const val JBFNRODY = "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 0."
const val TUGMEXST = "[IMPORTANT] Farm Machine: Machine 4 unloads 2230740 g of APPLE harvest in the shed."
const val SEKOMOBU = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 1 for 1 days."
const val PDBPWPZN = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 2 for 1 days."
const val JVLHWTLL = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 3 for 1 days."
const val RUSTUDIC = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 4 for 1 days."
const val STJKUIIX = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 5 for 1 days."
const val UZEVILCU = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 6 for 1 days."
const val MJIILRXR = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 7 for 1 days."
const val PYIKNZOU = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 8 for 1 days."
const val LGOXKFDT = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 9 for 1 days."
const val LTSEFRLC = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 10 for 1 days."
const val ENFGDTYL = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 11 for 1 days."
const val DRGQLPQI = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 12 for 1 days."
const val TWCYHFLX = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 13 for 1 days."
const val CRELJYCO = "[IMPORTANT] Farm Action: Machine 6 performs CUTTING on tile 14 for 1 days."
const val UXMBVRIV = "[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 15 for 3 days."
const val BJRIQLYT = "[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 16 for 3 days."

/**
 * Farm system test
 */
class NoTimeLeft : CloudSystemExtension() {
    override val name = "Machine works too much"
    override val description = "tests if the machine stops working if it runs out of time"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FarmControllertest/farmnotime.json"
    override val scenario = "FarmControllertest/scenario.json"
    override val map = "FarmControllertest/mapnotime.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 6
    override val startYearTick = 16

    override suspend fun run() {
        assertNextLine(AJXFRGML)
        assertNextLine(BBKHYSBZ)
        assertNextLine(AJXFRGML)
        assertNextLine(UBWKQMQB)
        assertNextLine(FIAGBFDB)
        assertNextLine(SVJZYIZR)
        assertNextLine(FIAGBFDB)
        assertNextLine(SUSVSEOG)
        assertNextLine(FIAGBFDB)
        assertNextLine(QUMIKFKL)
        assertNextLine(FIAGBFDB)
        assertNextLine(OKZZZMTD)
        assertNextLine(FIAGBFDB)
        assertNextLine(KZTVGFKW)
        assertNextLine(FIAGBFDB)
        assertNextLine(SHABGYFL)
        assertNextLine(FIAGBFDB)
        assertNextLine(WGMEAEUB)
        assertNextLine(FIAGBFDB)
        assertNextLine(YBYJQOUU)
        assertNextLine(FIAGBFDB)
        assertNextLine(QXCQVHTK)
        assertNextLine(FIAGBFDB)
        assertNextLine(IMMMDGNH)
        assertNextLine(FIAGBFDB)
        assertNextLine(THDESVNL)
        assertNextLine(FIAGBFDB)
        assertNextLine(SMNODIHA)
        assertNextLine(FIAGBFDB)
        assertNextLine(ZGUONMRH)
        assertNextLine(FIAGBFDB)
        assertNextLine(DGDXSEHT)
        assertNextLine(VOZNSUST)
        assertNextLine(OYHLIGSF)
        assertNextLine(KOEHCERM)
        assertNextLine(LIFPRMBY)
        assertNextLine(KOEHCERM)
        assertNextLine(JBFNRODY)
        assertNextLine(TUGMEXST)
        assertNextLine(BBKHYSBZ)
        assertNextLine(AJXFRGML)
        assertNextLine(BBKHYSBZ)
        assertNextLine(AJXFRGML)
        assertNextLine(BBKHYSBZ)
        assertNextLine(AJXFRGML)
        assertNextLine(BBKHYSBZ)
        assertNextLine(AJXFRGML)
        assertNextLine(SEKOMOBU)
        assertNextLine(PDBPWPZN)
        assertNextLine(JVLHWTLL)
        assertNextLine(RUSTUDIC)
        assertNextLine(STJKUIIX)
        assertNextLine(UZEVILCU)
        assertNextLine(MJIILRXR)
        assertNextLine(PYIKNZOU)
        assertNextLine(LGOXKFDT)
        assertNextLine(LTSEFRLC)
    }
}
