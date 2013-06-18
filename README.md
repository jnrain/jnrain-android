# JNRain for Android

[![Build Status](https://travis-ci.org/jnrainerds/jnrain-android.png)](https://travis-ci.org/jnrainerds/jnrain-android)

这是[江南听雨 BBS](http://bbs.jnrain.com/) 的 Android 客户端。项目刚刚起步，十分欢迎各种形式的贡献。有意愿参与的可通过 pull request 的方式与我们互动。


## 许可证

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)


## 构建方式

构建之前你需要安装 [Android SDK](http://developer.android.com/sdk/index.html)，并设置好 `$ANDROID_HOME` 环境变量（到你的 Android SDK 目录）。项目采用 [Maven](http://maven.apache.org/download.html) 进行构建，在版本库根目录

    $ mvn clean package

即可得到 apk 包。

Eclipse 用户注意：m2e-android 的[已知问题](https://github.com/rgladwell/m2e-android/issues/104) **已经解决**，所以 launch 的问题已经解决了。请参考 `deps` 目录下的 README 指示完成工作区配置。


## 依赖

* [RoboSpice](https://github.com/octo-online/robospice/): 进行异步网络请求。
* [RoboGuice](http://code.google.com/p/roboguice/): 简化 UI 代码。
* [ActionBarSherlock](http://actionbarsherlock.com/): 兼容低版本系统的 action bar 实现。
* [roboguice-sherlock](https://github.com/rtyley/roboguice-sherlock/): 结合 RoboGuice 和 ActionBarSherlock 的 glue code。
* [ViewPagerIndicator](http://viewpagerindicator.com/): 提供 `Fragment` 选项卡的支持。


## 功能特性

因为是最初的原型开发阶段，所以功能极少（制作这个客户端的过程也是参与者学习的过程）。下面是目前已经实现的功能。

* 大区列表 API
* 版面列表 API
* 热帖（十大） API
    * 全站
    * *TODO: 大区*
    * *TODO: 版面*
* 帖子列表 API
* 读帖 API
* 登录注销
* 发帖 API

同样因为原型开发的原因，客户端没有接入[落花](https://github.com/xen0n/luohua/)/[微雨](https://github.com/xen0n/weiyu/)项目的API终点，而是接入了江南听雨BBS PHP的实验API终点，以利用已经成熟的数据源。


<!-- vim:set ai et ts=4 sw=4 sts=4 ff=unix fenc=utf-8: -->
