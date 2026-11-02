@echo off
set VERSION=8.11.1
set BASE=%USERPROFILE%\.gradle\soni-wrapper\gradle-%VERSION%
if not exist "%BASE%in\gradle.bat" (
  if not exist "%USERPROFILE%\.gradle\soni-wrapper" mkdir "%USERPROFILE%\.gradle\soni-wrapper"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$u='https://services.gradle.org/distributions/gradle-%VERSION%-bin.zip'; Invoke-WebRequest $u -OutFile '%USERPROFILE%\.gradle\soni-wrapper\gradle.zip'; Expand-Archive -Force '%USERPROFILE%\.gradle\soni-wrapper\gradle.zip' '%USERPROFILE%\.gradle\soni-wrapper'; Remove-Item '%USERPROFILE%\.gradle\soni-wrapper\gradle.zip'"
)
call "%BASE%in\gradle.bat" %*
