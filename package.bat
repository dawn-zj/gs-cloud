@echo off

cd %~dp0

echo -------------------Start Build Java-------------------
echo.
call mvn clean package -Dmaven.test.skip=true
echo -------------------End Build Java-------------------
echo.

cd %~dp0/gs-ui

if not exist node_modules (
	echo -------------------Start Install Vue-------------------
	echo.
	call npm install
	echo -------------------End Install Vue-------------------
	echo.
)
echo -------------------Start Build Vue-------------------
echo.
call npm run build:prod
echo -------------------End Build Vue-------------------
echo.

pause