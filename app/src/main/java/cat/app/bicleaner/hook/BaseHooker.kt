package cat.app.bicleaner.hook

abstract class BaseHooker(val mClassLoader: ClassLoader) {
    abstract fun hook()
    open fun lazyHook() {}
}