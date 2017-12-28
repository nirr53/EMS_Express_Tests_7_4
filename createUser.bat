set IP=%1
set PORT=%2
set usersNumber=%3
set usersPrefixName=%4
set domainName=%5
set createStatus=%6
set phoneType=%7
set regionName=%8
set location=%9

start /d "C:\Users\nirk\Desktop\myEclipseProjects\EMS_Tests_7_4\createUsersViaPost.exe" %IP% %PORT% %usersNumber% %usersPrefixName% %domainName% %createStatus% %phoneType% %regionName% %location%
