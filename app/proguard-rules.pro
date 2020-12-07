# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclassmembers class **{ public static com.meituan.robust.ChangeQuickRedirect *; }
-keepattributes SourceFile,LineNumberTable

-dontwarn com.google.**

-dontwarn com.android.**

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}

-keep  class * extends  androidx.appcompat.app.AppCompatActivity{ *; }


-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao { public static java.lang.String TABLENAME; }
-keep class **$Properties

-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class com.hao.minovel.spider.data.*
-keep class com.hao.minovel.db.*
-keep class com.hao.minovel.moudle.entity.*
-keep class com.hao.minovel.spider.data.*
-keep class com.hao.minovel.view.minovelread.*
-keep class **Dao

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}






#tinker multidex keep patterns:
-keep public class * implements com.tencent.tinker.entry.ApplicationLifeCycle {
    <init>(...);
    void onBaseContextAttached(android.content.Context);
}

-keep public class com.tencent.tinker.entry.ApplicationLifeCycle {
    *;
}

-keep public class * extends com.tencent.tinker.loader.TinkerLoader {
    <init>(...);
}

-keep public class * extends android.app.Application {
     <init>();
     void attachBaseContext(android.content.Context);
}

-keep class com.tencent.tinker.loader.TinkerTestAndroidNClassLoader {
    <init>(...);
}

#your dex.loader patterns here
-keep class com.hao.minovel.tinker.app.App {
    <init>(...);
}

-keep class com.tencent.tinker.loader.* {
    <init>(...);
}

-keep class android.support.test.internal** { *; }
-keep class org.junit.* { *; }
