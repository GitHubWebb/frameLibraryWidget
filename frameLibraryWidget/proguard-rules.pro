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
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keep class com.maiget.headlines.bean.respbean.**{*; }

-keep class com.sina.weibo.sdk.** { *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

#友盟
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}


-dontwarn com.taobao.**
-keep class com.taobao.**{*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}


-dontwarn org.chromium.tencent.**
-keep class org.chromium.tencent.** {*;}

-dontwarn com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn org.android.spdy.**
-keep class org.android.spdy.** {*;}

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

 -keep class com.umeng.** {*;}
 -keepclassmembers class * {
    public <init> (org.json.JSONObject);
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep public class com.framelibrary.R$*{
 public static final int *;
 }



# 云信混淆代码


 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.module.AppGlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }



 -optimizations !code/simplification/cast,!field/*,!class/merging/*
 -optimizationpasses 5
 -allowaccessmodification
 -dontpreverify


 # The remainder of this file is identical to the non-optimized version
 # of the Proguard configuration file (except that the other file has
 # flags to turn off optimization).
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -verbose

 # The support library contains references to newer platform versions.
 # Don't warn about those in case this app is linking against an older
 # platform version.  We know about them, and they are safe.
 ### Android support
 -dontwarn org.apache.http.**
 -dontwarn android.support.**
 -keep class android.support.** {*;}
 -keep class android.webkit.** {*;}

 ### keep options, system default, from android example
 -keep public class * extends android.app.Activity
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep public class com.google.vending.licensing.ILicensingService
 -keep public class com.android.vending.licensing.ILicensingService

 -keepattributes *Annotation*,InnerClasses
 #-keepattributes SourceFile,LineNumberTable

#qiniu
-dontwarn com.qiniu.**
-keep class com.qiniu.**{*;}

 ### APP 3rd party jars
 -dontwarn com.amap.**
 -keep class com.amap.** {*;}
 -dontwarn com.baidu.location.**
 -keep class com.baidu.location.** {*;}
 -dontwarn com.autonavi.amap.**
 -keep class com.autonavi.amap.** {*;}
 -dontwarn com.alibaba.**
 -keep class com.alibaba.fastjson.** {*;}

 ### APP 3rd party jars(xiaomi push)
 -dontwarn com.xiaomi.push.**
 -keep class com.xiaomi.** {*;}
 ### APP 3rd party jars(huawei push)

 -keepattributes Exceptions
 -keepattributes Signature
 # hmscore-support: remote transport
 -keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
 # hmscore-support: remote transport
 -keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
 <init>(...); }
 # manifest: provider for updates
 -keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }

 ### APP 3rd party jars(meizu push)
 -dontwarn com.meizu.cloud.**
 -keep class com.meizu.cloud.** {*;}

 # fcm
 -dontwarn com.google.**
 -keep class com.google.** {*;}

 ### APP 3rd party jars(glide)
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

 ### org.json xml
 -dontwarn org.json.**
 -keep class org.json.**{*;}

 ### face unity
 -keep class com.faceunity.auth.AuthPack {*;}

 ### jrmf wx
 -dontwarn com.jrmf360.**
 -keep class com.jrmf360.** {*;}
 #微信
 -dontwarn com.switfpass.pay.**
 -keep class com.switfpass.pay.**{*;}
 -dontwarn com.tencent.mm.**
 -keep class com.tencent.mm.**{*;}
 #支付宝
 -dontshrink
 -dontpreverify
 -dontoptimize
 -dontusemixedcaseclassnames

 -flattenpackagehierarchy
 -allowaccessmodification
 -printmapping map.txt

 -optimizationpasses 7
 -verbose
 -keepattributes Exceptions,InnerClasses
 -dontskipnonpubliclibraryclasses
 -dontskipnonpubliclibraryclassmembers
 -ignorewarnings

 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends java.lang.Throwable {*;}
 -keep public class * extends java.lang.Exception {*;}

 -keep class com.alipay.android.app.IAlixPay{*;}
 -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 -keep class com.alipay.sdk.app.PayTask{ public *;}
 -keep class com.alipay.sdk.app.AuthTask{ public *;}
 -keep class com.alipay.sdk.app.H5PayCallback {
     <fields>;
     <methods>;
 }
 -keep class com.alipay.android.phone.mrpc.core.** { *; }
 -keep class com.alipay.apmobilesecuritysdk.** { *; }
 -keep class com.alipay.mobile.framework.service.annotation.** { *; }
 -keep class com.alipay.mobilesecuritysdk.face.** { *; }
 -keep class com.alipay.tscenter.biz.rpc.** { *; }
 -keep class org.json.alipay.** { *; }
 -keep class com.alipay.tscenter.** { *; }
 -keep class com.ta.utdid2.** { *;}
 -keep class com.ut.device.** { *;}


 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 # adding this in to preserve line numbers so that the stack traces
 # can be remapped
 -renamesourcefileattribute SourceFile
 -keepattributes SourceFile,LineNumberTable


 #支付宝end


 -dontwarn android.net.**
 -keep class android.net.SSLCertificateSocketFactory{*;}
 #okhttp
 -dontwarn okhttp3.**
 -keep class okhttp3.**{*;}
 #okio
 -dontwarn okio.**
 -keep class okio.**{*;}

### glide 3
-keepnames class com.netease.nim.uikit.support.glide.NIMGlideModule

### glide 4
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

 ### fabric
 -keepattributes *Annotation*
 -keepattributes SourceFile,LineNumberTable
 -keep class com.crashlytics.** { *; }
 -dontwarn com.crashlytics.**

 ### NIM SDK
 -dontwarn com.netease.**
 -keep class com.netease.** {*;}
 -dontwarn org.apache.lucene.**
 -keep class org.apache.lucene.** {*;}
 -keep class net.sqlcipher.** {*;}

 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

 -keep class **.R$* {
  *;

 }


 #高德
 -keep class com.amap.api.location.**{*;}
 -keep class com.amap.api.fence.**{*;}
 -keep class com.autonavi.aps.amapapi.model.**{*;}

 #搜索
 -keep   class com.amap.api.services.**{*;}

 #2D地图
 -keep class com.amap.api.maps2d.**{*;}
 -keep class com.amap.api.mapcore2d.**{*;}

 -dontwarn com.amap.api.mapcore2d.MapMessage


#极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

-dontwarn com.framelibrary.bean.**
-keep class com.framelibrary.bean.**{*;}

-dontwarn com.framelibrary.**
-keep class com.framelibrary.**{*;}

-dontwarn com.zxing.**
-keep class com.zxing.**{*;}

#地区3级联动选择器

-keep class com.lljjcoder.**{
	*;
}

-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}


#七牛音视频直播
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}

#SpiderMan debug 模式 崩溃日志抓取
-keep class com.simple.spiderman.** { *; }
-keepnames class com.simple.spiderman.** { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.support.annotation.** { *; }
-keep public class * extends android.support.v4.content.FileProvider
-keep class * implements Android.os.Parcelable {
    public static final Android.os.Parcelable$Creator *;
}


-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

#饺子视频混淆
-dontwarn cn.jzvd.**
-keep class cn.jzvd.**{*;}

#侧滑关闭页面混淆
-dontwarn com.github.anzewei.**
-keep class com.github.anzewei.**{*;}


#根依赖库
-dontwarn com.framelibrary.**
-keep class com.framelibrary.** {*;}

-dontwarn com.wenld.wenldbanner.**
-keep class com.wenld.wenldbanner.** {*;}



#dueeeke/dkplayer

-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class com.dueeeke.videoplayer.** { *; }
-dontwarn com.dueeeke.videoplayer.**


-dontwarn com.yanzhenjie.permission.**

-keep class android.support.** { *; }

#dueeeke/dkplayer end

# 路由混淆配置
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
