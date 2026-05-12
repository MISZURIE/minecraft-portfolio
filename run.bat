@echo off
echo ==========================================
echo   MINECRAFT PORTFOLIO - Java First Person
echo   Ploychomphoo Kathinthet
echo ==========================================
echo.
echo Compiling...
set FX=javafx-sdk-21.0.2\lib
javac --module-path %FX% --add-modules javafx.controls -d out -sourcepath src\main\java src\main\java\com\portfolio\minecraft\*.java
if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo.
echo ==========================================
echo   CONTROLS:
echo   WASD         = Walk around
echo   Mouse        = Look around
echo   Left Click   = Break block
echo   Right Click  = Place block / Interact
echo   Q/E / Scroll = Cycle block type
echo   SPACE        = Jump
echo   F            = Toggle Fly mode
echo   ESC          = Close panel
echo.
echo   PORTFOLIO INFO BLOCKS:
echo   Find glowing colored blocks around the
echo   world and RIGHT-CLICK them to see
echo   portfolio info!
echo ==========================================
echo.
java --module-path %FX% --add-modules javafx.controls --enable-native-access=javafx.graphics -cp out com.portfolio.minecraft.MinecraftPortfolio
pause
