#!/bin/bash

echo "========================================"
echo "  定联管理系统 - APK 一键编译脚本"
echo "========================================"
echo ""

# 检查是否有 Android SDK
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    if [ -d "$HOME/Library/Android/sdk" ]; then
        export ANDROID_HOME="$HOME/Library/Android/sdk"
        export ANDROID_SDK_ROOT="$HOME/Library/Android/sdk"
    elif [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_HOME="$HOME/Android/Sdk"
        export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
    else
        echo "[错误] 未找到 Android SDK！"
        echo ""
        echo "请先安装 Android Studio："
        echo "https://developer.android.com/studio"
        echo ""
        exit 1
    fi
fi

echo "[信息] 使用 Android SDK: $ANDROID_HOME"
echo "[信息] 开始编译..."
echo ""

# 使用 Gradle Wrapper 编译
./gradlew clean assembleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "  [成功] 编译完成！"
    echo "========================================"
    echo ""
    echo "APK 位置："
    echo "app/build/outputs/apk/release/app-release.apk"
    echo ""
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open app/build/outputs/apk/release/
    else
        xdg-open app/build/outputs/apk/release/
    fi
else
    echo ""
    echo "========================================"
    echo "  [失败] 编译出错！"
    echo "========================================"
    echo ""
    echo "建议使用 Android Studio 打开项目进行编译"
    echo ""
fi
