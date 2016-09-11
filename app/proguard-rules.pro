# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\android\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify                # 混淆时是否做预校验
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#不混淆行数,方便debug
#-keepattributes SourceFile,LineNumberTable
#不混淆注解
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes Signature

#-keepattributes InnerClasses

 # 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment



-dontnote android.support.**
-dontwarn android.support.**

-keep class android.support.** { *; }
-keep interface android.support.** { *; }

-keep class com.iwillow.android.digestnews.entity.**{
  *;
}

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}


-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class **.R$* {
        public static <fields>;
    }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class com.squareup.okhttp.** { *; }

-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }


-dontwarn okio.**
-keep class okio.**{ *;}
-keep class org.jsoup.**{*;}

-dontwarn rx.**
-keep class rx.** { *; }
-keep class * extends io.realm.RealmObject
-keep class io.realm.**{*;}

 #gson
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.android.volley.** { *; }

-keep class uk.co.senab.photoview.** { *; }

-keep class com.google.android.exoplayer.** { *; }

-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**
-keep class com.squareup.leakcanary.**{*;}

#3D 地图

-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
#定位

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 搜索

-keep   class com.amap.api.services.**{*;}

#2D地图

-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

   # 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}