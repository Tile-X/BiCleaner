package cat.app.bicleaner.hook.handler

interface HookHandler {

    fun matchScope(packageName: String): Boolean

    fun handleHook(classloader: ClassLoader)

}