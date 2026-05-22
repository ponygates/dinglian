@echo off
chcp 65001 >nul
echo ========================================
echo   定联管理系统 - APK 一键编译脚本
echo ========================================
echo.

REM 检查是否有 Android SDK
if not exist "%LOCALAPPDATA%\Android\Sdk" (
    echo [错误] 未找到 Android SDK！
    echo.
    echo 请先安装 Android Studio：
    echo https://developer.android.com/studio
    echo.
    pause
    exit /b 1
)

REM 设置环境变量
set ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk
set ANDROID_SDK_ROOT=%LOCALAPPDATA%\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\build-tools\28.0.3

echo [信息] 开始编译...
echo.

REM 使用 Gradle Wrapper 编译
call gradlew.bat clean assembleRelease

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo   [成功] 编译完成！
    echo ========================================
    echo.
    echo APK 位置：
    echo app\build\outputs\apk\release\app-release.apk
    echo.
    echo 正在打开输出目录...
    explorer app\build\outputs\apk\release\
) else (
    echo.
    echo ========================================
    echo   [失败] 编译出错！
    echo ========================================
    echo.
    echo 建议使用 Android Studio 打开项目进行编译
    echo.
)

pause
