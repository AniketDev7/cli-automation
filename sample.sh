echo >/dev/null # >nul & GOTO WINDOWS & rem ^echo 'Processing for Linux'# ***********************************************************# * NOTE: If you modify this content, be sure to remove carriage returns () # *       from the Linux part and leave them in together with the line feeds 
# *       (\n) for the Windows part. In summary:# *           New lines in Linux: \n# *           New lines in Windows: \n # ***********************************************************
# Do Linux Bash commands here... for example:ipconfig# Then, when all Linux commands are complete, end the sipt with 'exit'...exit 0- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
:WINDOWS
echo "Processing for Windows"

REM Do 
ipconfig
REM Then