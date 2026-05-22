# 快速编译指南（最简单的方法）

## 方法一：使用 Android Studio 编译（强烈推荐）⭐⭐⭐

这是最简单、最不容易出错的方法！

### 1. 准备工作
确保您的电脑已安装：
- ✅ Android Studio（下载：https://developer.android.com/studio）
- ✅ 至少 8GB RAM
- ✅ 至少 10GB 磁盘空间

### 2. 下载项目
```bash
# 克隆项目
git clone https://github.com/ponygates/dinglian.git
cd dinglian
```

### 3. 打开项目
1. 启动 Android Studio
2. 点击 **"Open an Existing Project"**
3. 选择刚才下载的 `dinglian` 文件夹
4. **等待 Gradle 同步完成**（第一次可能需要 5-10 分钟）

### 4. 编译签名 APK
1. 点击菜单：**Build** → **Generate Signed Bundle / APK...**
2. 选择 **APK** → **Next**
3. 填写签名信息：
   - **Key store path**: 点击右边的文件夹图标，选择 `app/dinglian-release-key.jks`
   - **Key store password**: `dinglian123`
   - **Key alias**: `dinglian`
   - **Key password**: `dinglian123`
4. 点击 **Next**
5. 选择 **release** 构建类型 → 点击 **Finish**

### 5. 找到编译好的 APK
编译完成后，APK 位置：
```
dinglian/app/build/outputs/apk/release/app-release.apk
```

就是这么简单！🎉

---

## 方法二：使用命令行编译（适合熟悉命令行的用户）

### Windows 用户
```cmd
cd dinglian
gradlew.bat assembleRelease
```

### Mac/Linux 用户
```bash
cd dinglian
./gradlew assembleRelease
```

编译好的 APK 同样在：
```
app/build/outputs/apk/release/app-release.apk
```

---

## 方法三：等待 GitHub Actions 自动编译（我已经配置好了）

1. 我已经把编译流程推送到 GitHub
2. 稍等一会儿，GitHub 会自动尝试编译
3. 查看编译状态：https://github.com/ponygates/dinglian/actions
4. 如果成功，您可以在 Actions 页面下载编译好的 APK

---

## 签名配置信息备忘

| 配置项 | 值 |
|--------|-----|
| 密钥文件位置 | app/dinglian-release-key.jks |
| 密钥库密码 | dinglian123 |
| 密钥别名 | dinglian |
| 密钥密码 | dinglian123 |

---

## 安装 APK 到手机

1. 用 USB 线连接手机到电脑
2. 在手机上开启 **"USB 调试"**（设置 → 开发者选项）
3. 在 Android Studio 中点击 **Run** 按钮（绿色 ▶️）
4. 选择您的手机
5. 点击 **OK**，APK 会自动安装！

---

## 常见问题解答

### Q: Gradle 同步一直失败怎么办？
A: 检查网络连接，或尝试配置国内镜像源。

### Q: 找不到签名密钥文件？
A: 确保密钥文件在 `app/dinglian-release-key.jks`，或者重新生成一个。

### Q: 编译时提示 SDK 版本不对？
A: 在 Android Studio 的 SDK Manager 中安装 Android 9.0 (API 28)。

### Q: 想自己重新生成签名密钥？
A: 用这个命令（记得修改密码！）：
```bash
keytool -genkey -v -keystore my-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-app
```

---

## 下一步

编译成功后，请查看 [BUILD_GUIDE.md](BUILD_GUIDE.md) 了解更多详细信息和功能测试建议！
