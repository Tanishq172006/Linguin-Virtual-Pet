import com.example.linguin.OpenAiAPI.OpenAIApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: OpenAIApi by lazy {
        retrofit.create(OpenAIApi::class.java)
    }
}
