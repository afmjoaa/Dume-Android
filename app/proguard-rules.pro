# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#TODO
#################################################################################################
# Standard Configuration for Android App
# See http://proguard.sourceforge.net/index.html#manual/examples.html

-optimizationpasses 2
#-dontobfuscate
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
#-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# adding this in to preserve line numbers so that the stack traces
# can be remapped
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable


#################################################################################################
# For RoboSpice
# See https://groups.google.com/forum/?fromgroups=#!topic/robospice/xGLRbGkLwQU
#Request classes purged by Proguard as they are "empty", others are kept
-keep class com.limbocitizen.android.playground.model.**

#RoboSpice requests and Results must be kept as they are used by reflection via Jackson
-keepclassmembers class com.limbocitizen.android.playground.request.** {
  public void set*(***);
  public *** get*();
  public *** is*();
}


### XML SERIALIZER SETTINGS

-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}


### Json SERIALIZER SETTINGS
-keepclassmembers,allowobfuscation class * {
    @org.codehaus.jackson.annotate.* <fields>;
    @org.codehaus.jackson.annotate.* <init>(...);
}


#Warnings to be removed. Otherwise maven plugin stops, but not dangerous
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.persistence.**

#################################################################################################
# For Actionbarsherlock
# See http://actionbarsherlock.com/faq.html

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }

# -keepattributes *Annotation*


#################################################################################################
# My Code

-dontwarn com.caverock.**
-keep class com.caverock.**
-keep interface com.caverock.**

#okhttp
-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepattributes Signature
-keepattributes *Annotation*


-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#geoFirestore
#-dontwarn org.imperiumlabs.**
#-keep class org.imperiumlabs.**
#-keep interface org.imperiumlabs.**
#-keep class io.dume.dume.components.library.myGeoFIreStore.GeoFirestore.** { *; }

#firebase
# Keep custom model classes
-keep class com.google.firebase.example.fireeats.java.model.** { *; }
-keep class com.google.firebase.example.fireeats.kotlin.model.** { *; }

# https://github.com/firebase/FirebaseUI-Android/issues/1175
-dontwarn okio.**
-dontwarn retrofit2.Call
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-keep class android.support.v7.widget.RecyclerView { *; }
#keep all serializable
-keep public class io.dume.dume.teacher.pojo.**{
public void set*(***);
public *** get*();
public *** is*();
}
-keepclasseswithmembernames class io.dume.dume.teacher.pojo.**{
void set*(***);
*** get*();
*** is*();
}

#linechart
-keep public class com.github.mikephil.charting.animation.* {
    public protected *;
    void set*(***);
    *** get*();
    *** is*();
}
-keep public class com.github.mikephil.** {
     public protected *;
     void set*(***);
     *** get*();
     *** is*();
}

-keepattributes *Annotation*, Signature, Exception
-keepattributes SourceFile, LineNumberTable

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

