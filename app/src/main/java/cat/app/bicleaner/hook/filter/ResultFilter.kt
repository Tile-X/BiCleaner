package cat.app.bicleaner.hook.filter

interface ResultFilter {

    fun applyFilter(result: Any?): Any?

}