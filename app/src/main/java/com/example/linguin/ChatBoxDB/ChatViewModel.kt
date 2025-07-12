import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.linguin.ChatBoxDB.Reaction
import com.example.myapplication.RetroChat.ChatDatabase
import com.example.myapplication.RetroChat.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ChatDatabase.getInstance(application).chatDao()
    val messages: LiveData<List<ChatMessage>> = dao.getAllMessages()

    private val reactions: List<Reaction> = loadReactions(application)

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            val userMessage = ChatMessage(
                message = userInput,
                isUser = true,
                timestamp = System.currentTimeMillis()
            )
            dao.insertMessage(userMessage)

            val reply = findReply(userInput)
            val botMessage = ChatMessage(
                message = reply,
                isUser = false,
                timestamp = System.currentTimeMillis()
            )
            dao.insertMessage(botMessage)
        }
    }

    private fun findReply(input: String): String {
        val normalizedInput = input
            .lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .trim()

        Log.d("coolforyou", "Normalized user input: $normalizedInput")

        val match = reactions.firstOrNull { reaction ->
            val normalizedTrigger = reaction.trigger
                .lowercase()
                .replace(Regex("[^a-z0-9\\s]"), "")
                .trim()

            normalizedInput.contains(normalizedTrigger)
        }

        Log.d("coolforyou", "Matched trigger: ${match?.trigger ?: "None"}")

        return match?.reply ?: "Hmm... I don't have a reply for that. 🐧"
    }

    private fun loadReactions(application: Application): List<Reaction> {
        return try {
            val inputStream = application.assets.open("LinguinResponseJSON.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<Reaction>>() {}.type
            val reactions = Gson().fromJson<List<Reaction>>(reader, type)

            Log.d("coolforyou", "Loaded ${reactions.size} reactions")
            reactions.forEach {
                Log.d("coolforyou", "Trigger: '${it.trigger}', Reply: '${it.reply}'")
            }

            reactions
        } catch (e: Exception) {
            Log.e("coolforyou", "Error loading reactions", e)
            emptyList()
        }
    }
}

