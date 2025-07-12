package com.example.linguin

import android.annotation.SuppressLint
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.airbnb.lottie.LottieDrawable
import com.example.myapplication.AIpetApp.*
import com.example.myapplication.databinding.FragmentLiguinMainBinding
import com.example.virtualpet.data.PetDatabase
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class LiguinMainFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentLiguinMainBinding
    private lateinit var viewModel: PetViewModel
    private val args: LiguinMainFragmentArgs by navArgs()

    private var lastY: Float = 0f
    private val swipeThreshold = 100f

    private lateinit var tts: TextToSpeech
    private var ttsReady = false
    private var reactionToSpeak: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiguinMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = TextToSpeech(requireContext(), this)

        binding.petAnimationView.setAnimation("pet_animationreal.json")
        binding.petAnimationView.repeatCount = LottieDrawable.INFINITE
        binding.petAnimationView.playAnimation()

        val dao = PetDatabase.getInstance(requireContext()).petDao()
        val repository = PetRepository(dao)
        val factory = PetModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PetViewModel::class.java]

        viewModel.pet.observe(viewLifecycleOwner) { pet ->
            binding.happinessText.text = "Happiness: ${pet.happiness}"
            binding.hungerText.text = "Hunger: ${pet.hunger}"
            binding.sadnessText.text = "Sadness: ${pet.sad}"
        }

        binding.petAnimationView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastY = event.y
                    viewModel.increaseHunger(10)
                    viewModel.increaseSadness(10)
                    viewModel.decreaseHappiness(10)
                    binding.petAnimationView.playAnimation()
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.y - lastY
                    if (abs(deltaY) > swipeThreshold) {
                        viewModel.increaseHappiness(10)
                        lastY = event.y
                        binding.petAnimationView.playAnimation()
                    }
                    true
                }

                else -> false
            }
        }

        val message = args.ToyOrFoodData
        val reactionView = binding.petReaction

        val reaction = when (message) {
            "Arch Toy" -> "Ohhh sleek choice! Arch is bae "
            "Mac Toy" -> "Ew... I feel overpriced and sad. "
            "Tux Penguin" -> "YAY TUX! My best friend is here!! ️"
            "SudoSandwich" -> "Mmm... admin-level taste! "
            "Shell Fish" -> "Shellfish? More like shell-scripting snack! "
            "Apt Pie" -> "Tastes like fast installs and sweet dependencies! "
            "Windows Toy" -> "NOOO! I won't touch that! "
            "Top Ramen" -> "The meal of haxors... delicious!"
            else -> "Hello, I am Linguin. Created by Tanishq Pandey."
        }

        reactionToSpeak = reaction

        if (reaction.isNotBlank()) {
            reactionView.text = reaction
            reactionView.visibility = View.VISIBLE
            reactionView.isSelected = true
        } else {
            reactionView.visibility = View.GONE
        }

        setupPetWorker()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            tts.setSpeechRate(1.0f)
            tts.setPitch(1.0f)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(requireContext(), "TTS language not supported", Toast.LENGTH_SHORT).show()
                return
            }

            ttsReady = true
            reactionToSpeak?.let {
                Handler(Looper.getMainLooper()).postDelayed({
                    val speakResult = tts.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
                    Log.d("TTS", "Speak result: $speakResult")
                }, 500)
            }
        } else {
            Toast.makeText(requireContext(), "TTS initialization failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPetWorker() {
        val workRequest = PeriodicWorkRequestBuilder<PetWorkManager>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "pet_decay_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}
