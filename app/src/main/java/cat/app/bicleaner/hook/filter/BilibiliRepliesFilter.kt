package cat.app.bicleaner.hook.filter

import cat.app.bicleaner.hook.config.ModuleConfig
import cat.app.bicleaner.hook.util.call

class BilibiliRepliesFilter: ResultFilter {

    private val replyFilterRules = ModuleConfig.rules

    override fun applyFilter(result: Any?): Any? {
        if (ModuleConfig.moduleEnable) return remakeReplies(result as List<*>)
        return result
    }

    private fun checkFilterByLevel(reply: Any?): Boolean {
        if (!ModuleConfig.filterByLevel) return true
        val member = reply.call("getMember")
        val level = member.call("getLevel") as Long
        return level >= ModuleConfig.requiredLevel
    }

    private fun checkFilterByRules(reply: Any?): Boolean {
        if (!ModuleConfig.filterByRules) return true
        val content = reply.call("getContent")
        val message = content.call("getMessage") as String
        replyFilterRules.forEach {
            if (message.contains(it)) return false
        }
        return true
    }

    private fun flagReply(reply: Any?) {
        val content = reply.call("getContent")
        content.call("setMessage", "该消息已被过滤")
        content.call("clearPictures")
        reply.call("clearReplies")
        reply.call("clearCount")
    }

    private fun remakeReplies(replies: List<*>): List<*> {
        if (ModuleConfig.showFilter) {
            replies.forEach {
                if (!(checkFilterByLevel(it) && checkFilterByRules(it))) flagReply(it)
            }
            return replies
        }
        return replies.filter { checkFilterByLevel(it) && checkFilterByRules(it) }
    }

}