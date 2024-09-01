package cat.app.bicleaner.hook

interface HookHandler {

    fun getScopePackage(): String

    fun handleHook(classloader: ClassLoader)

}