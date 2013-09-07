# apklib 依赖关系存放目录

## 原因

项目 `project.properties` 里面指定 apklib 引用的方法是相对路径。为了防止不同开发者采用路径设置不同而频繁造成变动，而把依赖关系都收集到这里。具体来说，就是三个 apklib 依赖关系：

* ActionBarSherlock
* ViewPagerIndicator
* SlidingMenu


## 设置

**注意** 以下设置步骤需要网络连接。

1. 检出版本库到本地之后，请在此目录下检出依赖关系的版本库：

```bash
git clone https://github.com/JakeWharton/ActionBarSherlock.git
git clone https://github.com/JakeWharton/Android-ViewPagerIndicator.git
git clone https://github.com/xen0n/SlidingMenu.git
```

2. 然后根据版本库根目录下的 `pom.xml` 指定的依赖关系版本，切到合适的 tag：

```bash
cd ActionBarSherlock
git checkout 4.3.1  # 以后可能会变，以 pom.xml 中指定的版本为准
cd ../Android-ViewPagerIndicator
git checkout 2.4.1  # 同上
# SlidingMenu 目前使用的就是自己 fork 的 master 分支, 所以不用切换
```

3. 如果你的 Android SDK 里没有 Android 4.0 (API level 14) 的话，下载一个装上，ABS 的构建需要。

4. 在 Eclipse 里导入个库的项目，项目类型选 `Maven/Existing Maven Projects`，搜索根目录就选本目录。然后只导入 `actionbarsherlock` 和两个 `library` 项目。

5. （可能不需要）刷新 `jnrain-android` 项目的 Maven 依赖（在右键菜单的 Maven 子菜单里），构建项目。这应该会触发各 apklib 的构建然后成功；如果失败了，手动构建三个依赖关系，应该也没问题。

6. 如果构建还是不能成功，就在各项目的项目属性 -> Android 里边调整下 apklib 声明。然后分别构建各个依赖项目，应该能解决 build path 的问题了。


<!-- vim:set ai et ts=4 sw=4 sts=4 ff=unix fenc=utf-8: -->
