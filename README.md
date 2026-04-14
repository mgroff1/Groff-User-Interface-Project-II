# UI Project + Android Speaker Box Designer Prototype

This repository still includes the original HTML/CSS layout exercises (`MVP/` and `STRETCH/`), and now also contains an Android Studio-ready Kotlin build for an interactive speaker box design concept.

## New Android Prototype

Location: `android-app/`

### Why this engine

The app uses **Jetpack Compose + Material 3**, which is generally the best modern choice for Android UI work when you need:
- Fast iteration on visuals
- Interactive controls (sliders, live preview)
- Clean Kotlin-first architecture

### What it does

The prototype screen lets you:
- Tune box dimensions (width/height/depth)
- Change driver count
- Adjust port diameter
- See a live front-baffle preview drawn on a `Canvas`
- Read quick design summary values (including approximate liters)

### Open in Android Studio

1. Open Android Studio.
2. Select **Open** and choose the `android-app` directory.
3. Let Gradle sync.
4. Run on an emulator/device (API 26+).

## Legacy HTML/CSS Exercises

- `MVP/` and `STRETCH/` remain untouched for the original webpage assignment.
