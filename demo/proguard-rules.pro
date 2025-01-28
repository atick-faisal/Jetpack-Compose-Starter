# Minify obfuscates navigation destinations marked with @Serializable
# https://issuetracker.google.com/issues/353898971?pli=1
-keep @interface kotlinx.serialization.Serializable
-keep @kotlinx.serialization.Serializable class * { *; }
