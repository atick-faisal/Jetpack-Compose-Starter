![Jetpack Logo](https://github.com/atick-faisal/Jetpack-Compose-Starter/assets/38709932/6d8f68ad-3045-4736-99ed-86c1593f1241)

<p align="center">
    <a href="https://github.com/atick-faisal/Jetpack-Compose-Starter/releases"><img src="https://img.shields.io/github/release/atick-faisal/Jetpack-Compose-Starter?colorA=363a4f&colorB=b7bdf8&style=for-the-badge"></a>
    <a href="https://github.com/atick-faisal/Jetpack-Compose-Starter/issues"><img src="https://img.shields.io/github/issues/atick-faisal/Jetpack-Compose-Starter?colorA=363a4f&colorB=f5a97f&style=for-the-badge"></a>
    <a href="https://github.com/atick-faisal/Jetpack-Compose-Starter/contributors"><img src="https://img.shields.io/github/contributors/atick-faisal/Jetpack-Compose-Starter?colorA=363a4f&colorB=a6da95&style=for-the-badge"></a>
    <img src="https://img.shields.io/github/actions/workflow/status/atick-faisal/Jetpack-Compose-Starter/Build.yaml?style=for-the-badge&logo=android&labelColor=363a4f"/>
</p>

## Documentation
<br>
<p align="center">
        <img src="https://github.com/atick-faisal/Jetpack-Compose-Starter/assets/38709932/02fde5d3-f564-4020-8778-47b2d6a1b7b5" width=400 />
    <br>
    <a href="https://atick.dev/Jetpack-Compose-Starter">Read The Documentation Here</a>
</p>

## Features
This template offers Modern Android Development principles and Architecture guidelines. It provides an out-of-the-box template for:
* Connecting to a remote API using Retrofit and OKHttp
* Persistent database solution using Room and Datastore
* Bluetooth communication using classic and low-energy (upcoming) protocols

It contains easy-to-use Interfaces for common tasks. For example, the following provides utilities for Bluetooth communication:
``` kotlin
/**
 * BluetoothManager interface provides methods to manage Bluetooth connections.
 */
interface BluetoothManager {
    /**
     * Attempts to establish a Bluetooth connection with the specified device address.
     *
     * @param address The address of the Bluetooth device to connect to.
     * @return A [Result] indicating the success or failure of the connection attempt.
     */
    suspend fun connect(address: String): Result<Unit>

    /**
     * Returns the state of the connected Bluetooth device.
     *
     * @return A [StateFlow] emitting the current state of the connected Bluetooth device.
     */
    fun getConnectedDeviceState(): StateFlow<BtDevice?>

    /**
     * Closes the existing Bluetooth connection.
     *
     * @return A [Result] indicating the success or failure of closing the connection.
     */
    suspend fun closeConnection(): Result<Unit>
}
```

It also contains several utilities and extension functions to make repetitive tasks easier. For example:
``` kotlin
/**
 * Displays a short toast message.
 *
 * @param message The message to be displayed in the toast.
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Checks if the app has a given permission.
 *
 * @param permission The permission to check.
 * @return `true` if the permission is granted, `false` otherwise.
 */
fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

/**
 * Checks if all the given permissions are granted.
 *
 * @param permissions List of permissions to check.
 * @return `true` if all permissions are granted, `false` otherwise.
 */
fun Context.isAllPermissionsGranted(permissions: List<String>): Boolean {
    return permissions.all { hasPermission(it) }
}
```

## Technologies
* Kotlin
* Jetpack Compose
* Kotlin Coroutines
* Kotlin Flow for Reactive Data
* Retrofit and OkHttp
* Room Database
* Preferences Datastore
* Dependency Injection with Hilt
* Gradle Kotlin DSL
* Gradle Version Catalog
* Convention Plugin

## Architecture
This template follows the [official architecture guidance](https://developer.android.com/topic/architecture)  suggested by Google.

## Modularization
![Modularization](https://github.com/atick-faisal/Jetpack-Compose-Starter/blob/main/assets/modularization.svg)

## Building
### Debug
This project requires Firebase for analytics. Building the app requires `google-services.json` to be present inside the `app` dir. This file can be generated from the [Google Cloud Console](https://firebase.google.com/docs/android/setup). After that, run the following from the terminal.

``` sh
$ ./gradlew assembleDebug 
```

Or, use `Build > Rebuild Project`.

### Release
Building the `release` version requires a `Keystore` file in the `app` dir. Also, a `keystore.properties` file needs to be created in the `rootDir`.
```
storePassword=****
keyPassword=*****
keyAlias=****
storeFile=keystore file name (e.g., key.jks)
```
After that, run the following from the terminal.
``` sh
$ ./gradlew assembleRelease 
```

<p align="center"><img src="https://raw.githubusercontent.com/catppuccin/catppuccin/main/assets/footers/gray0_ctp_on_line.svg?sanitize=true" /></p>
<p align="center"><a href="https://sites.google.com/view/mchowdhury" target="_blank">Qatar University Machine Learning Group</a>
<p align="center"><a href="https://github.com/atick-faisal/Jetpack-Compose-Starter/blob/main/LICENSE"><img src="https://img.shields.io/static/v1.svg?style=for-the-badge&label=License&message=MIT&logoColor=d9e0ee&colorA=363a4f&colorB=b7bdf8"/></a></p>
