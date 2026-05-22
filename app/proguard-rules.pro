# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep all classes in our package
-keep class com.app.dinglian.** { *; }

# Keep support library classes
-keep class android.support.** { *; }
-dontwarn android.support.**

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-dontwarn com.github.mikephil.charting.**

# Keep our models
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
