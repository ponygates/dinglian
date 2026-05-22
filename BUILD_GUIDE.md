# 定联管理系统 - 编译和使用指南

## 项目完善内容 ✅

已完成以下项目完善：
- ✅ 更新 build.gradle 配置文件
- ✅ 配置签名设置
- ✅ 生成签名密钥 (dinglian-release-key.jks)
- ✅ 创建 ProGuard 配置文件
- ✅ 完善 Gradle Wrapper 脚本

## 项目结构

```
dinglian/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/app/dinglian/...
│   │       ├── res/...
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── dinglian-release-key.jks  ⬅️ 签名密钥
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
├── gradlew  ⬅️ Gradle Wrapper (可执行)
└── README.md
```

## 签名配置信息

已配置的签名信息（仅供开发测试使用）：

| 配置项 | 值 |
|--------|-----|
| 密钥文件 | dinglian-release-key.jks |
| 密钥库密码 | dinglian123 |
| 密钥别名 | dinglian |
| 密钥密码 | dinglian123 |

## 在 Android Studio 中编译

### 1. 准备环境

确保已安装：
- Android Studio 3.5+
- Android SDK Platform 28
- Android SDK Build-Tools 28.0.3
- JDK 8

### 2. 打开项目

1. 启动 Android Studio
2. 选择 "Open an Existing Project"
3. 选择项目根目录 `dinglian/`
4. 等待 Gradle 同步完成

### 3. 编译 Debug 版本

1. 在 Android Studio 中，点击菜单 `Build` → `Make Project`
2. 或点击工具栏的 "Make" 按钮
3. 编译后的 APK 位置：
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### 4. 编译 Release 版本（已签名）

1. 点击菜单 `Build` → `Generate Signed Bundle / APK...`
2. 选择 "APK" → Next
3. 配置签名信息：
   - Key store path: 选择 `app/dinglian-release-key.jks`
   - Key store password: `dinglian123`
   - Key alias: `dinglian`
   - Key password: `dinglian123`
4. 选择 "release" 构建类型
5. 点击 "Finish"

编译完成后，签名 APK 位置：
```
app/build/outputs/apk/release/app-release.apk
```

### 5. 使用命令行编译

如果您已配置好 Android SDK 和 Gradle 环境：

```bash
# 编译 Debug 版本
./gradlew assembleDebug

# 编译 Release 版本（已签名）
./gradlew assembleRelease

# 清理并重新编译
./gradlew clean assembleRelease
```

## 安装 APK 到手机

### 方法一：通过 Android Studio

1. 将手机连接到电脑并启用 USB 调试
2. 在 Android Studio 中点击 "Run" 按钮 ▶️
3. 选择您的设备并确认安装

### 方法二：直接安装 APK

```bash
# 通过 adb 安装
adb install app/build/outputs/apk/release/app-release.apk

# 或直接传输 APK 文件到手机，在手机上点击安装
```

## 功能测试建议

安装完成后，请按以下顺序测试功能：

1. **启动页** - 等待 3 秒，确认自动跳转
2. **密码设置** - 首次进入设置密码（可选）
3. **添加客户** - 进入管理页面添加几个测试客户
4. **客户分级** - 设置不同的客户等级（A/B/C/D）
5. **跟踪页面** - 查看需联系的客户
6. **联系功能** - 测试电话和微信功能
7. **签单功能** - 测试签单信息填写
8. **统计图表** - 查看客户统计
9. **设置功能** - 测试各项设置

## 常见问题

### Q: Gradle 同步失败怎么办？
A: 检查网络连接，配置 Gradle 代理，或尝试更换国内镜像源。

### Q: 找不到 Android SDK？
A: 在 Android Studio 的 `File` → `Project Structure` → `SDK Location` 中配置正确的 SDK 路径。

### Q: 签名密钥安全吗？
A: 当前使用的是测试密钥，生产环境请重新生成更安全的密钥并妥善保管。

### Q: 如何重新生成签名密钥？
A: 使用以下命令：
```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias my-key
```

## 技术支持

如有问题，请通过 GitHub Issues 或邮件联系。

---

**祝您使用愉快！**
