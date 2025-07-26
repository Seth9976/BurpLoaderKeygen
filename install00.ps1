# ============================================================================
# Burp Suite Pro 安装脚本 (修改版)
#
# 主要修改:
# 1. 不再自动下载和安装JDK/JRE，而是使用下面指定的本地Java路径。
# 2. 不再自动下载Burp Suite Jar包，而是使用同目录下的指定Jar文件。
# 3. 请确保 loader.jar 和 burpsuite_pro_v2025.6.3.jar 与此脚本在同一目录下。
#
# 注意: 密钥生成是交互式的, 脚本会自动打开密钥生成器窗口,
# 您需要根据README中的指南手动完成激活过程。
# ============================================================================

# --- 请根据您的环境修改以下变量 ---
# 指定您本地 JDK 21 的 java.exe 完整路径
$javaPath = "E:\Download\JAVA\JDK\jdk-21.0.7.6\bin\java.exe"
# 指定要使用的 Burp Suite Jar 文件名
$burpJar = "burpsuite_pro_v2025.7.1.jar"
# --- 变量配置结束 ---

# 设置 Wget 进度条为静默，以防万一有其他下载操作
$ProgressPreference = 'SilentlyContinue'

# 检查指定的 java.exe 是否存在
if (-not (Test-Path $javaPath)) {
    Write-Host "错误: 在指定路径未找到 java.exe: $javaPath" -ForegroundColor Red
    Write-Host "请修改脚本中的 `$javaPath` 变量为您正确的JDK路径。" -ForegroundColor Red
    exit
}
Write-Host "将使用Java环境: $javaPath" -ForegroundColor Green

# 检查指定的 Burp Suite Jar 是否存在
if (-not (Test-Path $burpJar)) {
    Write-Host "错误: 在当前目录未找到指定的Burp Jar: $burpJar" -ForegroundColor Red
    Write-Host "请确保 `$burpJar` 文件与本脚本在同一目录下。" -ForegroundColor Red
    exit
}
Write-Host "将使用Burp Suite Jar: $burpJar" -ForegroundColor Green


# 如果不存在，则下载 loader.jar [cite: 55]
if (-not (Test-Path loader.jar)){
    echo "`n正在下载 Loader ...."
    Invoke-WebRequest -Uri "https://github.com/xiv3r/Burpsuite-Professional/raw/refs/heads/main/loader.jar" -OutFile loader.jar
    echo "`nLoader 下载完成"
}else{
    echo "`nLoader 已存在"
}

# 为执行命令创建 Burp.bat 文件
if (Test-Path burp.bat) {rm burp.bat}
# 使用指定的Java路径和Burp Jar文件名 [cite: 54]
$command = "`"$javaPath`" --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm.Opcodes=ALL-UNNAMED -javaagent:`"$pwd\loader.jar`" -noverify -jar `"$pwd\$burpJar`""
$command | add-content -path Burp.bat
echo "`nBurp.bat 文件已创建"


# 创建 Burp-Suite-Pro.vbs 文件用于后台执行
if (Test-Path Burp-Suite-Pro.vbs) {
   Remove-Item Burp-Suite-Pro.vbs}
echo "Set WshShell = CreateObject(`"WScript.Shell`")" > Burp-Suite-Pro.vbs
add-content Burp-Suite-Pro.vbs "WshShell.Run chr(34) & `"$pwd\Burp.bat`" & Chr(34), 0"
add-content Burp-Suite-Pro.vbs "Set WshShell = Nothing"
echo "`nBurp-Suite-Pro.vbs 文件已创建."

# 使用指定的Java路径启动密钥生成器
echo "`n`n正在启动密钥生成器 .... "
start-process "$javaPath" -argumentlist "-jar loader.jar"

# 使用指定的Java路径启动 Burp Suite Professional
echo "`n`n正在启动 Burp Suite Professional"
echo "请根据弹出的Keygen窗口和Burp Suite激活向导手动完成激活。"
# 使用 `&` 调用操作符来执行路径中可能包含空格的命令
& "$javaPath" --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED --add-opens=java.base/jdk.internal.org.objectweb.asm.Opcodes=ALL-UNNAMED -javaagent:"loader.jar" -noverify -jar "$burpJar"