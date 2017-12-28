#include <Constants.au3>
#include <MsgBoxConstants.au3>
#include <IE.au3>
#include <Array.au3>
#include <Date.au3>
#include <File.au3>
#include <http.au3>

MsgBox(64, "UTF-8", Utf82Unicode("дцья"), 5)

MsgBox(64, "UTF-8-2", Utf82Unicode2("дцья"), 5)
MsgBox(64, "UTF-8-2", Asc2Unicode("дцья"), 5)




Exit 1


Func Utf82Unicode($Utf8String)
    Local $BufferSize = StringLen($Utf8String) * 2
    Local $Buffer = DllStructCreate("byte[" & $BufferSize & "]")
    Local $Return = DllCall("Kernel32.dll", "int", "MultiByteToWideChar", _
        "int", 65001, _
        "int", 0, _
        "str", $Utf8String, _
        "int", StringLen($Utf8String), _
        "ptr", DllStructGetPtr($Buffer), _
        "int", $BufferSize)
    Local $UnicodeString = StringLeft(DllStructGetData($Buffer, 1), $Return[0] * 2)
    $Buffer = 0
    Return $UnicodeString
 EndFunc


 Func Utf82Unicode2($Utf8String)
    Local $BufferSize = StringLen($Utf8String) * 2
    Local $Buffer = DllStructCreate("byte[" & $BufferSize & "]")
    Local $Return = DllCall("Kernel32.dll", "int", "MultiByteToWideChar", _
        "int", 65001, _
        "int", 0, _
        "str", $Utf8String, _
        "int", StringLen($Utf8String), _
        "ptr", DllStructGetPtr($Buffer), _
        "int", $BufferSize)
    Local $UnicodeString = StringLeft(DllStructGetData($Buffer, 1), $Return[0] * 2)
    $Buffer = 0
    Return $UnicodeString



 EndFunc

 Func Asc2Unicode($input)
    If StringLen($input) <> 1 Then Return SetError(-1, -1, -1)
    Local $FullUniStr = DllStructCreate('byte[3]')
    Local $Buffer = DllStructCreate('byte[2]', DllStructGetPtr($FullUniStr) + 2)
    Local $Return = DllCall('Kernel32.dll', 'int', 'MultiByteToWideChar', _
            'int', 0, _
            'int', 0, _
            'str', $input, _
            'int', StringLen($input), _
            'ptr', DllStructGetPtr($Buffer, 1), _
            'int', 2)
    DllStructSetData($FullUniStr, 1, 0xFF, 1)
    DllStructSetData($FullUniStr, 1, 0xFE, 2)
    Local $temp = DllStructGetData($Buffer, 1)
    Return '\u' & StringMid($temp, 5, 2) & StringMid($temp, 3, 2)
EndFunc   ;==>Asc2Unicode