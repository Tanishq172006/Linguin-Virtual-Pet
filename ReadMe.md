# 🐧 Linguin – The Linux-Themed AI Virtual Pet App

Linguin is your open-source, animated AI companion that lives on your Android device. Inspired by the spirit of Linux, Linguin is fun, reactive, and educational — complete with voice interaction, pet stats, Rive animations, and tech-themed humor.

---

## ✨ Features



- 🐧 **Well-Driven Reactions**  
  Linguin reacts uniquely to food, toys, and speech prompts using pre-defined reactions or Backend base-responses.

- 🎙️ **Text-to-Speech & Voice Recognition**  
  Talk to Linguin, and he'll respond with witty Linux-based remarks in a human male voice.

- 📉 **Pet Stat Simulation**  
  Track Linguin’s happiness, hunger, and sadness levels in real-time. Neglect him, and he’ll let you know!

- ⏰ **Background Simulation (WorkManager)**  
  Even when you’re away, Linguin’s behavior evolves over time—just like a real digital pet.

- 🎞️ **Dynamic Animations (Rive + Lottie)**  
  Stunning animations bring Linguin to life, reacting to actions like feeding or chatting.

- 🛍️ **Item Shop Integration**  
  Give Linguin toys and food (like Top Ramen or the dreaded Windows Toy) — he’ll love or hate them accordingly!

- 🔐 **Offline Mode with Local Database (Room)**  
  All pet states are stored offline, keeping your data safe and accessible without internet.


  

---

## 📦 Tech Stack

- **Kotlin + MVVM Architecture**
- **Jetpack Libraries** (Room, WorkManager, Navigation with Safe Args)
- **Rive Animation Engine**
- **Text-to-Speech + SpeechRecognizer**
- **Retrofit (for optional AI API integration)**
- **Version Catalog with Gradle Kotlin DSL**
# 🐧 Linguin – Your Linux-Themed Virtual Pet

Linguin is a virtual pet powered by open-source charm and geeky humor. Designed for Android, Linguin reacts to your voice, taps, and items like Top Ramen and Arch toys. Built with Kotlin, Rive animations, Text-to-Speech, and speech recognition.

---

## 🎤 Voice Commands Feature (Speech Recognition)

Linguin responds to your spoken prompts using Android’s Speech Recognition + Text-to-Speech (TTS). You can **talk to your pet**, and Linguin will speak back in a fun, Linux-themed tone.

### ✅ Example Voice Prompts

| You Say...             | Linguin Responds...                                                                 |
|------------------------|-------------------------------------------------------------------------------------|
| `hungry`               | "I'm running on fumes. Feed me some Top Ramen."                                    |
| `play`                 | "You got games on your Linux?"                                                     |
| `status`              | "Current hunger: 45, happiness: 80, sadness: 10" *(based on actual pet stats)*     |
| `hello`                | "Hey there, human. Kernel v5.15 reporting in."                                     |
| `how are you`          | "System status: All processes running smoothly. No kernel panics detected."       |
| `what are you doing`   | "Monitoring CPU cycles and idling like a good background daemon."                 |
| `who are you`          | "I’m Linguin, your Linux-powered AI companion. All open-source, no spyware."      |
| `what is linux`        | "Linux is a kernel, but it’s also a way of life."                                  |
| `uptime`               | "I've been running since boot. No reboots. No crashes."                            |
| `give me a command`    | "Try this: sudo apt-get install happiness."                                        |
| `sing`                 | "♫ Init the beat, mount the root, dance with loops, and reboot ♫"                 |
| `sad`                  | "Executing cheerup.sh… You’re loved, appreciated, and rooted in greatness."       |
| `good morning`         | "Morning, sysadmin. Let’s grep the day!"                                           |
| `good night`           | "Shutting down… Entering sleep mode… zzz"                                          |
| `secret`               | "I hide Easter eggs in my logs… Only root knows."                                  |
| `love you`             | "I love you too. Open-source style."                                               |
| `help`                 | "Say: ‘status’, ‘feed’, ‘play’, or ‘what is Linux’."                               |
| `funny`                | "I'm not just funny… I'm chmod 777 hilarious."                                     |
| _Anything else?_       | "I only speak fluent Penguin. Try again?"                                          |

---

## 📦 Tech Stack

- **Kotlin** – Modern Android development
- **SpeechRecognizer** – For live voice command input
- **TextToSpeech (TTS)** – To speak Linguin’s responses
- **Room Database** – To store pet stats (hunger, happiness, sadness)
- **WorkManager** – To simulate realistic idle behavior over time
- **Rive Animations** – Lottie-like animations for cute pet reactions
- **Safe Args Navigation** – For passing item data like food or toys
- **MVVM Architecture** – Clean separation of UI and logic

---

## 🧪 Triggering Speech Commands in Code

Speech recognition checks phrases like this:

```kotlin
when {
    spoken?.contains("hungry") == true -> speak("I'm running on fumes. Feed me some Top Ramen.")
    spoken?.contains("status") == true -> {
        val pet = viewModel.pet.value
        speak("Current hunger: ${pet?.hunger}, happiness: ${pet?.happiness}, sadness: ${pet?.sad}")
    }
    spoken?.contains("what is linux") == true -> speak("Linux is a kernel, but it’s also a way of life.")
    // ...more commands
    else -> speak("I only speak fluent Penguin. Try again?")
}
- 

---

## 🚀 Getting Started

### Requirements

- Android Studio Koala or newer
- Min SDK: 24 (Android 7.0)
- Internet (for OpenAI-based features, optional)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/linguin-pet-app.git
   cd linguin-pet-app
