
#include <Date.au3>
Global $testerLevel			    = 1
Global $functionLevel		    = 2
Global $innerFunctionLevel	    = 3



disablePopUpDialog()

;--------------------------------------------------------------------------------------------------
Func  disablePopUpDialog($maxTab = 15)

   myToolTip("enter disablePopUpDialog()", 150, 150, $functionLevel)
   Local $hIE = WinGetHandle("[Class:IEFrame]")
   Sleep(1000)
   myToolTip("$hIE is " & $hIE, 150, 150, $innerFunctionLevel)
   Local $hCtrl = ControlGetHandle($hIE, "", "[ClassNN:DirectUIHWND1]")
   Sleep(1000)
   myToolTip("$hCtrl is " & $hCtrl, 150, 150, $innerFunctionLevel)
   For $i = 1 To $maxTab Step 1
	  ToolTip("(" & $i & "/" & $maxTab, 150, 150)
	  ControlSend($hIE, "", $hCtrl, "{TAB}")
	  Sleep(500)
	  ControlSend(WinGetHandle("[Class:IEFrame]"), "", ControlGetHandle($hIE, "", "[ClassNN:DirectUIHWND1]"), "S")
	  ToolTip("(" & $i & "/" & $maxTab, 150, 150)
	  ControlSend(WinGetHandle("[Class:IEFrame]"), "", ControlGetHandle($hIE, "", "[ClassNN:DirectUIHWND1]"), "s")
	  Sleep(2000)

   Next
   myToolTip("exit disablePopUpDialog()", 150, 150, $functionLevel)

EndFunc
;--------------------------------------------------------------------------------------------------
Func myToolTip($txt, $xPos = 150, $yPos = 150, $level = $testerLevel, $delay = 500)

   Local $target = " "
   For $i = 1 To $level Step 1
	  $target = $target & @TAB

   Next
   ConsoleWrite("" & $target &  _NowTime() & "  " & $txt & @TAB & @TAB & @LF)
   ToolTip(@LF & $txt & @LF & "---------------------------------------" , $xPos, $yPos)
   Sleep($delay)

EndFunc