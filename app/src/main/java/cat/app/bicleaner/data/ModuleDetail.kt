package cat.app.bicleaner.data

import cat.app.bicleaner.util.ModuleUtils

class ModuleDetail {

    val isModuleActivated: Boolean
        get() = ModuleUtils.isModuleActivated()

    val moduleVersionName: String
        get() = ModuleUtils.getModuleVersionName() ?: "null"

    val moduleVersionCode: Int = 1

}