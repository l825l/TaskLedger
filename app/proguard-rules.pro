# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Keep data models
-keep class com.ledger.task.data.model.** { *; }
-keep class com.ledger.task.data.local.** { *; }

# Keep ViewModel classes
-keep class com.ledger.task.viewmodel.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.viewmodel.** { *; }

# Keep enum classes
-keep class com.ledger.task.data.model.Priority
-keep class com.ledger.task.data.model.TaskStatus
-keep class com.ledger.task.viewmodel.SortField

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom views
-keep class com.ledger.task.ui.** { *; }

# Keep serialization
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep annotations
-keepclassmembers class * {
    @androidx.compose.* <fields>;
    @androidx.lifecycle.* <fields>;
}

# OkHttp (if used)
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Error Prone annotations (used by Tink/crypto libraries)
-dontwarn com.google.errorprone.annotations.**

# Tink crypto library
-dontwarn com.google.crypto.tink.**
-keep class com.google.crypto.tink.** { *; }

# Keep Gson
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep enum toString
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
