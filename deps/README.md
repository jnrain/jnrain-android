# apklib 依赖关系存放目录

## 原因

项目 `project.properties` 里面指定 apklib 引用的方法是相对路径。为了防止不同开发者采用路径设置不同而频繁造成变动，而把依赖关系都收集到这里。具体来说，就是三个 apklib 依赖关系：

* ActionBarSherlock
* ViewPagerIndicator
* SlidingMenu


## 

**注意** 以下设置步骤需要网络连接。

1.  确保安装了 [Gradle](http://www.gradle.org)，后边编译 ActionBarSherlock 会用到。
1.  如果你的 Android SDK 里没有 Android 4.0 (API level 14) 的话，下载一个装上，ABS 的构建需要。
1.  在你的 Android SDK 管理器里下载 Android Support Repository （在 Extras 分类里），并将其中的 support-v4 链接或拷贝到你的 Maven 本地库中。

    ```bash
    # 假设你的 Android SDK 装在 /path/to/android-sdk, 你的 Maven 本地库就在默认位置 (~/.m2/repository)
    # 先确保有 com.android 的位置，然后建立符号链接
    # Windows 用户貌似只好拷贝了，当然你非要 mklink 的话我不拦着你，反正我没试过行不行- -
    mkdir -p ~/.m2/repository/com/android
    ln -s /path/to/android-sdk/extras/android/m2repository/com/android/support/support-v4 ~/.m2/repository/com/android/support-v4
    ```

## 自动设置/更新

使用 Linux/Mac OS X 等类 Unix 操作系统的用户可执行本目录下的 `update-deps.sh` 自动在构建环境中设置依赖关系：

```bash
# 一般来讲是这样
./update-deps.sh

# 如果它告诉你你的 Maven 版本不对，这么给它指定路径
MVN_CMD=mvn-3.0 ./update-deps.sh
```

其实 Windows 下的 Git Bash/Cygwin/MinGW 应该也都能用，不过我没测试过，如果你有这种设置的话欢迎开 issue 告诉我你的试验结果。


## 手动设置步骤

不用 bash 的 Windows 用户，不好意思了 :-P


### 准备版本库

1.  检出版本库到本地之后，请在此目录下检出依赖关系的版本库：

    ```bash
    git clone https://github.com/xen0n/ActionBarSherlock.git
    git clone https://github.com/xen0n/Android-ViewPagerIndicator.git
    git clone https://github.com/xen0n/SlidingMenu.git
    ```

1.  然后根据版本库根目录下的 `pom.xml` 指定的依赖关系版本，切到合适的 tag：

    ```bash
    cd ActionBarSherlock
    git checkout 4.4.0-xen0n  # 以后可能会变，以 pom.xml 中指定的版本为准
    cd ../Android-ViewPagerIndicator
    git checkout 2.4.1-xen0n  # 同上
    # SlidingMenu 目前使用的就是自己 fork 的 master 分支, 所以不用切换
    ```


### 构建

```bash
# 先回到 deps/ 目录

# 现在构建 ABS, 因为我忘了改 samples 项目的 POM 所以它的构建会失败，跳过也没关系
cd ActionBarSherlock/actionbarsherlock/
mvn clean install
# 安装 ABS 的 parent POM
cd ..
mvn install -N

# 构建 VPI
cd ../Android-ViewPagerIndicator/
mvn clean install

# 构建 SlidingMenu
# 地图相关的部分应该会编译失败，跳过
cd ../SlidingMenu/library/
mvn clean install
# 同样姿势安装 parent POM
cd ..
mvn install -N

# 依赖关系构建结束，可以 cd ../.. 编译 app 本体了
```


## Eclipse 用户环境设置

1.  在 Eclipse 里导入各个库的项目，项目类型选 `Git/Projects from Git`，搜索根目录就选本目录。然后只导入 `actionbarsherlock` 和两个 `library` 项目。Maven 特性会被自动识别出来的。

1.  （可能不需要）刷新 `jnrain-android` 项目的 Maven 依赖（在右键菜单的 Maven 子菜单里），构建项目。这应该会触发各 apklib 的构建然后成功；如果失败了，手动构建三个依赖关系，应该也没问题。

1.  如果构建还是不能成功，就在各项目的项目属性 -> Android 里边调整下 apklib 声明，实在不行就 refresh 或者 clean 项目都试一下。然后分别构建各个依赖项目，应该能解决 build path 的问题了。


<!-- vim:set ai et ts=4 sw=4 sts=4 ff=unix fenc=utf-8: -->
