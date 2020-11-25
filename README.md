# frameLibraryWidget
[![](https://jitpack.io/v/GitHubWebb/frameLibraryWidget.svg)](https://jitpack.io/#GitHubWebb/frameLibraryWidget)
----
## 引用方式
## 注:从2.0-开始支持AndroidX
>1.需要在根Build.gradle中配置jitpack依赖
>>allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
>2.在需要调用的module	dependencies 中,加入
>>implementation com.github.GitHubWebb.frameLibraryWidget:frameLibraryWidget:2.0.6-androidx
	
	
### 提醒
 注 所引入的开源库都根据自身需要进行了二次修改
 引入的youth.banner
 implementation 'com.youth.banner:banner:1.4.10'
 
 引入的WenldBanner
 Banner控件 https://www.jianshu.com/p/b8fe093a9d4b
 implementation 'com.github.LidongWen:WenldBanner:2.0.2'
 
 