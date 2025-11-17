@echo off
setlocal

echo Creating folder...

:: Set the name of the folder
set "folderName=Grupper_Beta_7"

:: Create the folder
mkdir "%folderName%"

:: Copy the files to the new folder
copy "Grupper.jar" "%folderName%"
copy "resources.zip" "%folderName%"
copy "wordlist.txt" "%folderName%"
copy "Readme.md" "%folderName%\Readme.txt"
copy "LICENSE" "%folderName%\License.txt"
copy "LICENSE-APACHE.txt" "%folderName%\License-Apache.txt"
mkdir "%folderName%\mod_list"
copy "mod_list\*.txt" "%folderName%\mod_list"

echo Creating zip file...

:: Check if 7-Zip is intalled in the PATH variable
where 7z >nul 2>nul
if errorlevel 1 (
    echo 7-Zip is not installed in your PATH. Please, install 7-Zip and add it to your PATH.
    exit /b
)

:: Compress the folder in a zip file withe today's date
7z a "%folderName%.zip" "%folderName%\*"

:: Erase the created folder (optional)
rmdir /s /q "%folderName%"

echo zip file sucessfully created: %folderName%.zip
endlocal