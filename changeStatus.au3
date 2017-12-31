
; Global source files
#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

; Set arguments via command line
$IP	 		  = $CmdLine[1]  ; I.e.  10.21.51.12
$PORT	 	  = $CmdLine[2]  ; I.e.  8081
$MAC_ADDRESS   = $CmdLine[3]  ; I.e.  00908f06c006
$DEVICE_NAME   = $CmdLine[4]  ; I.e.  changeStatus1
$PHONE_TYPE 	  = $CmdLine[5]  ; I.e.  430HD
$DOMAIN_NAME   = $CmdLine[6]  ; I.e.  cloudbond365b.com
$DEVICE_STATUS = $CmdLine[7]  ; I.e.  registered
$LOCATION 	  = $CmdLine[8]  ; I.e.  Location12
$PHONE_NUMBER  = $CmdLine[9]  ; I.e.  +97239764713

;~ $IP	 			  = "10.21.8.32"
;~ $PORT	 		  = "8081"
;~ $MAC_ADDRESS 	  = "00908f06c006"
;~ $DEVICE_NAME  	  = "changeStatus1"
;~ $PHONE_TYPE 	  = "430HD"
;~ $DOMAIN_NAME 	  = "cloudbond365b.com"
;~ $DEVICE_STATUS 	  = "registered"
;~ $LOCATION		  = "Location12"
;~ $PHONE_NUMBER	  = "+97239764713"

;~ Global data
Global $EMS_POST_URL	 = "/rest/v1/ipphoneMgrStatus/keep-alive"									; URL for the second POST query that sent when creating a user via POST query
Global $EMS_POST_VERSION = "UC_2.0.13.121"							 								; Version number for users which created via POST query
Global $EMS_USER_AGENT	 = "AUDC-IPPhone-" & $PHONE_TYPE & "_"  & $EMS_POST_VERSION & "/1.0.0000.0"	; User-agent for users which created via POST query
Global $EMS_PASSWORD	 = "3f6d0199102b53ca0a37a527f0efc221"										; Password for users which created via POST query
Global $EMS_USERNAME 	 = 'system'
Global $SESSION_ID   	 = "a033bb54"
Global $EMS_SUBNET   	 = "255.255.255.0"

; Logger levels
Global $CNT_LVL 	= 5
Global $SRC_LVL		= 4
Global $IN_FUNC_LVL	= 3
Global $FUNC_LVL	= 2
Global $TST_LVL		= 1
Global $TST_DSC_LVL	= 0



sendKeepAlivePacket($EMS_PASSWORD	 , $MAC_ADDRESS   , $IP         , $PHONE_TYPE 					   , _
				    $EMS_POST_VERSION, $EMS_USER_AGENT, $DEVICE_NAME, $DEVICE_NAME & "@" & $DOMAIN_NAME, _
					$DEVICE_STATUS   , $PHONE_NUMBER   , $DOMAIN_NAME, $LOCATION)

;--------------------------------------------------------------------------------------------------
Func sendKeepAlivePacket($userPassword, $macAdress  , $ipAddress, $phoneType, _
						 $phoneVersion, $userAgent  , $userName , $userId	, _
						 $deviceStatus, $phoneNumber, $sipProxy , $location)


	  ;~ Create the URL for sending
	  $targetUrl = "http://" & $IP & ":" & $PORT & $EMS_POST_URL
	  myToolTip("$targetUrl - " & $targetUrl, 150, 150, $IN_FUNC_LVL)

	  ;~ Create the Packet for sending
	  myToolTip("Stage 0 -  send packet with primary data to make the system be reasy for the 'create-user' method.", 150, 150, $IN_FUNC_LVL)
	  $PostData0 = "{" 												   			   & @LF & _
	   '"sessionId"'          & ":" & " " & '"' & $SESSION_ID 	& '",' 			   & @LF & _
	   '"emsUserName"' 	  	  & ":" & " " & '"' & $EMS_USERNAME & '",' 			   & @LF & _
	   '"emsUserPassword"'    & ":" & " " & '"' & $userPassword & '",' 			   & @LF & _
	   '"mac"' 			  	  & ":" & " " & '"' & $macAdress    & '",' 			   & @LF & _
	   '"ip"'	  			  & ":" & " " & '"' & $ipAddress	& '",' 			   & @LF & _
	   '"subnet"'		  	  & ":" & " " & '"' & $EMS_SUBNET   & '",' 			   & @LF & _
	   '"model"'		  	  & ":" & " " & '"' & $phoneType    & '",' 			   & @LF & _
	   '"fwVersion"'		  & ":" & " " & '"' & $phoneVersion & '",' 			   & @LF & _
	   '"userAgent"'		  & ":" & " " & '"' & $userAgent    & '",' 			   & @LF & _
	   '"userName"'		  	  & ":" & " " & '"' & $userName     & '",' 			   & @LF & _
	   '"userId"'		  	  & ":" & " " & '"' & $userId       & '",' 			   & @LF & _
	   '"status"'		  	  & ":" & " " & '"' & $deviceStatus & '",' 			   & @LF & _
	   '"phoneNumber"' 	      & ":" & " " & '"' & $phoneNumber  & '",' 			   & @LF & _
	   '"location"' 	  	  & ":" & " " & '"' & $location  	& '",' 			   & @LF & _
	   '"sipProxy"'		      & ":" & " " & '"' & $sipProxy     & '"'  		   	   & @LF & _
	   "}"
	  myToolTip("$PostData0 is:" & @CRLF & $PostData0, 150, 150, $IN_FUNC_LVL)

	  ;~ Send the packet
	  $Socket = _HTTPConnect($IP)
	  _HTTPPost($IP, $targetUrl, $Socket, $PostData0)
	  $recv = _HTTPRead($Socket,0)
	  myToolTip("$recv - " & $recv, 150, 150, $IN_FUNC_LVL)
	  _HTTPClose($Socket)

	  ;~ Wait after send
	  myCountDown(7000)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt, $xPos = 150, $yPos = 150, $level = $TST_LVL, $delay = 500)

   Local $target = " "
   For $i = 1 To $level Step 1
	  $target = $target & @TAB

   Next
   ConsoleWrite("" & $target &  _NowTime() & "  " & $txt & @TAB & @TAB & @LF)
   ToolTip(@LF & $txt & @LF & "---------------------------------------" , $xPos, $yPos)
   Sleep($delay)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myRandomPhoneNumber($length = "9")

   Local $sText = ""
   For $i = 1 To $length
		 $sText &= Chr(Random(48, 57, 1))

   Next
   myToolTip("myRandomPhoneNumber() $sText - " & $sText, 150, 150, $FUNC_LVL)
   return $sText

EndFunc
;--------------------------------------------------------------------------------------------------
Func myCountDown($target, $txt = "")

   For $i = 1000 To $target Step 1000
	  Sleep(1000)
	  ToolTip($txt & "(" & $i & "/" & $target, 150, 150)

   Next

EndFunc