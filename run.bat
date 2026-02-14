@echo off
REM Find the launcher JAR dynamically
setlocal enabledelayedexpansion

set LAUNCHER_JAR=
for %%f in (surf-icon-generator-launcher-*.jar) do (
    set LAUNCHER_JAR=%%f
    goto found
)

:found
if "%LAUNCHER_JAR%"=="" (
    echo Could not find surf-icon-generator-launcher JAR.
    pause
    exit /b 1
)

echo Launching !LAUNCHER_JAR!...
java -jar "!LAUNCHER_JAR!"
pause