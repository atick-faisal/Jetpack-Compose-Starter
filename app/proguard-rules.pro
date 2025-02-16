# Minify obfuscates navigation destinations marked with @Serializable
# https://issuetracker.google.com/issues/353898971?pli=1
-keep @interface kotlinx.serialization.Serializable
-keep @kotlinx.serialization.Serializable class * { *; }

# Crashlytics
# https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.