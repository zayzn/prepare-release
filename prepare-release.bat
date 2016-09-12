@echo off
cd .
set /p source=Source directory: 
java -jar PrepareRelease.jar "%source%"
pause
