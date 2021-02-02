# BPArty
BPA Software Engineering project 2020-2021
BPArty is a local multiplayer party board game that involves two players 
alternating turns and trying to make it to the end with the most points!

This project was created with the collaboration of four Java programmers
for the BPA Software Engineering event (2021)

Compilation/Building directions:
	
	Unzip the BPARTY file from google drive
	bpa-game holds all the files necessary to run the program
	there is a jar file there to double click, however it is not set up with the database, 
	so the full experience is accessed through the compiler.
	
	For Eclipse:

	When all the files are integrated, add the jar files to the classpath, by going to
	Project -> Preferences -> Java Build Path and adding all the external jars
	To run the main class, right click the project, go to  Run as -> Run configurations
	Make a new configuration in the top left, make the project my-gdx-game-desktop
	Set the main class as DesktopLauncher
	Click on the arguments tab, scroll down, and set the workspace as other -> core assets
	Once this is all setup and the files are imported, the project should be ready to run
	
	Current bugs with the jar file version:
		No database leads to no saving,
		Because of no saving, if the player lands on a versus tile, the game crashes
		Therefore the minigames are disabled in this version
 		
Features:
    
         Local multiplayer party board game
 	- Two player board with minigames
		-Danger Noodles
		-Pong
		-Desert Drop
		-Cream Catch
		-Balloon Pop
     	-Lots of different tiles that inflict different consequences
		-Blue tile +2 points
		-Red tile -2 points
		-Green tile does nothing
		-Versus tile starts a minigame
		-Red event tile -3 to -5 points
		-Blue event tile +3 to +5 points
		-Treasure tile open a chest for 1 to 6 points
		-Midway tile mandatory minigame for +15 points
		-Bad event tile does a random affect on the players
		-Star tile +5 points
         
	
         Java programming utilizing the LibGDX framework
         Crash reporting available via JavaMail
         Database storing via SQLite
         Logging via log4j2
         Music and SFX - https://www.zapsplat.com/ and GarageBand
