# JNRain for Android

[![Build Status](https://travis-ci.org/jnrainerds/jnrain-android.png)](https://travis-ci.org/jnrainerds/jnrain-android)

这是[江南听雨 BBS](http://bbs.jnrain.com/) 的 Android 客户端。功能不算完善，十分欢迎各种形式的贡献。JNRainerds 欢迎有意愿参与的同学们加入。


## 许可证

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)


## 构建

### 编译方法

*   安装 [Android SDK](http://developer.android.com/sdk/index.html)，并设置好 `$ANDROID_HOME` 环境变量（到你的 Android SDK 目录）
*   安装并设置好 [Maven](http://maven.apache.org/download.html) 的 3.0 版本。3.1 版暂时有插件不兼容而无法构建的问题。
*   安装好 [Gradle](http://www.gradle.org), 编译定制的依赖关系需要。
*   **重要** 去 `deps/` 目录照着 README 准备依赖关系。
*   执行构建

    ```sh
    # 在版本库的顶层目录下
    mvn clean package  # 生成的 apk 包在 target/ 目录里
    ```

### Eclipse 设置

请参考 `deps` 目录下的 README 指示完成工作区配置。


## 依赖的库

JNRain for Android 是基于很多开源库开发而成的产品，以下是本项目所有的直接依赖关系。

* [RoboSpice](https://github.com/octo-online/robospice/): 异步网络请求。
* [RoboGuice](http://code.google.com/p/roboguice/): 简化 UI 代码。
* [ActionBarSherlock](http://actionbarsherlock.com/): 兼容低版本系统的 action bar 实现。
* [roboguice-sherlock](https://github.com/rtyley/roboguice-sherlock/): 把 RoboGuice 和 ActionBarSherlock 结合起来。
* [ViewPagerIndicator](http://viewpagerindicator.com/): 提供 `Fragment` 选项卡的支持。
* [SlidingMenu](https://github.com/jfeinstein10/SlidingMenu): 提供滑动菜单的交互方式。


## API 覆盖

* 旧站 (KBS) API
    * 登录/注销
    * 大区列表
    * 版面列表
    * 热帖（十大）
        - 全站
        - *TODO: 大区*
        - *TODO: 版面*
    * 帖子列表
    * 读帖
    * 发帖

新站 API 尚不稳定，数据源也没有，因此暂时没有支持。这部分的工作会在新站上线公测之时同步完成。


<!-- vim:set ai et ts=4 sw=4 sts=4 ff=unix fenc=utf-8: -->
