# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xiayumo/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontwarn
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法


-dontskipnonpubliclibraryclasses




-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep public class com.jisupei.utils.widget.MyRadioButton    # 保持哪些类不被混淆
-keep public class com.jisupei.activity.homepage2.H5DaicaiActivity    # 保持哪些类不被混淆

-keepclassmembers class com.jisupei.activity.homepage2.H5DaicaiActivity.JavaScriptinterface{
  public *;
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
 }
-keepclassmembers class com.jisupei.activity.homepage2.H5DaicaiActivity$JavaScriptinterface{
    public *;
}
-keep public class com.jisupei.activity.homepage2.H5DaicaiActivity$JavaScriptinterface{
    public void *(java.lang.String);
}
-keepclassmembers class com.jisupei.activity.VpH5Activity$JavaScriptinterface{
    public *;
}
-keepclassmembers class com.jisupei.activity.datail.H5orderActivity$JavaScriptinterface{
    public *;
}
#-keepclassmembers class com.jisupei.activity.homepage2.H5DaicaiActivity$clickAD{
#    public *;
#}
#-keep public class com.jisupei.activity.homepage2.H5DaicaiActivity$clickAD{
#    public void *(java.lang.String);
#}
#-keepclassmembers class com.jisupei.class$* {
#    <methods>;
#}




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
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }
# Gson specific classes

#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keepattributes Signature
#忽略警告
-ignorewarning
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }
-keep class com.j256.ormlite.misc.** { *; }
-keep class com.j256.ormlite.dao.** { *; }
-keep public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keep public class * extends com.j256.ormlite.android.apptools.OpenHelperManager
-dontwarn com.j256.ormlite.**
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.stmt.**
-dontwarn com.j256.ormlite.misc.**
-dontwarn com.j256.ormlite.dao.**
# volley
-dontwarn com.android.volley.jar.**
-keep class com.android.volley.**{*;}

#adapter也不能混淆
#-keep public class * extends android.widget.BaseAdapter {*;}
-keepnames class * implements java.io.Serializable     #比如我们要向activity传递对象使用了Serializable接口的时候，这时候这个类及类里面#的所有内容都不能混淆
-keepclassmembers class * implements java.io.Serializable {
        *;
}

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#########fresco##########
#okio
-dontwarn okio.**
-keep class okio.**{*;}
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
##########################autoscrollviewpager########################

-keep class cn.trinea.android.** { *; }
-keepclassmembers class cn.trinea.android.** { *; }
-dontwarn cn.trinea.android.**
##########################个推########################
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
##########################蒲公英########################
#-libraryjars libs/pgyer_sdk_x.x.jar
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }

#百度
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**