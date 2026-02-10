# Games Scoring App

A sleek, high-performance **Compose Multiplatform** mobile application designed for seamless scorekeeping and game management on both Android and iOS.

## ✨ Features

### 🎮 Game Management
*   **Custom Set-Up**: Configure games with specific types, custom player names, and distinct UI color themes.
*   **Persistent Sessions**: All game progress is automatically saved using a Room database and can be resumed from the "Saved Games" screen.
*   **Dynamic Scoring**: Intuitive interfaces tailored to different game types to track points effortlessly.

### 🛠️ Integrated Utilities
*   **Advanced Random Number Generator**:
    *   Generates values within a custom range (up to ±100,000).
    *   **Right-to-Left Animation**: Numbers reveal digit-by-digit from right to left, creating a professional "counter" effect.
    *   **Zero-Padding**: Automatically pads numbers with leading zeros based on the maximum limit (e.g., `42` becomes `000042`) for a consistent layout.
    *   **Monospaced Display**: Uses `Roboto Mono` to ensure visual stability during animations.

### 🎨 Modern UI/UX
*   **Dark Mode Aesthetic**: A clean, high-contrast dark theme using `League Gothic` and `Roboto Condensed` fonts.
*   **Haptic Feedback**: Integrated tactile responses for a more physical feel during interaction.
*   **Smooth Navigation**: Powered by the Compose Navigation component for fluid screen transitions.

## 🛠️ Technology Stack

*   **Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Shared UI for Android & iOS).
*   **Language**: Kotlin.
*   **Database**: Room (SQL) with multiplatform coroutine-based initialization.
*   **Architecture**:
    *   **State Management**: `remember`, `mutableStateOf`, and `collectAsState` for reactive UI updates.
    *   **Resources**: Centralized resource management via the `Res` object (Generated Resource system).
    *   **Navigation**: Type-safe routing using `NavHost` and `navArgument`.

## 🚀 Getting Started

### Prerequisites
*   **Android Studio** (Latest version recommended).
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

*   `commonMain`: The core logic. Contains the `App.kt` entry point, database entities, navigation logic, and the shared Compose UI.
*   `composeResources`: Shared assets including:
    *   `font/`: Custom TTF fonts (`robotomono.ttf`, `leaguegothic.ttf`).
    *   `drawable/`: App icons and logos.
*   `androidMain`: Android-specific platform configuration.
*   `iosMain`: iOS-specific platform configuration and UIKit integration.

## 📜 License
This project is for personal use and portfolio demonstration.
