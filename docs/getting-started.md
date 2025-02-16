# Getting Started

This guide will help you set up and run the project on your local machine.

## Quick Start

1. Clone the repository (with depth 1 to reduce clone size):

	```bash
	git clone --depth 1 -b main https://github.com/atick-faisal/Jetpack-Android-Starter.git
	```

2. Open the project in Android Studio Hedgehog or newer

3. Run the debug build variant:

	```bash
	./gradlew assembleDebug
	```

> [!NOTE]
> The debug variant should work out of the box with the template `google-services.json` file.
> However, Firebase features like authentication and Firestore won't be functional until you set up
> your own Firebase project.

## Prerequisites

- Android Studio Hedgehog or newer
- JDK 21
- An Android device or emulator running API 24 (Android 7.0) or higher

## Setting Up Firebase Features

To use Firebase authentication, Firestore, and analytics:

1. Follow our [Firebase Setup Guide](firebase.md) to:
	- Create your Firebase project
	- Configure Authentication
	- Set up Firestore
	- Get your `google-services.json`

2. Before replacing the template `google-services.json`, prevent Git from tracking changes:

	```bash
	git update-index --skip-worktree app/google-services.json
	```

3. Replace the template file at `app/google-services.json` with your own

## Release Build Setup

To create release builds, you'll need to set up signing:

1. Create a keystore file
2. Create `keystore.properties` in the project root:

	```properties
	storePassword=your-store-password
	keyPassword=your-key-password
	keyAlias=your-key-alias
	storeFile=your-keystore-file.jks
	```

3. Place your keystore file in the `app/` directory
4. Build the release variant:

    ```bash
    ./gradlew assembleRelease
    ```

> [!TIP]
> Use Android Studio's "Generate Signed Bundle/APK" tool to help create your keystore if you
> don't
> have one.

## Next Steps

1. **Understand the Architecture**: Read our [Architecture Overview](architecture.md) to understand
   how the app is structured

2. **Setup CI/CD**: Follow the [GitHub CI/CD Guide](github.md) to set up automation

3. **Code Style**: Review the [Spotless Setup](spotless.md) for code formatting guidelines

## Common Issues

1. **Build Fails**:
	- Ensure you have JDK 21 set in Android Studio
	- Run `./gradlew clean` and try again
	- Check if all dependencies are resolved

2. **Firebase Features Not Working**:
	- Verify you've replaced `google-services.json`
	- Check Firebase Console for proper setup
	- Ensure SHA-1 is added for authentication

3. **Release Build Fails**:
	- Verify keystore.properties exists and has correct values
	- Confirm keystore file is in the correct location
	- Check signing configuration in build.gradle

> [!IMPORTANT]
> Never commit sensitive files like `keystore.properties`, your keystore file, or your real
`google-services.json` to version control.

## IDE Setup

For the best development experience:

1. **Enable Compose Preview**:
	- Ensure "Live Edit of Literals" is enabled
	- Configure appropriate preview devices

2. **Run Configurations**:
	- Use provided run configurations for common tasks
	- Signing Report configuration helps get SHA-1 for Firebase

3. **Code Style**:
	- Import the project's `.editorconfig`
	- Enable "Format on Save" for Kotlin files
	- Use the Spotless plugin for consistent formatting

## Further Reading
- [Firebase Setup Guide](firebase.md): Learn how to set up Firebase features in your project
- [Dependency Management](dependency.md): Understand how dependencies are managed in the project
- [Architecture Overview](architecture.md): Learn about the app's architecture
- [Design Philosophy](philosophy.md): Understand the design principles behind the architecture
- [Adding New Features](guide.md): Learn how to add new features to the project
- [Convention Plugins](plugins.md): Learn about custom Gradle plugins used in the project
- [Performance Optimization](performance.md): Optimize the app for speed and efficiency
- [Useful Tips & Tricks](tips.md): Get useful tips for development and debugging
- [Publishing to Play Store](fastlane.md): Learn how to publish your app to the Google Play Store
