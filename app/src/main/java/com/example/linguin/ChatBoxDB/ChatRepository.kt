import android.content.Context
import androidx.lifecycle.LiveData
import com.example.linguin.ChatBoxDB.Reaction
import com.example.myapplication.RetroChat.ChatDao
import com.example.myapplication.RetroChat.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatRepository(private val dao: ChatDao, context: Context) {

    private val triggers: List<Reaction> = loadTriggersFromJson(context)

    fun getMessages(): LiveData<List<ChatMessage>> = dao.getAllMessages()

    suspend fun insertMessage(message: ChatMessage) {
        dao.insertMessage(message)
    }

    fun simulateResponse(userMsg: ChatMessage): ChatMessage {
        val userText = userMsg.message.lowercase().replace(Regex("[^a-z0-9 ]"), "")

        val matched = triggers.firstOrNull { trigger ->
            userText.contains(trigger.trigger.lowercase())
        }

        val reply = matched?.reply ?: listOf(
            "Insert coin to continue... 🪙",
            "Back in my day, emojis were made of punctuation :)",
            "Where we're going, we don't need modern tech! 🛸"
        ).random()

        return ChatMessage(
            message = reply,
            isUser = false,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun loadTriggersFromJson(context: Context): List<Reaction> {
        return try {
            val json = context.assets.open("reactions.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Reaction>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
