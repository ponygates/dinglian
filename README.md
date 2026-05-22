# 定联管理系统

一款专门为保险代理人设计的客户定期联系管理工具，基于张瀞心《定联》开发。

---

## 📱 立即获取 APK（3种方式）

### 方式一：使用 Android Studio 编译（推荐）⭐

**最简单！**
1. 下载 Android Studio：https://developer.android.com/studio
2. 克隆项目：`git clone https://github.com/ponygates/dinglian.git`
3. 在 Android Studio 中打开项目
4. 菜单 `Build` → `Generate Signed Bundle / APK...`
5. 使用签名信息（密码：dinglian123）
6. 完成！

详细步骤：[QUICK_START.md](QUICK_START.md)

### 方式二：一键编译脚本

**Windows 用户：**
双击运行 `BUILD_APK.bat`

**Mac/Linux 用户：**
```bash
chmod +x build_apk.sh
./build_apk.sh
```

### 方式三：GitHub Actions 自动编译

查看：https://github.com/ponygates/dinglian/actions

---

## 🔐 签名配置

| 配置项 | 值 |
|--------|-----|
| 密钥文件 | app/dinglian-release-key.jks |
| 密钥库密码 | dinglian123 |
| 密钥别名 | dinglian |
| 密钥密码 | dinglian123 |

---

## ⭐ 功能特性

### 客户分级管理
- **A级**（挺你到底）：已送保险建议书，需每周联系一次
- **B级**（好友）：可聊个人心事，需每3周联系一次
- **C级**（三分熟）：曾经很熟但许久未见，需每6周联系一次
- **D级**（认识有联络方式）：需每8周联系一次

### 主要功能
- **跟踪页面**：显示需要联系的客户，可直接拨打或复制微信
- **管理页面**：查看、添加、编辑、删除所有客户信息
- **签单页面**：查看已签单客户的详细信息
- **统计页面**：查看客户统计数据和分布图表
- **智能提醒**：每天定时提醒需要联系的客户
- **密码保护**：数据库密码保护，保障数据安全

### 技术特性
- 兼容Android 8.0+系统
- 支持华为手机和大众安卓手机
- 本地SQLite数据库存储
- 支持导入手机联系人
- 支持数据备份和恢复（开发中）

## 📋 开发环境
- Android Studio 3.5+
- Java 8
- Android SDK 26 (Android 8.0)
- Gradle 5.4.1+

## 📁 项目结构
```
app/
├── src/
│   └── main/
│       ├── java/com/app/dinglian/
│       │   ├── activity/          # Activity页面
│       │   ├── fragment/          # Fragment页面
│       │   ├── database/          # 数据库相关
│       │   ├── model/             # 数据模型
│       │   ├── receiver/          # 广播接收器
│       │   └── service/           # 服务
│       ├── res/                   # 资源文件
│       └── AndroidManifest.xml    # 应用清单
├── build.gradle                   # 应用级构建配置
└── ...
```

## 💡 使用说明

1. 在管理页面添加客户信息
2. 设置客户等级和跟进状态
3. 系统会根据等级自动计算下次联系时间
4. 在跟踪页面查看需要联系的客户
5. 点击微信或电话图标可快速联系

## 👨‍💼 作者
新华保险 司马君

## 📌 版本
1.0.0
