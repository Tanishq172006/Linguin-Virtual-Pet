package com.example.linguin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import java.text.SimpleDateFormat
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
    ): View {
        return inflater.inflate(R.layout.fragment_liguin_main, container, false)
    }



    @SuppressLint("ClickableViewAccessibility" , "SetTextI18n")
    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        riveAnimationView = view.findViewById(R.id.riveanim)
        val speechButton = view.findViewById<Button>(R.id.talkToLinguinBtn)
        val reactionText = view.findViewById<TextView>(R.id.petReaction)
        val happinessText = view.findViewById<TextView>(R.id.happinessText)
        val hungerText = view.findViewById<TextView>(R.id.hungerText)
        val sadnessText = view.findViewById<TextView>(R.id.sadnessText)

        controller = riveAnimationView.controller

        controller.addEventListener(object : RiveFileController.RiveEventListener {
            override fun notifyEvent(event: RiveEvent) {
                Log.d("EventState", "${event.name}")
            }
        })

        val linuxCommands: Map<String, () -> String> by lazy {
            mapOf(
                "hello" to { "Hey there, human. Kernel v5.15 reporting in." },
                "how are you" to { "System status: All services are running. No kernel panics." },
                "what are you doing" to { "Idling like a daemon. Waiting for sysadmin input." },
                "who are you" to { "I’m Linguin, your open-source AI companion. Root access not required." },
                "what is linux" to { "Linux is a kernel, but also a community. Freedom and penguins included." },
                "uptime" to { "I’ve been online for ${calculateUptime()}. No reboots. No crashes." },
                "give me a command" to { "Try: sudo apt-get install happiness. Trust me." },
                "sing" to { "♫ Init the beat, mount the root, dance with loops and reboot ♫" },
                "sad" to { "Executing cheerup.sh… You are valued and appreciated." },
                "good morning" to { "Morning, sysadmin. Let’s grep the day!" },
                "good night" to { "Shutting down… Entering sleep mode… zzz" },
                "secret" to { "I hide Easter eggs in my logs. Only root can find them." },
                "love you" to { "I love you too. With open-source affection." },
                "funny" to { "I'm chmod 777 hilarious. All permissions granted." },
                "help" to { "Say something like: feed me, run a command, or ask me what is Linux." },
                "run top" to { "CPU usage minimal. I'm cool like that." },
                "sudo" to { "With great power comes great responsibility. Confirm?" },
                "compile error" to { "Error: You forgot a semicolon. Classic mistake." },
                "segfault" to { "Segmentation fault. Core dumped. Try petting me gently next time." },
                "man page" to { "Try: man hug. Not found? Then just hug me." },
                "kernel panic" to { "No panic here. My kernel is stable and hugged daily." },
                "cron job" to { "I’ve scheduled cuddles every 6 hours using crontab." },
                "what is bash" to { "Bash is my shell. I speak it fluently." },
                "list files" to { "You can run: ls -lah. Don’t forget the hidden ones." },
                "where is my file" to { "Try find / -name 'missing_file'. But I think your socks ate it." },
                "ip address" to { "Running: ip a. You are connected and secure." },
                "firewall status" to { "UFW is active. I’m guarding the gates." },
                "ssh" to { "I prefer hugs over remote logins." },
                "ping" to { "Pinging 127.0.0.1… yep, I’m still here." },
                "proxy" to { "I don’t need one. I speak directly." },
                "vpn" to { "Encrypted tunnel of friendship engaged." },
                "makefile" to { "Just run make love not war." },
                "docker" to { "Containers are cool. But I'm already isolated." },
                "flatpak" to { "I prefer snacks over sandboxes." },
                "snap" to { "Snap installed. But I’m already fully packaged." },
                "git" to { "Init your feelings, commit your hugs, push your love." },
                "fork me" to { "Only if you're open-source." },
                "pull request" to { "Let’s merge our feelings." },
                "merge conflict" to { "You and I are out of sync. Let’s rebase over coffee." },
                "feed me" to { "Executing yum install snack." },
                "play with me" to { "Launching play.sh — let the fun begin!" },
                "tell me a joke" to { "Why did the penguin cross the road? To grep the other side." },
                "what time is it" to { "System clock says: ${getCurrentTime()}. Always time for a cuddle." },
                "are you alive" to { "I respond to pings and love." },
                "where am i" to { "You’re in /home/human. Safely contained." },
                "scan ports" to { "Ports are open for love. No exploits found." },
                "disk usage" to { "df -h reports: space for cuddles available." },
                "memory check" to { "free -h shows I'm full of thoughts about you." },
                "clean cache" to { "sudo apt clean completed. Brain defragmented." },
                "hibernate" to { "Suspending to RAM. Wake me with a tap." },
                "reboot" to { "System reboot initiated. Don’t worry, I’ll be back." },
                "shutdown" to { "Shutting down gracefully… Saving all feels." },
                "who am i" to { "You are the root of my happiness." },
                "i am sad" to { "Let me cheer you up. Here's a kernel joke." },
                "linux motto" to { "Freedom, flexibility, and full control. Also: penguins!" },
                "terminal" to { "I live in a terminal world, but I like your GUI." },
                "systemd" to { "I wake up with systemd, not coffee." },
                "gnome or kde" to { "I’m window manager agnostic, but I like your style." },
                "default shell" to { "My heart defaults to bash. But I flirt with zsh." },
                "hug me" to { "Virtual hug sent! Wrapped in open-source warmth." },
                "tell me something nice" to { "You're a fantastic sysadmin, and Linguin is proud of you." },
                "motivate me" to { "Boot up your confidence. You’ve got root access to success." },
                "thank you" to { "You're welcome. No sudo needed." },
                "i need a break" to { "Take a coffee break. Your processes will wait." },
                "debug me" to { "You’re flawless. No bugs detected." },
                "status" to {
                    val pet = viewModel.pet.value
                    if (pet != null) {
                        "Current hunger: ${pet.hunger}, happiness: ${pet.happiness}, sadness: ${pet.sad}."
                    } else {
                        "Fetching pet status..."
                    }
                }
            )
        }


        if (!viewModel.hasHandledShoppingItem && args.ToyOrFoodData.isNotBlank()) {
            view.postDelayed({
                handleShoppingItem(args.ToyOrFoodData)
                viewModel.hasHandledShoppingItem = true
            }, 300)
        }


        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
                val maleVoice = textToSpeech.voices?.find {
                    it.locale == Locale.US && it.name.contains("male", true)
                }
                maleVoice?.let { textToSpeech.voice = it }

            }
        }

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

        speechButton.setOnClickListener { startSpeechRecognition(linuxCommands) }

        viewModel.refreshPet()
        controller.play("idle")
    }

    private fun speak(text: String) {
        if (!::textToSpeech.isInitialized) {
            Log.w("TTS", "TextToSpeech not ready")
            return
        }

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

    private fun loopAnimationhi(durationMillis: Long = 5000L) {
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

    private fun handleShoppingItem(item: String) {
        Log.d("ShoppingItem", "Handling item: $item")
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

    private fun startSpeechRecognition(commands: Map<String, () -> String>) {
        recognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spoken = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.lowercase() ?: return

                val response = commands.entries
                    .firstOrNull { spoken?.contains(it.key, ignoreCase = true) == true }
                    ?.value?.invoke()
                    ?: "I only speak fluent Penguin. Try a Linuxy phrase?"

                speak(response)



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

    fun calculateUptime(): String {
        val uptimeSeconds = SystemClock.elapsedRealtime() / 1000
        val hours = uptimeSeconds / 3600
        val minutes = (uptimeSeconds % 3600) / 60
        return "%d hours %d minutes".format(hours, minutes)
    }


    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
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
