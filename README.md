# Games Scoring App

A sleek, high-performance **Compose Multiplatform** mobile application built to streamline scorekeeping and game management across Android and iOS.

## ✨ Features

### 🏆 Diverse Scoreboards
Tailored interfaces for various game styles and competitive formats:
*   **Truco**: Specialized point tracking for the popular card game.
*   **Generala**: A dedicated dice game scorecard.
*   **Points-Based**: Standard numeric tracking for classic board games.
*   **Levels**: Track progress through stages or tiers.
*   **Ranking**: Real-time leaderboards to see who is currently in the lead.

### 🛠️ Game Utilities
A complete toolkit of essential gaming helpers:
*   **Random Number Generator**: Features a professional right-to-left "odometer" animation, monospaced digits, and automatic zero-padding (up to ±100,000).
*   **Roll Dice**: Quickly simulate dice throws for any tabletop game.
*   **Coin Toss**: A quick binary decision maker.
*   **Player Picker**: Randomly select which player starts or who is "it."
*   **Timer**: Built-in countdown and stopwatch functionality for timed rounds.

### 🎮 Game Management
*   **Custom Set-Up**: Configure games with specific types, custom player names, and distinct UI color themes.
*   **Persistent Sessions**: All game progress is automatically saved using a Room database and can be resumed at any time from the "Saved Games" screen.

## 🛠️ Technology Stack

*   **Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Shared UI for Android & iOS).
*   **Language**: Kotlin.
*   **Database**: Room (SQL) with multiplatform coroutine-based initialization.
*   **Navigation**: Type-safe routing using `NavHost` and `rememberNavController`.
*   **Architecture**:
    *   **Async Processing**: Kotlin Coroutines for non-blocking database operations.
    *   **State Management**: Reactive UI updates using `collectAsState` and `mutableStateOf`.

## 🚀 Getting Started

### Prerequisites
*   **Android Studio** (Latest version).
*   **Kotlin Multiplatform** plugin.
*   **Xcode** (Required for iOS builds).

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/games-scoring-app.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync and generate the `Res` (Resource) classes.
4. Run the `composeApp` configuration for Android or the `iosApp` configuration for iOS.

## 📂 Project Structure

*   `commonMain`: The heart of the app.
    *   `App.kt`: Central entry point and `NavHost` configuration.
    *   `Data/`: Room database implementation and entities.
    *   `Pages/`: UI screens for Game, Setup, Utilities, and Saved Games.
    *   `Theme/`: Visual styles and custom font definitions (League Gothic, Roboto Mono).
*   `composeResources`: Shared assets including:
    *   `font/`: Custom TTF files for branding and animated numbers.
    *   `drawable/`: Vector logos and utility icons.

## 📜 License
This project is for personal use and portfolio demonstration.
