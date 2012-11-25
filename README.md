# JNRain for Android

这是[江南听雨 BBS](http://bbs.jnrain.com/) 的 Android 客户端。项目刚刚起步，十分欢迎各种形式的贡献。有意愿参与的可通过 pull request 的方式与我们互动。


## 许可证

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)


## 构建方式

构建之前你需要安装 [Android SDK](http://developer.android.com/sdk/index.html)，并设置好 `$ANDROID_HOME` 环境变量。项目采用 [Maven](http://maven.apache.org/download.html) 进行构建，运行

    $ mvn clean package

即可得到 apk 包。在安装了 Maven 集成和 maven-android-plugin 的 Eclipse 环境中，只需检出整个版本库即可在 Eclipse 中进行构建。


## 依赖

* [RoboSpice](https://github.com/octo-online/robospice/): 进行异步网络请求。
* [RoboGuice](http://code.google.com/p/roboguice/): 简化 UI 代码。


## 功能特性

因为是最初的原型开发阶段，所以功能极少（制作这个客户端的过程也是参与者学习的过程）。下面是目前已经实现的功能。

* 大区列表 API
* 版面列表 API

同样因为原型开发的原因，客户端没有接入[落花/微雨项目](https://github.com/xen0n/weiyu)的API终点，而是接入了江南听雨BBS PHP的实验API终点，以利用已经成熟的数据源。
