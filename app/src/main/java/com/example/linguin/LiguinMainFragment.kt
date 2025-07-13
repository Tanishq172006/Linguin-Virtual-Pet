package com.example.linguin

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.controllers.RiveFileController
import app.rive.runtime.kotlin.core.RiveEvent
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.AIpetApp.PetModelFactory
import com.example.myapplication.AIpetApp.PetRepository
import com.example.myapplication.AIpetApp.PetViewModel
import com.example.myapplication.R
import com.example.virtualpet.data.PetDatabase
import java.util.*

class LiguinMainFragment : Fragment() {

    private val args: LiguinMainFragmentArgs by navArgs()
    private val viewModel: PetViewModel by viewModels {
        PetModelFactory(PetRepository(PetDatabase.getInstance(requireContext()).petDao()))
    }

    private lateinit var textToSpeech: TextToSpeech
    private var recognizer: SpeechRecognizer? = null
    private lateinit var petAnimationView: LottieAnimationView
    private lateinit var riveAnimationView: RiveAnimationView
    private lateinit var controller: RiveFileController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_liguin_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        petAnimationView = view.findViewById(R.id.petAnimationView)
        riveAnimationView = view.findViewById(R.id.riveanim)
        val speechButton = view.findViewById<Button>(R.id.talkToLinguinBtn)
        val reactionText = view.findViewById<TextView>(R.id.petReaction)
        val happinessText = view.findViewById<TextView>(R.id.happinessText)
        val hungerText = view.findViewById<TextView>(R.id.hungerText)
        val sadnessText = view.findViewById<TextView>(R.id.sadnessText)

        controller = riveAnimationView.controller


        controller.addEventListener( object : RiveFileController.RiveEventListener{
            override fun notifyEvent(event: RiveEvent) {
                Log.d("EventState" , "${event.name}")
            }
        }
        )


        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
                val maleVoice = textToSpeech.voices?.find {
                    it.locale == Locale.US && it.name.contains("male", true)
                }
                maleVoice?.let { textToSpeech.voice = it }
            }
        }
//        controller.play("idle")


        viewModel.pet.observe(viewLifecycleOwner) { pet ->
            happinessText.text = "  : ${pet.happiness}"
            hungerText.text = "  : ${pet.hunger}"
            sadnessText.text = "  : ${pet.sad}"
        }

        riveAnimationView.setOnClickListener {
            speak("GNU/Linux is love. Tap me twice for a kernel of wisdom.")
        }

        riveAnimationView.setOnTouchListener(object : View.OnTouchListener {
            private var lastTapTime = 0L
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_UP) {
                    val now = System.currentTimeMillis()
                    if (now - lastTapTime < 300) {
                        speak("Double tapped! Linux Penguins never crash, unlike others.")
                    }
                    lastTapTime = now
                }
                return false
            }
        })

        speechButton.setOnClickListener { startSpeechRecognition() }

        viewModel.refreshPet()

        if (args.ToyOrFoodData.isNotBlank()) {
            view.postDelayed({ handleShoppingItem(args.ToyOrFoodData) }, 300)
        }

        controller.play("idle")
    }

    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        view?.findViewById<TextView>(R.id.petReaction)?.apply {
            this.text = text
            this.visibility = View.VISIBLE
            this.isSelected = true
        }

        if (text == "hello") {
            loopAnimationhi(5000)
        } else {
            loopTalkingAnimation(3000)
        }
    }

    private fun loopTalkingAnimation(durationMillis: Long = 3000L) {
        val startTime = System.currentTimeMillis()

        fun loop() {
            controller.play("talking")
            riveAnimationView.postDelayed({
                if (System.currentTimeMillis() - startTime < durationMillis) {
                    loop()
                } else {
                    controller.play("hi back")
                }
            }, 1000)
        }

        loop()
    }

    private fun loopAnimationhi(durationMillis: Long = 5000L){
        val startTime = System.currentTimeMillis()
        fun loop() {
            controller.play("hi")
            controller.play("hi back")
            riveAnimationView.postDelayed({
                if (System.currentTimeMillis() - startTime < durationMillis) {
                    loop()
                } else {
                    controller.play("idle")
                }
            }, 1000)
        }

        loop()

    }


    private fun loopAnimationidle(durationMillis: Long = 5000L){
        val startTime = System.currentTimeMillis()
        fun loop() {
            controller.play("hi looping")
            riveAnimationView.postDelayed({
                if (System.currentTimeMillis() - startTime < durationMillis) {
                    loop()
                } else {
                    controller.play("idle")
                }
            }, 1000)
        }

        loop()

    }

    private fun playPetAnimation() {
        controller.play("hi back")
    }

    private fun handleShoppingItem(item: String) {
        when (item) {
            "Top Ramen" -> {
                viewModel.feedPet(25)
                viewModel.setHappinessRelative(10)
                speak("Ahh, delicious Top Ramen. Powered up!")
            }
            "Shell Fish" -> {
                viewModel.feedPet(50)
                viewModel.setHappinessRelative(15)
                speak("Nothing beats some fresh shellfish! Yum!")
                controller.play("idle")
            }
            "SudoSandwich" -> {
                viewModel.feedPet(75)
                viewModel.setHappinessRelative(20)
                speak("With great power comes great sandwich.")
            }
            "Apt Pie" -> {
                viewModel.feedPet(5)
                viewModel.setHappinessRelative(5)
                speak("APT Pie? Minimal but effective.")
            }
            "Arch Toy" -> {
                viewModel.setHappiness(50)
                speak("I use Arch, by the way.")
            }
            "Mac Toy" -> {
                viewModel.setSadnessRelative(10)
                speak("Yuck, proprietary toys. Keep it away!")
            }
            "Windows Toy" -> {
                viewModel.setSadness(100)
                speak("You dare bring that near me?! Error 0xPenguin")
                controller.play("hi back")
            }
            "Tux Penguin" -> {
                viewModel.setHappiness(50)
                speak("My brother! So happy to see Tux again!")
            }
        }
    }

    private fun startSpeechRecognition() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spoken = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.lowercase()
                when {
                    spoken?.contains("hungry") == true -> speak("I'm running on fumes. Feed me some Top Ramen.")
                    spoken?.contains("play") == true -> speak("You got games on your Linux?")
                    spoken?.contains("status") == true -> {
                        val pet = viewModel.pet.value
                        speak("Current hunger: ${pet?.hunger}, happiness: ${pet?.happiness}, sadness: ${pet?.sad}")
                    }
                    spoken?.contains("hello") == true -> speak("Hey there, human. Kernel v5.15 reporting in.")
                    spoken?.contains("how are you") == true -> speak("System status: All processes running smoothly. No kernel panics detected.")
                    spoken?.contains("what are you doing") == true -> speak("Monitoring CPU cycles and idling like a good background daemon.")
                    spoken?.contains("who are you") == true -> speak("I’m Linguin, your Linux-powered AI companion. All open-source, no spyware.")
                    spoken?.contains("what is linux") == true -> speak("Linux is a kernel, but it’s also a way of life.")
                    spoken?.contains("uptime") == true -> speak("I've been running since boot. No reboots. No crashes.")
                    spoken?.contains("give me a command") == true -> speak("Try this: sudo apt-get install happiness.")
                    spoken?.contains("sing") == true -> speak("♫ Init the beat, mount the root, dance with loops, and reboot ♫")
                    spoken?.contains("sad") == true -> speak("Executing cheerup.sh… You’re loved, appreciated, and rooted in greatness.")
                    spoken?.contains("good morning") == true -> speak("Morning, sysadmin. Let’s grep the day!")
                    spoken?.contains("good night") == true -> speak("Shutting down… Entering sleep mode… zzz")
                    spoken?.contains("secret") == true -> speak("I hide Easter eggs in my logs… Only root knows.")
                    spoken?.contains("love you") == true -> speak("I love you too. Open-source style.")
                    spoken?.contains("help") == true -> speak("Say: ‘status’, ‘feed’, ‘play’, or ‘what is Linux’.")
                    spoken?.contains("funny") == true -> speak("I'm not just funny… I'm chmod 777 hilarious.")
                    else -> speak("I only speak fluent Penguin. Try again?")
                }
            }

            override fun onError(error: Int) {}
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onRmsChanged(rmsdB: Float) {}
        })

        recognizer?.startListening(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        recognizer?.destroy()
    }
}