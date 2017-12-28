; Global source files
#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

;~ Internal Parameters
;~ $IP	 		 = "10.21.8.8"										; IP of the EMS-IPP manager				    (I.e. 10.21.8.32)
;~ $PORT	 	 = "80"												; Port of the computer that sends the alarm (I.e 8081)
;~ $MAC		 = "00908fbd4350"									; MAC of regsisterd phone 					(I.e 00908fdf411a)
;~ $NAME		 = "IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE22"	; Name of alarm								(I.e IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE)
;~ $DESCRIPTION = "sev5"											; Description of alarm						(I.e dateTest)
;~ $DATE		 = "2017-07-217T12:24:18"							; Date of alarm 							(I.e 2017-07-217T12:24:18)
;~ $INFO1 		 = "123"											; Description #1 alarm						(I.e. info1)
;~ $INFO2 		 = "456"											; Description #2 alarm						(I.e. info2)
;~ $SEVERITY    = "warning"										; Sevrity of alarm							(I.e. 5)

;~ External parametsrs
$IP	 		 = $CmdLine[1]										; IP of the EMS-IPP manager				    (I.e. 10.21.8.32)
$PORT	 	 = $CmdLine[2]										; Port of the computer that sends the alarm (I.e 8081)
$MAC		 = $CmdLine[3]										; MAC of regsisterd phone 					(I.e 00908fdf411a)
$NAME		 = $CmdLine[4]										; Name of alarm								(I.e IPPHONE CONFERENCE SPEAKER CONNECTION FAILURE)
$DESCRIPTION = $CmdLine[5]										; Description of alarm						(I.e dateTest)
$DATE		 = $CmdLine[6]										; Date of alarm 							(I.e 2017-07-217T12:24:18)
$INFO1 		 = $CmdLine[7]										; Description #1 alarm						(I.e. info1)
$INFO2 		 = $CmdLine[8]										; Description #2 alarm						(I.e. info2)
$SEVERITY    = $CmdLine[9]										; Sevrity of alarm							(I.e. 5)

;~ Translate sevrity level to Number
ConsoleWrite("" & @TAB &  _NowTime() & "  " & "$DESCRIPTION - <" & $DESCRIPTION & ">" & @TAB & @TAB & @LF)
ConsoleWrite("" & @TAB &  _NowTime() & "  " & "Severity before - <" & $SEVERITY & ">" & @TAB & @TAB & @LF)
If 	   (StringInStr($SEVERITY, "info")) Then
   $SEVERITY = "1"
EndIf
If (StringInStr($SEVERITY, "warning"))  Then
   $SEVERITY = "2"
EndIf
If (StringInStr($SEVERITY, "minor"))    Then
   $SEVERITY = "3"
EndIf
If (StringInStr($SEVERITY, "Major"))    Then
   $SEVERITY = "4"
EndIf
If (StringInStr($SEVERITY, "Critical")) Then
   $SEVERITY = "5"
EndIf
ConsoleWrite("" & @TAB &  _NowTime() & "  " & "Severity after - " & $SEVERITY & ">" & @TAB & @TAB & @LF)


; Rewrite empty info String
If 	   (StringInStr($INFO1, "empty")) Then
   $INFO1 = ""
EndIf
If 	   (StringInStr($INFO2, "empty")) Then
   $INFO2 = ""
EndIf
ConsoleWrite("$INFO1 <" & $INFO1 & ">" & @TAB & @TAB & @LF)
ConsoleWrite("$INFO2 <" & $INFO2 & ">" & @TAB & @TAB & @LF)

;~ Create the query using the given paramaters
$PostData0 = "{" 															   						& @LF & _
				  '"sessionId"'       & ":" & " " & '"' & "5f20db06" 						 & '",' & @LF & _
				  '"emsUserName"' 	  & ":" & " " & '"' & "system" 							 & '",' & @LF & _
				  '"emsUserPassword"' & ":" & " " & '"' & "db2362e6ffd4bc4f36321cddbd6d098f" & '",' & @LF & _
				  '"alerts"' 		  & ":" & " " & '[' 						   			 		& @LF & _
														 '{' 								 		& @LF & _
															 '"uniqueId": 2,' 										& @LF & _
															 '"name": "'  		   & $NAME 	      & '",' 			& @LF & _
															 '"description": "'    & $DESCRIPTION & '",' 			& @LF & _
															 '"date": "' 		   & $DATE 		  & '",'  			& @LF & _
															 '"severity": "'       & $SEVERITY    & '",' 			& @LF & _
															 '"source": "IPPhone/' & $MAC         & '",' 			& @LF & _
															 '"type": 2,'											& @LF & _
															 '"probableCause": 5,'									& @LF & _
															 '"additionalInfo1": "' & $INFO1      & '",'			& @LF & _
															 '"additionalInfo2": "' & $INFO2      & '"'			& @LF & _
														  '}' 										 & @LF & _
												    ']' 											 & @LF & _
			 "}"

;~ Send the query
ConsoleWrite("" & @TAB &  _NowTime() & "  " & "$PostData1 is:" & @CRLF & $PostData0 & @TAB & @TAB & @LF)
$target2 = "http://" & $IP & ":" & $PORT & "/ipprest/lync_auto_prov.php/rest/v1/ipphoneMgrAlert/alert "
ConsoleWrite("" & @TAB &  _NowTime() & "  " & "$target2 - " & $target2 & @TAB & @TAB & @LF)
$Socket = _HTTPConnect($IP)
HTTPJasonPost($IP, $target2, $Socket, $PostData0)
$recv = _HTTPRead($Socket,0)
ConsoleWrite("$recv - " & $recv & @TAB & @TAB & @LF)
_HTTPClose($Socket)

;--------------------------------------------------------------------------------------------------
Func markEnd($sFilePath)

    _FileCreate($sFilePath)
	Sleep(2000)
    Local $hFileOpen = FileOpen($sFilePath, $FO_READ + $FO_OVERWRITE)
	Sleep(2000)
    FileWrite($hFileOpen, "--" & @CRLF)
	Sleep(2000)
    FileClose($hFileOpen)
	Sleep(2000)
    Return True

EndFunc
;--------------------------------------------------------------------------------------------------
Func HTTPJasonPost($host, $page, $socket = -1, $data = "")
    dim $command

    If $socket == -1 Then
        If $_HTTPLastSocket == -1 Then
            SetError(1)
            Return
        EndIf
        $socket = $_HTTPLastSocket
    EndIf

    dim $datasize = StringLen($data)

    $command = "POST "&$page&" HTTP/1.1"&@CRLF
    $command &= "Host: " &$host&@CRLF
    $command &= "User-Agent: "&$_HTTPUserAgent&@CRLF
    $command &= "Connection: close"&@CRLF
    $command &= "Content-Type: application/jason"&@CRLF
    $command &= "Content-Length: "&$datasize&@CRLF
    $command &= ""&@CRLF
    $command &= $data&@CRLF

    dim $bytessent = TCPSend($socket, $command)

    If $bytessent == 0 Then
        SetExtended(@error)
        SetError(2)
        return 0
    EndIf

    SetError(0)
    Return $bytessent
EndFunc