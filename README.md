# JNRain for Android

[![Build Status](https://travis-ci.org/jnrainerds/jnrain-android.png)](https://travis-ci.org/jnrainerds/jnrain-android)

这是[江南听雨 BBS](http://bbs.jnrain.com/) 的 Android 客户端，由[江南听雨研发中心](https://github.com/jnrainerds)开发。功能不算完善，十分欢迎各种形式的贡献。江南听雨研发中心热忱欢迎同学们的加入。


## 许可证

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)


## 功能特性

### 敏感权限要求

以下是应用要求的敏感权限及相关说明。为了可读性，权限名称如果以 `android.permission.` 打头则省略此前缀。说明中提到的使用地点是应用中唯一使用到相应权限的地点，没有遗漏，改动应用功能时开发者应注意更新此处的对应说明。

汉语名称 | 权限 | 说明
:------- |:---- |:----
读取手机状态和身份 | `READ_PHONE_STATE` | 用于注册页面自动获取当前手机号码


## 构建

### 编译方法

*   安装 [Android SDK](http://developer.android.com/sdk/index.html)，并设置好 `$ANDROID_HOME` 环境变量（到你的 Android SDK 目录）
*   安装并设置好 [Maven](http://maven.apache.org/download.html)，至少需要 3.1.1 版本。3.0 系列已经不再被最新版的 android-maven-plugin 插件支持。
*   **重要** 去 `deps/` 目录照着 README 准备依赖关系。
*   执行构建

    ```sh
    # 在版本库的顶层目录下
    # 生成的 apk 包会出现在 target/ 目录里
    # 生成调试用 apk:
    mvn clean package

    # 用正式版配置生成 (进行多遍优化, 混淆生成的字节码, 去除调试信息)
    mvn clean package -Ddebug=0
    ```

### Eclipse 设置

请参考 `deps` 目录下的 README 指示完成工作区配置。


## 依赖的库

JNRain for Android 是基于很多开源库开发而成的产品，以下是依赖项目的列表。为实现代码重用，大量辅助代码和胶水层分离到了 [Cytosol](https://github.com/xen0n/Cytosol/) 库中。

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
