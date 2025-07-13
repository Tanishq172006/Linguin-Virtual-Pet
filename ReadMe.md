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
