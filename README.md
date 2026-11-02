# SONI CARD v1.0

SONI CARD is an original Tajik-first Android digital-wallet **UI demonstration** built with Kotlin, Jetpack Compose and Material 3. It runs offline with sample data. It is not a bank, payment service, stored-value wallet, or connection to any financial institution.

## What's included

- Premium light interface, SONI CARD branding and virtual masked-card visual
- 28 navigation destinations covering onboarding, authentication UI, dashboard, cards, transfers, payments, QR, history, statistics, notifications, profile, security, settings, language, support and about
- Tajik, Russian and English UI text map; language selection updates the current UI
- Local sample transactions, search and transaction filters
- Demo freeze/unfreeze card control and hide/show balance
- DataStore preference storage foundation, biometric capability dependency and input validation
- Unit tests and GitHub Actions APK workflow

## Safety notice

All balances, card details and transactions are fictional demo data. No real card number, CVV, bank password, SMS code or real PIN is requested. Transfer, top-up, withdrawal, payment and QR operations do not contact a bank and must not be used for real money. A production financial service requires licensed partners, authorized APIs, a secure backend, compliance review and independent security testing.

## Beginner setup

1. Download `SONI-CARD-V1.0.zip` from the link shared with this project and extract it.
2. Open GitHub and create a new repository named `SONI-CARD`.
3. Upload the extracted project contents (the `.github` folder must be included) to the repository's `main` branch. Alternatively, use Git from a terminal: `git init`, `git add .`, `git commit -m "Initial SONI CARD project"`, then add your GitHub remote and push `main`.
4. Open the repository's **Actions** tab. The workflow starts automatically after a push to `main`; you can also select **Android CI** and choose **Run workflow**.
5. Wait for the green successful workflow run. It runs unit tests and builds the debug APK.
6. Open the completed workflow run, scroll to **Artifacts**, and download **SONI-CARD-APK**.
7. Extract the artifact ZIP. Install `app-debug.apk` on an Android device. Android may ask you to allow installation from that source. Only install builds you trust.
8. To update the app, edit the project, commit and push to `main`; GitHub Actions will build a new APK.

## Build locally

Use Android Studio with JDK 17 and Android SDK Platform 35 installed. Open this folder, allow Gradle sync, then run:

```bash
./gradlew testDebugUnitTest --no-daemon
./gradlew assembleDebug --no-daemon --stacktrace
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

The included `gradlew` is a small self-bootstrapping launcher that downloads pinned Gradle 8.11.1 on first use. It intentionally avoids checking in a binary wrapper JAR; network access is required on first invocation. The GitHub workflow uses the same launcher.

## Project details

- Application ID: `tj.soni.card` (debug APK application ID has `.debug` suffix)
- Minimum SDK: 26
- Compile/target SDK: 35
- Kotlin: 2.0.21
- Android Gradle Plugin: 8.9.2
- Gradle distribution: 8.11.1
- UI: Jetpack Compose + Material 3

## License

MIT. See [LICENSE](LICENSE).
