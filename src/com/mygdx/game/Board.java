package com.mygdx.game;

import java.io.IOException;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

/**
 * 
 * Contains all of the information and input required to run the board
 * 
 */
public class Board implements Screen, InputProcessor {

	private static final Logger LOGGER = LogManager.getLogger(Board.class);
	
	private SQLiteConnect sqlite = new SQLiteConnect();
	
	Texture texture;
	
	//Camera for perspective
	OrthographicCamera camera;
	
	//Spritebatch to draw the sprites
	SpriteBatch spriteBatch;
	
	//Integers related to rolling the dice, including holding values
	int diceNumber;
	int oldDice;
	int numbNeeded;
	int oldNumb;
	int oldDiceLoop = diceNumber;
	
	//Space integers hold the value for where the character is, initial position being (27,40), oldSpace values are held to nullify where the characters position is
	int spaceY = sqlite.positionY();
	int spaceX = sqlite.positionX();
	int oldSpaceY = spaceY;
	int oldSpaceX = spaceX;
	
	//Holds the values for the players points
	int iceCreamPoints = sqlite.points1();
	int popsiclePoints = sqlite.points2();
	
	//Holds string for which character is currently playing
	String currentPlayer;
	String notCurrentPlayer;
	
	//Value for determining which minigame to run
	int randGame;
	
	//Counter for tutorial, starts at 0
	int tutorialCounter = 0;
	
	//Allows for multiple inputs, i.e pressing, scrolling, zooming, dragging, etc...
	InputMultiplexer inputMultiplexer = new InputMultiplexer();
	
	//Holds map, isometric rendering, and game screen
	private TiledMap tiledMap;
	private IsometricTiledMapRenderer isometricRenderer;
	final BPArty game;
	
	//All the text buttons
	TextButton diceBtn, resumeBtn, quitBtn, saveBtn, mainMenuBtn, leftBtn, rightBtn, yesBtn, noBtn, restartBtn, playBtn, chestBtn, chestBtn2, chestBtn3, chestBtn4, chestBtn5, chestBtn6, sendCrashBtn, defaultQuitBtn;
	
	//Stage
	Stage stage;
	
	//The font used for the HUD
	BitmapFont font;
	
	//The different tables used for the HUD, basically allows for labeling the screen
	Table table, table2, table3;
	
	//The skin used for the table
	Skin skin;
	
	//The pause menu stuff, toggle determines if pause menu is active, and the slider/container allows for the volume to change
	int toggle = 0;
	int sliderValue = sqlite.volume();
	Slider volumeSlider;
	Container<Slider> container;
	
	//All the labels used for the HUD/information relaying
	Label sliderLabel, volumeLabel, turnLabel, playerLabel, pointsLabel1, pointsLabel2, player1, player2, textBox, introduction, explainChar, blueTile, starTile, versusTile, eventBTile, treasureTile, greenTile, eventRTile, decisionTile, specialTile, eventBadTile, endTile;
	
	//Variable mainly used for the textBox label
	String text = "";
	
	//Ints to determine turn, playing status, or tutorial status
	int turn = sqlite.turn();
	int playing = sqlite.playing();
	int tutorial = sqlite.tutorial();
	
	int minigameInt = 0;
	
	boolean doublePoints = false, triplePoints = false, playMusic = true;
	
	//Draws the shapes in the tutorial
	ShapeRenderer shapeRenderer;
	
	//Creates cells on the different layers
	TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
	TiledMapTileLayer.Cell cell2 = new TiledMapTileLayer.Cell();
	
	AudioManager audioManager = new AudioManager();
	
	//Lots of arrays dedicated towards locating positions of all the tiles, X/Y coordinates on the layer
	int[] blueX = {27,27,27,27,27,29,33,33,31,33,34,42,42,42,48,52,51,58,55,53};
	int[] blueY = {41,42,43,44,46,47,48,51,54,60,60,52,51,50,51,49,56,56,64,52};
	
	int[] treasureX = {30,35,33,42,42,49,53,55,58,55,51,51,51,53,55,56};
	int[] treasureY = {47,53,59,60,49,47,49,58,59,65,53,55,57,58,58,57};
	
	int[] starX = {27,35,50,54};
	int[] starY = {45,57,47,58};
	
	int[] versusX = {27,33,35,31,39,43,47,53,51,58};
	int[] versusY = {47,47,56,56,60,49,51,51,54,58};
	
	int[] eventBlueX = {28,40,42,52,58,57};
	int[] eventBlueY = {47,60,55,53,57,65};
	
	int[] decisionX = {33,45};
	int[] decisionY = {52,49};
	
	int[] redX = {33,35,37,42,49,48,52};
	int[] redY = {50,52,60,54,51,47,58};
	
	int[] eventRedX = {35,35,35,41,45,55,56,56,58,55,56};
	int[] eventRedY = {54,55,60,60,47,53,53,54,60,62,58};
	
	int[] eventBadX = {46,51,51,56,54};
	int[] eventBadY = {47,49,58,62,53};
	
	int[] triplePointsX = {42};
	int[] triplePointsY = {57};
	
	/**
	 * Constructor for Board
	 * @param game how the program knows which screen to switch to
	 * 
	 */
	public Board(final BPArty game) {
		this.game = game;
		
		//Load in the board created by Tiled editor
		tiledMap = new TmxMapLoader().load("board2.tmx");
		
		//Set to isometric rendering, gives perspective
		isometricRenderer = new IsometricTiledMapRenderer(tiledMap);
		
		//Create camera
		camera = new OrthographicCamera();
		
		//Sets camera perspective, position, and initial zoom
		camera.setToOrtho(false,2000,2000);
		camera.position.x = 1601;
		camera.position.y = 209;
		camera.zoom = .2f;
		
		//Create font, load in font created
		font = new BitmapFont(Gdx.files.internal("ArialBlack.fnt"));
		
		//Processor created and set for input
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//Texture for characters, both in one png
		texture = new Texture(Gdx.files.internal("BestBuds.png"));
		
		//Creates new layer, has all the tiles for the map
		TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
		
		//Create layer for ice cream and put on cell as texture region, puts the layer above the map layer
		tiledMap.getLayers().add(tileLayer);
		TextureRegion textureRegion = new TextureRegion(texture, 44, 32);		
		tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);						
		cell.setTile(new StaticTiledMapTile(textureRegion));
		tileLayer.setCell(spaceX, spaceY, cell);
		
		//Creates the spriteBatch and shapeRender objects
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
			
		//Random object
		Random rNumb = new Random();
		
		//Determines who gets the first turn
		int randomTurn = 1+rNumb.nextInt(2);
		if (randomTurn == 1) {
			turn = 1;
		} else  if (randomTurn == 2){
			turn = 2;
		}
		
		//Creates play/restart label for end of tutorial, player label for turn, and numbNeeded for gate at end of loop on board
		restartBtn = new TextButton("Restart", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
		playBtn = new TextButton("Play", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
		
		playerLabel = new Label(String.format("%s", currentPlayer), new Label.LabelStyle(font, Color.WHITE));
		
		numbNeeded = 1+rNumb.nextInt(6);
					
		if (playing == 1) {
			show();
		}
		
		LOGGER.debug("Constructor initiated");
	}

	/**
	 * 
	 * Draws the line and circle at the designated coordinates, used during tutorial to draw attention
	 * calls show method as well so that it appears
	 * 
	 * @param x1 first x cood of line
	 * @param y1 first y coord of line
	 * @param x2 second x coord of line
	 * @param y2 second x coord of line
	 * @param circleX x coord of top left of circle
	 * @param circleY y coord of top left of circle
	 */
	public void tutorialShapes(float x1, float y1, float x2, float y2, float circleX, float circleY) {
		LOGGER.debug("Tutorial shape #" + tutorialCounter + " created");
		show();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rectLine(new Vector2(x1, y1), new Vector2(x2, y2), 1);
		shapeRenderer.circle(circleX, circleY, 50);
		shapeRenderer.end();		
	}

	@Override
	public void render(float delta) {
		
		//Debugging purposes
		//System.out.println(Gdx.input.getX());
		//System.out.println(Gdx.input.getY());	

		//Clear screen and update camera
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		isometricRenderer.setView(camera);
		isometricRenderer.render();
		
		audioManager.playBoard();
		
		//Set the label for the player below the turn label 
		if (turn == 1) {
			currentPlayer = "Ice Cream";
			notCurrentPlayer = "Popsicle";
			playerLabel.setText("Ice Cream");
		} else {			
			currentPlayer = "Popsicle";
			notCurrentPlayer = "Ice Cream";
			playerLabel.setText("Popsicle ");
		}
		
		if (tutorial == 1) {
		//Goes through the entire tutorial, each time the user clicks, it makes the tutorial counter go up by one
		if (tutorialCounter == 0) {	
			camera.position.x = 1601;
			camera.position.y = 209;
			tutorialShapes(318, 356, 500, 356, 280, 390);
    	} else if (tutorialCounter == 1) {
			tutorialShapes(455, 396, 500, 356, 440, 445);
    	} else if (tutorialCounter == 2) {
			tutorialShapes(590, 460, 503, 356, 585, 510);
    	} else if (tutorialCounter == 3) {
			tutorialShapes(710, 529, 700, 361, 724, 576);
    	} else if (tutorialCounter == 4) {
			tutorialShapes(798, 484, 700, 361, 800, 535);
    	} else if (tutorialCounter == 5) {
			tutorialShapes(900, 450, 697, 361, 933, 485);
    	} else if (tutorialCounter == 6) {
			tutorialShapes(1000, 410, 697, 361, 1010, 460);
    	} else if (tutorialCounter == 7) {
    		//Camera position changes to explain other tiles/mechanics
    		camera.position.x = 1900;
    		camera.position.y = 370;
			tutorialShapes(595, 250, 697, 100, 545, 260);
    	} else if(tutorialCounter == 8) {
			tutorialShapes(863, 250, 697, 100, 905, 275);
    	} else if (tutorialCounter == 9) {
    		//And again here
    		camera.position.x = 2100;
    		camera.position.y = 180;
			tutorialShapes(841, 480, 697, 375, 890, 475);
    	} else if (tutorialCounter == 10) {
			tutorialShapes(520, 91, 697, 365, 470, 77);
    	} else if (tutorialCounter == 11) {
    		//Rendering stops, simply explains the mechanics of a section
    		camera.position.x = 2400;
    		camera.position.y = 110;
    		show();
    	} else if (tutorialCounter == 12) {
    		camera.position.x = 2700;
    		camera.position.y = 150;
    		show();
    	} else if (tutorialCounter == 13) {
    		show();
    	}
		
		//Asked at the end of the tutorial, restart sets counter to 0 and pressing play sets playing to true and starts the game
		if (restartBtn.isPressed()) {
				LOGGER.info("Tutorial restarted.");
				tutorialCounter = 0;
				restartBtn.remove();
				playBtn.remove();
				show();
		} else if (playBtn.isPressed()) {
				LOGGER.info("Tutorial completed.");
				tutorial = 0;
				playing = 1;
				camera.position.x = 1601;
				camera.position.y = 209;
				restartBtn.remove();
				playBtn.remove();
				show();
		}
	}
		//Asked at the beginning, yes button to start the tutorial, no to start the game
		if (playing == 0) {
			if (yesBtn.isPressed()) {				
				LOGGER.info("Tutorial started.");
				tutorial = 1;
				yesBtn.remove();
				noBtn.remove();
				show();			
			} else if (noBtn.isPressed()) {
				LOGGER.info("Game started.");
				playing = 1;
				yesBtn.remove();
				noBtn.remove();
				show();
			}
		}
		
		//Toggles the pause menu on and off
		if (playing == 1) {
			
		try {	
			
		switch(toggle) {
		
		case 0:	
			
			//Sets pause HUD to invisible and board HUD to visible
			table2.setVisible(false);
			diceBtn.setVisible(true);
			//If statement to make the decision tile buttons appear
			if (spaceX == 33 && spaceY == 52) {
				leftBtn.setVisible(true);
				rightBtn.setVisible(true);
			}
			//Restricts camera to inside those X and Y positions, if outside, jerks it back inside.
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				if (camera.position.y >= 360) {
					camera.position.y += 0;
					camera.position.y = 359.9999f;
				}
				else if (camera.position.x >=2500) {
					camera.position.x += 0;
					camera.position.x = 2499.999f;
				} else if (camera.position.x <= 1600) {
					camera.position.x += 0;
					camera.position.x = 1600.001f;
				} else if (camera.position.y<=100) {
					camera.position.y += 0;
					camera.position.y = 100.001f;	
				}
				else {
					camera.position.x -= Gdx.input.getDeltaX() * 1.5f;
					camera.position.y += Gdx.input.getDeltaY() * 1.5f;
				}
			}
			
			//key for toggling on pause menu
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				LOGGER.info("Pause menu pulled up.");
				toggle++;	
			}	
			
			break;
			
		case 1:
			
			//Sets board HUD to invisible, including left/right buttons if on decision tile, and makes pause menu visible
			
			diceBtn.setVisible(false);
			diceBtn.setTouchable(Touchable.disabled);
			table2.setVisible(true);
			leftBtn.setVisible(false);
			rightBtn.setVisible(false);
			
			if (diceNumber > 0) {
				mainMenuBtn.setTouchable(Touchable.disabled);
				saveBtn.setTouchable(Touchable.disabled);
			} else {
				mainMenuBtn.setTouchable(Touchable.enabled);
				saveBtn.setTouchable(Touchable.enabled);
			}
			
			if (resumeBtn.isPressed()) {
				audioManager.playClick();
				LOGGER.info("Pause menu down.");
				toggle--;
				if (diceNumber == 0) {
					diceBtn.setTouchable(Touchable.enabled);
				}
			} else if (quitBtn.isPressed()) {
				//Exits app
				audioManager.playClick();
				LOGGER.info("Game has been exited");
				Gdx.app.exit();
			} else if (mainMenuBtn.isPressed()) {
				//Goes back to main menu
				audioManager.playClick();
				audioManager.stopAll();
				LOGGER.info("Returned to main menu.");
				game.setScreen(new TransitionScreen(new MainMenuScreen(game), game));
			} else if (saveBtn.isPressed()) {
				LOGGER.info("Game has been saved.");
				sqlite.updateAll(sliderValue, turn, playing, tutorial, spaceX, spaceY, iceCreamPoints, popsiclePoints);
			} else if (volumeSlider.isDragging()) {
				//Volume slider values
				sliderValue = (int) volumeSlider.getValue();
				sliderLabel.setText(sliderValue);
				sqlite.updateVolume(sliderValue);
				audioManager.updateAll();
			}
			break;
	}
		
		} catch (Exception e) {
				LOGGER.fatal("A fatal error has occured. Game crashed.");
				try {
					CrashInfo ci = new CrashInfo(e, game);
					game.setScreen(ci);
				} catch (IOException n) {
					
				}	
		}	
	
			if (chestBtn.isTouchable()) {
					
				if (chestBtn.isPressed()) {				
					treasurePoints(0);
					//Rest is basically duplicated code, but necessary so the program can figure out which player gained the points, as well as how many points to allocate. 
					//I couldn't put it in the one if statement below, like where the points get displayed, because then the program couldn't figure out which chestBtn was pressed to know which randomSelector index to add 
				} else if (chestBtn2.isPressed()) {	
					treasurePoints(1);
				} else if (chestBtn3.isPressed()) {		
					treasurePoints(2);
				} else if (chestBtn4.isPressed()) {
					treasurePoints(3);
				} else if (chestBtn5.isPressed()) {
					treasurePoints(4);
				} else if (chestBtn6.isPressed()) {
					treasurePoints(5);
				}
				
				//Once the user presses any of the chest buttons, set them to invisible and disabled, and update the score
				if (chestBtn.isPressed() || chestBtn2.isPressed() || chestBtn3.isPressed() || chestBtn4.isPressed() || chestBtn5.isPressed() || chestBtn6.isPressed()) {
					
					audioManager.playCoin();
					
					LOGGER.info("Chest button has been pressed.");
					chestBtn.setTouchable(Touchable.disabled);
					chestBtn2.setTouchable(Touchable.disabled);
					chestBtn3.setTouchable(Touchable.disabled);
					chestBtn4.setTouchable(Touchable.disabled);
					chestBtn5.setTouchable(Touchable.disabled);
					chestBtn6.setTouchable(Touchable.disabled);
					
					chestBtn.setVisible(false);
					chestBtn2.setVisible(false);
					chestBtn3.setVisible(false);
					chestBtn4.setVisible(false);
					chestBtn5.setVisible(false);
					chestBtn6.setVisible(false);

					pointsLabel1.setText(iceCreamPoints);
					pointsLabel2.setText(popsiclePoints);
					
					
					
					diceBtn.setTouchable(Touchable.enabled);
				}
											
			} //End of chestBtn touchable if statement
			
			if (diceNumber < 0) {
				LOGGER.error("Dice number went below 0");
				diceNumber = 0;
			}
			
			
			//set label above dice to nothing if it equals 0
			String diceStr = String.valueOf(diceNumber);
			
			if (diceStr.equals("0")) {
				diceStr = "";
			}
			//Draws dice roll 			
			spriteBatch.begin();
			font.draw(spriteBatch, diceStr, BPArty.WIDTH / 2 - 20, BPArty.HEIGHT-600);
			spriteBatch.end();
			
			if (iceCreamPoints < 0) {
				iceCreamPoints = 0;
				pointsLabel1.setText(iceCreamPoints);
			}
			if (popsiclePoints < 0) {
				popsiclePoints = 0;
				pointsLabel2.setText(popsiclePoints);
			}
			
			if (spaceX == 56 && spaceY == 56) {
				text = "You need to roll a " + numbNeeded + " to pass";
				textBox.setText(text);
				diceBtn.setTouchable(Touchable.enabled);
			}
			if (spaceX == 57 && spaceY == 67) {
				if (iceCreamPoints > popsiclePoints) {
					LOGGER.info("Ice cream has won.");
					sqlite.updateAll(50,0,0,0,27,40,0,0);
					game.setScreen(new IceCreamWin(game));
					
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							Gdx.app.exit();
						}
					}, 1f);				
					
				} else {
					LOGGER.info("Popsicle has won.");
					sqlite.updateAll(50,0,0,0,27,40,0,0);
					game.setScreen(new PopsicleWin(game));
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							Gdx.app.exit();
						}
					}, 1f);					
				}
			}			
			
		} //End of playable if statement		
		//Basically puts all of the stuff added to the stage
		stage.act(delta);
		stage.draw();
	} //End of render method
	
	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Applies settings to a button
	 * 
	 * @param button the manipulated button
	 * @param x top left x coord
	 * @param y top left y coord
	 * @param visible is it visible
	 * @param touchable is it touchable
	 * @param text text that goes on button
	 * @param upStyle appearance when not hovered over
	 * @param overStyle appearance when hovered over
	 */
	//This method didn't seem to be working, called a NPE when I tried loading board with buttons like this
	public void showSettings(TextButton button, float x, float y, boolean visible, Touchable touchable, String text, String upStyle, String overStyle) {
		button = new TextButton(text, game.ButtonStyles(upStyle, overStyle));
		button.setPosition(x, y);
		stage.addActor(button);
		button.setVisible(visible);
		button.setTouchable(touchable);
	}
	
	/**
	 * Gets 6 random numbers with values 1-6 that assign points to the player
	 * 
	 * @param chest assigns a value 0-5 which is used in an array
	 */
	public void treasurePoints(int chest) {
		
		int[] randomSelector = new Random().ints(1,7).distinct().limit(6).toArray();
		
		//Sets bottom text to announce which player gained how many points
		textBox.setText(notCurrentPlayer + " gained " + randomSelector[chest] + " points!");		
		//If player equals ice cream, give them the points, if not, give popsicle the points
		if (turn == 1) {
			popsiclePoints+=randomSelector[chest];
		} else {
			iceCreamPoints+=randomSelector[chest];
		}
	}
	
	/**
	 * 
	 * Used to assign settings to the tiles or map when going through the tutorial
	 * 
	 * @param explainTile which tile is being explained
	 * @param padLeft how much to pad from the left
	 * @param padBottom how much to pad from the bottom
	 * @param padTop how much to pad from the top
	 * @param fontScaleX the scale width of the font
	 * @param fontScaleY the scale height of the font
	 * @param explanation the explanation of the tile
	 */
	public void tutorialSettings(Label explainTile, float padLeft, float padBottom, float padTop, float fontScaleX, float fontScaleY, String explanation) {
		explainTile = new Label(String.format(explanation), new Label.LabelStyle(font, Color.BLACK));
		table3.padLeft(padLeft);
		table3.padTop(padTop);
		table3.padBottom(padBottom);
		explainTile.setFontScale(fontScaleX, fontScaleY);
		table3.add(explainTile);
	}
	
	@Override
	public void show() {
		
		LOGGER.debug("Stage created and added to input multiplexer.");
		stage = new Stage();
		
		inputMultiplexer.addProcessor(stage);
		
		if (tutorial == 1) {	
			
				//Creates new table to draw the labels, fits to screen
				table3 = new Table();
				table3.setFillParent(true);
				table3.center();
			
				//table3.debug();
				
			    table3.padLeft(420);
				stage.addActor(table3);		
	
		  try {
			  //Each time the user clicks, it puts a new explanation, sets the font if it's too large, and adds it to table
			if (tutorialCounter == 0) {
				tutorialSettings(explainChar, 300, 0, 0, .8f, .9f, "These are the characters, they move together.");				
			}
			else if (tutorialCounter == 1) {
				tutorialSettings(blueTile, 280, 0, 0, .9f, .9f, "Blue tiles give 2 points to the player.");
			} else if (tutorialCounter == 2) {
				tutorialSettings(starTile, 280, 0, 0, .9f, .9f, "Star tiles give 5 points to the player.");
			} else if (tutorialCounter == 3) {
				tutorialSettings(versusTile, 280, 0, 0, .6f, .9f, "Versus tiles initiate a minigame that gives double points!");
			} else if (tutorialCounter == 4) {
				tutorialSettings(eventBTile, 280, 0, 0, .9f, .9f, "Blue event tiles gives lots of points!");
			} else if (tutorialCounter == 5) {
				tutorialSettings(treasureTile, 280, 0, 0, .7f, .9f, "Treasure tiles contain treasure!");
			} else if (tutorialCounter == 6) {
				tutorialSettings(greenTile, 280, 0, 0, .9f, .9f, "Green tiles do nothing.");
			} else if (tutorialCounter == 7) {
				table3.align(Align.bottom);
				tutorialSettings(decisionTile, 0, 80, 0, .6f, .9f, "Splits allow you to choose left or right, think carefully about what you choose!");
			} else if (tutorialCounter == 8) {
				table3.align(Align.bottom);
				tutorialSettings(eventRTile, 0, 80, 0, .6f, .9f, "Red event tiles start a BAD event! Don't want to land on those.");
			} else if (tutorialCounter == 9) {
				table3.align(Align.top);
				tutorialSettings(specialTile, 0, 0, 330, .6f, .9f, "This is a special tile, for this one, a triple point minigame is engaged! Think of it as a half-way checkpoint." );
			} else if (tutorialCounter == 10) {
				table3.align(Align.top);
				tutorialSettings(eventBadTile, 0, 0, 330, .6f, .9f, "This tile is a little different than the red event tile, this one can affect both players! Test your luck.");
			} else if (tutorialCounter == 11) {			
				table3.align(Align.top);
				tutorialSettings(specialTile, 0, 0, 100, .7f, .9f, "Here, you loop around going clockwise, and when you reach the exit tile, you must roll equal \nto or higher than the number required to pass. If you don't, you loop again!");
			} else if (tutorialCounter == 12) {
				table3.align(Align.top);			
				tutorialSettings(endTile, 0, 0, 100, .7f, .9f, "Once you make it to the end here, the player with the higher points wins!");
			} else if (tutorialCounter == 13) {
				table3.align(Align.top);
				tutorialSettings(textBox, 0, 0, 100, .7f, .9f, "Would you like to retake the tutorial?");
				
				//Once at the end, it adds the restart/play button from earlier
				restartBtn.getLabel().setAlignment(Align.left);
				restartBtn.getLabelCell().padLeft(25);
				restartBtn.getLabel().setFontScale(2,2);
			
				playBtn.getLabel().setAlignment(Align.left);
				playBtn.getLabelCell().padLeft(42);
				playBtn.getLabel().setFontScale(2,2);
			
				restartBtn.setPosition(350, 250);
				playBtn.setPosition(650, 250);
			
				stage.addActor(restartBtn);
				stage.addActor(playBtn);
			}
		} catch (Exception e) {
			//Catch if the user clicks too fast/both buttons at same time, stop the tutorial by setting counter to 20, give option to restart
			//20 is an arbitrary number, it just had to be anything above 13 to stop the if statement counter from working in touchDown method
			LOGGER.fatal("Tutorial frozen");
			tutorialCounter = 20;
			    }
		} else if (playing == 0) {
			
			LOGGER.info("Options for tutorial popped up");
			
			//If not playing, nor tutorial, so basically this is only called when the game first starts up
				table3 = new Table();
				table3.setFillParent(true);
			
				font = new BitmapFont(Gdx.files.internal("ArialBlack.fnt"));
				introduction = new Label(String.format("%s","Hi! Welcome to BPArty, do you need to take the tutorial?"), new Label.LabelStyle(font, Color.BLACK));	
				yesBtn = new TextButton("Yes", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
				noBtn = new TextButton("No", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
			
				//Yes and no button are created to see whether the user wants the tutorial or not
				yesBtn.getLabel().setAlignment(Align.left);
				yesBtn.getLabelCell().padLeft(43);
				yesBtn.getLabel().setFontScale(2,2);
			
				noBtn.getLabel().setAlignment(Align.left);
				noBtn.getLabelCell().padLeft(52);
				noBtn.getLabel().setFontScale(2,2);
			
				yesBtn.setPosition(350, 250);
				noBtn.setPosition(650, 250);
				
				table3.add(introduction);
				
				stage.addActor(yesBtn);
				stage.addActor(noBtn);
				stage.addActor(table3);
				
			} else if (playing == 1) {
				LOGGER.info("Show method enacted for started game.");
				//Adds the IMPORTANT dice button!!!!! And the left/right button for decision tile, sets the positions, and adds to stage
				diceBtn = new TextButton("Roll", game.ButtonStyles("MenuRectangle","DiceButton"));
				diceBtn.getLabel().setAlignment(Align.left);
				diceBtn.getLabelCell().padLeft(33);
				diceBtn.getLabel().setFontScale(3,3);
				diceBtn.setPosition(0,0);
		
				leftBtn = new TextButton("", game.ButtonStyles("LeftButton","LeftButtonLit"));
				rightBtn = new TextButton("", game.ButtonStyles("RightButton","RightButtonLit"));
		
				leftBtn.setPosition(350,100);
				rightBtn.setPosition(650,100);

				stage.addActor(diceBtn);
				stage.addActor(leftBtn);
				stage.addActor(rightBtn);
		
				//Set them initially as invisible and untouchable, as they only pop up when on a split path
				leftBtn.setVisible(false);
				rightBtn.setVisible(false);
				leftBtn.setTouchable(Touchable.disabled);
				rightBtn.setTouchable(Touchable.disabled);	
				
				//Chest buttons! For treasure! A pirate's life doesn't sound all too bad. They are text buttons, but have no text, it's simply to portray the image
				chestBtn = new TextButton("", game.ButtonStyles("ChestSprite", "ChestSprite"));	
				chestBtn2 = new TextButton("", game.ButtonStyles("ChestSprite2", "ChestSprite2"));		
				chestBtn3 = new TextButton("", game.ButtonStyles("ChestSprite3", "ChestSprite3"));	
				chestBtn4 = new TextButton("", game.ButtonStyles("ChestSprite4", "ChestSprite4"));	
				chestBtn5 = new TextButton("", game.ButtonStyles("ChestSprite5", "ChestSprite5"));	
				chestBtn6 = new TextButton("", game.ButtonStyles("ChestSprite6", "ChestSprite6"));
				
				//Sets each position 
				chestBtn.setPosition(350, 350);
				chestBtn2.setPosition(450, 350);
				chestBtn3.setPosition(550, 350);
				chestBtn4.setPosition(650, 350);
				chestBtn5.setPosition(750, 350);
				chestBtn6.setPosition(850, 350);
				
				//Puts on stage
				stage.addActor(chestBtn);
				stage.addActor(chestBtn2);
				stage.addActor(chestBtn3);
				stage.addActor(chestBtn4);
				stage.addActor(chestBtn5);
				stage.addActor(chestBtn6);
				
				//Sets invisible
				chestBtn.setVisible(false);
				chestBtn2.setVisible(false);
				chestBtn3.setVisible(false);
				chestBtn4.setVisible(false);
				chestBtn5.setVisible(false);
				chestBtn6.setVisible(false);
				
				//Sets untouchable
				chestBtn.setTouchable(Touchable.disabled);
				chestBtn2.setTouchable(Touchable.disabled);
				chestBtn3.setTouchable(Touchable.disabled);
				chestBtn4.setTouchable(Touchable.disabled);
				chestBtn5.setTouchable(Touchable.disabled);
				chestBtn6.setTouchable(Touchable.disabled);

				//More tables, one for pause one for main user info
				table = new Table();
				table2 = new Table(skin);
		
				//Create pauseMenu sprite and set as background
				Sprite pauseMenu = new Sprite(texture = new Texture("PauseMenu.png"));
				SpriteDrawable pauseDraw = new SpriteDrawable(pauseMenu);
				table2.background(pauseDraw);
		
				//Create more button objects, and slider/container for volume, and labels to hold the text
				resumeBtn = new TextButton("Resume", game.ButtonStyles("MenuRectangle","MenuRectangle"));
				quitBtn = new TextButton("Quit", game.ButtonStyles("MenuRectangle","MenuRectangle"));
				mainMenuBtn = new TextButton("Return", game.ButtonStyles("MenuRectangle","MenuRectangle"));
				saveBtn = new TextButton("Save", game.ButtonStyles("MenuRectangle","MenuRectangle"));
				volumeSlider = new Slider(0, 100, 1, false, game.sliderStyles());
				container = new Container<Slider>(volumeSlider);
		
				container.setTransform(true);
				container.setScale(4.5f);
				font = new BitmapFont();
		
				sliderLabel = new Label(String.valueOf(sliderValue), new Label.LabelStyle(font, Color.WHITE));
				volumeLabel = new Label("Volume", new Label.LabelStyle(font, Color.WHITE));
		
				volumeLabel.setFontScale(4);
				sliderLabel.setFontScale(4);
				volumeSlider.setValue(sliderValue);
		
				//Sets the settings like location and font to the pause menu buttons
				buttonSettings(resumeBtn);
				buttonSettings(mainMenuBtn);
				buttonSettings(saveBtn);
				buttonSettings(quitBtn);
		
				//Draws onto table
				table2.center().bottom().pad(30);
				table2.add(volumeLabel).top().height(170).colspan(4);
				table2.row();
				table2.add(container).left().padTop(25).colspan(2).padLeft(145);
				table2.row();
				table2.add(sliderLabel).colspan(4).center();
				table2.row();
				table2.add(resumeBtn).padLeft(80);
				table2.add(mainMenuBtn);
				table2.add(saveBtn);
				table2.add(quitBtn);
		
				table2.setBounds(160, 90, 960, 540);
				
				//table.debug();
		
				table.top();
				table.setFillParent(true);
		
				//All the HUD stuff for the main screen, font is recalled because a different font object is used above and I wanted to reuse the same font variable :)
				font = new BitmapFont(Gdx.files.internal("ArialBlack.fnt"));
				player1 = new Label(String.format("Ice Cream"), new Label.LabelStyle(font, Color.WHITE));
				player2 = new Label(String.format("Popsicle"), new Label.LabelStyle(font, Color.WHITE));
				pointsLabel1 = new Label(String.format("%01d", iceCreamPoints), new Label.LabelStyle(font, Color.WHITE));
				pointsLabel2 = new Label(String.format("%01d", popsiclePoints), new Label.LabelStyle(font, Color.WHITE));
				playerLabel = new Label(String.format("%s", currentPlayer), new Label.LabelStyle(font, Color.WHITE));
				turnLabel = new Label(String.format("%s", "Turn:"), new Label.LabelStyle(font, Color.WHITE));
				textBox = new Label(String.format("%s", text), new Label.LabelStyle(font, Color.WHITE));
				
				//And positioned/drawn to the screen
				table.add(player1).expandX().padTop(10);
				table.add(player2).expandX().padTop(10);
				table.add(turnLabel).expandX().padTop(10);
				table.row();
				table.add(pointsLabel1).expandX();
				table.add(pointsLabel2).expandX();
				table.add(playerLabel).expandX();
				table.row();
				table.add(textBox).expandX().bottom().colspan(3).padTop(550);
		
				//tables are added to the stage
				stage.addActor(table);
				stage.addActor(table2);
			}
	}
	
	@Override
	public void hide() {

	}
	/**
	 * Applies the settings to the button
	 * @param button what is being modified
	 * @return button the button that is modified
	 */
	public TextButton buttonSettings(TextButton button) {
		button.getLabel().setAlignment(Align.left);
		button.getLabelCell().padLeft(18);
		button.getLabel().setFontScale(2,2);
		return button;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {	

		//if the user left clicks and the tutorial is active, add to the tutorial counter
		if (tutorial == 1 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			tutorialCounter++;
		} 
		
			if (playing == 1) {	

				//Create an object for the tile layers
				TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
		
				//Associate the tile layers with layers on the tiledMap
				tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
				
				if (leftBtn.isTouchable() && rightBtn.isTouchable()) {
					LOGGER.debug("Decision available at split.");
					decision();
				}
		
				//Controls the input for pressing the dice
				if ((Gdx.input.getX() <= diceBtn.getWidth()) && diceBtn.isTouchable()) {
					if(Gdx.input.getY() >= BPArty.HEIGHT - diceBtn.getHeight() && diceBtn.isTouchable()) {
				
						audioManager.playClick2();
						
						diceBtn.setTouchable(Touchable.disabled);
				
						text = "";
						textBox.setText(text);
				
						Random rNumb = new Random();
						diceNumber = 1+rNumb.nextInt(6);
						oldDiceLoop = diceNumber;
				
						oldDice = diceNumber;
					try {
						moveTile();
						checkTile();
			
						//Makes dice button touchable again after the diceNumber equals zero + 1 second
						Timer.schedule(new Timer.Task() {
							@Override
							public void run() {
								diceBtn.setTouchable(Touchable.enabled);
							}
						}, diceNumber + 1f);
						
						minigameInt++;
						LOGGER.info("Moving character was successful.");
					} catch (Exception e) {
						LOGGER.error("Error while moving. Message: " +  e.getMessage());
						e.printStackTrace();
						try {
							LOGGER.fatal("A fatal error has occured. Game crashed.");
							CrashInfo ci = new CrashInfo(e, game);
							game.setScreen(ci);
						} catch (IOException n) {
							
						}					
					}
					} //End of second dice if statement
					
				} //End of first dice if statement
				
			} //End of 'playing' if statement 
			return false;
	} //End of touchDown method
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		if (playing == 1) {
			//Allows for zooming with the scroll wheel, if outside the boundary, zooms back in/out
			if (toggle % 2 == 0) {
				if (camera.zoom > 0.3f) {
					camera.zoom += 0;
					camera.zoom = .2999f;
				} else if (camera.zoom < 0.21f) {
					camera.zoom += 0;
					camera.zoom = .21001f;
				} else {
					camera.zoom += amountY * 0.2f;
				}
			}
		}
		return false;
	}
	
	/**
	 * Sets left/right buttons to visible and touchable
	 */
	public void decisionTile() {
		LOGGER.info("Player landed on decision tile");	
		//If on a split path, shows left/right buttons and sets them touchable
		leftBtn.setVisible(true);
		rightBtn.setVisible(true);
		leftBtn.setTouchable(Touchable.enabled);
		rightBtn.setTouchable(Touchable.enabled);
	}
	
	/**
	 * Subtracts 3-5 points when the player lands on this tile
	 */
	public void eventRed() {
		LOGGER.info("Player landed on red event tile");
		Random rNumb = new Random();
		int minusPoints = 3+rNumb.nextInt(3);
		//Subtracts 3-5 points
		if (turn == 1) {
		iceCreamPoints-=minusPoints;
		pointsLabel1.setText(iceCreamPoints);
		text = "Ice cream lost " + minusPoints + " points!";
		textBox.setText(text);
		} else {
			popsiclePoints-=minusPoints;
			pointsLabel2.setText(popsiclePoints);
			text = "Popsicle lost " + minusPoints + " points!";
			textBox.setText(text);
		}
	}
	
	/**
	 * Has first place either lose half points, give five points to the other player, lose 5 points
	 * or points are distributed evenly
	 * or the player who lands on the tile loses 5 points
	 */
	public void eventBad() {
		LOGGER.info("Player landed on bad event tile");
		Random rNumb = new Random();
		int randEvent = 1+rNumb.nextInt(5);
		//Does one of these events, random 1-5, I can't say how balanced the point system is on this
		if (randEvent == 1) {
			if (iceCreamPoints > popsiclePoints) {
			text = "Ice cream loses half their points!";
			textBox.setText(text);
			iceCreamPoints = iceCreamPoints / 2;
			pointsLabel1.setText(iceCreamPoints);
			} else {
				text = "Popsicle loses half their points!";
				textBox.setText(text);
				popsiclePoints = popsiclePoints / 2;
				pointsLabel2.setText(popsiclePoints);
			}
		} else if (randEvent == 2) {
			if (iceCreamPoints < popsiclePoints) {
				text = "Since Ice Cream has less points than Popsicle, Popsicle gives 5 points to Ice Cream!";
				textBox.setText(text);
				iceCreamPoints += 5;
				popsiclePoints -= 5;
				pointsLabel1.setText(iceCreamPoints);
				pointsLabel2.setText(popsiclePoints);
			} else {
				text = "Since Popsicle has less points than Popsicle, Ice Cream gives 5 points to Popsicle!";
				textBox.setText(text);
				iceCreamPoints -= 5;
				popsiclePoints += 5;
				pointsLabel1.setText(iceCreamPoints);
				pointsLabel2.setText(popsiclePoints);
			}
		} else if (randEvent == 3) {
			text = "Points will be distributed evenly!";
			textBox.setText(text);
			int totalPoints = iceCreamPoints + popsiclePoints;
			iceCreamPoints = totalPoints / 2;
			popsiclePoints = totalPoints / 2;
			pointsLabel1.setText(iceCreamPoints);
			pointsLabel2.setText(popsiclePoints);
		} else if (randEvent == 4) {
			if (iceCreamPoints < popsiclePoints) {
				text = "Popsicle is winning? Popsicle loses 5 points.";
				textBox.setText(text);
				popsiclePoints -= 5;
				pointsLabel2.setText(popsiclePoints);
			} else {
				text = "Ice cream is winning? Ice cream loses 5 points.";
				textBox.setText(text);
				iceCreamPoints -= 5;
				pointsLabel1.setText(iceCreamPoints);
			}
		} else if (randEvent == 5) {
			if (turn == 1) {
				text = "Sorry, Ice Cream, but you landed here, so you lose 5 points.";
				textBox.setText(text);
				iceCreamPoints -= 5;
				pointsLabel1.setText(iceCreamPoints);
			} else {
				text = "Sorry, Popsicle, but you landed here, so you lose 5 points.";
				textBox.setText(text);
				popsiclePoints -= 5;
				pointsLabel2.setText(popsiclePoints);
			}
		}
	}
	
	/**
	 *  Player gains 3-5 points
	 */
	public void eventBlue() {
		LOGGER.info("Player landed on blue event tile");
		Random rNumb = new Random();
		int plusPoints = 3+rNumb.nextInt(3);
		if (turn == 1) {
		iceCreamPoints+=plusPoints;
		pointsLabel1.setText(iceCreamPoints);
		text = "Ice cream gained " + plusPoints + " points!";
		textBox.setText(text);
		} else {
			popsiclePoints+=plusPoints;
			pointsLabel2.setText(popsiclePoints);
			text = "Popsicle gained " + plusPoints + " points!";
			textBox.setText(text);
		}
	}
	
	/**
	 * Screen is set for random game tutorial
	 */
	public void versusTile() {
		LOGGER.info("Player landed on versus tile");	
		doublePoints = true;
		audioManager.stopAll();
		game.setScreen(new TransitionScreen(new TutorialMaster(game), game));
	}
	
	public void midWayTile() {
		LOGGER.info("Player made it to mid-way tile");
		triplePoints = true;
		audioManager.stopAll();
		game.setScreen(new TransitionScreen(new TutorialMaster(game), game));
	}
	
	/**
	 * Player gains 5 points
	 */
	public void starTile() {
		LOGGER.info("Player landed on star tile");
		//plus 5 points
		if (turn == 1) {
		iceCreamPoints+=5;
		pointsLabel1.setText(iceCreamPoints);
		text = "Ice cream gained 5 points!";
		textBox.setText(text);
		} else {
			popsiclePoints+=5;
			pointsLabel2.setText(popsiclePoints);
			text = "Popsicle gained 5 points!";
			textBox.setText(text);
		}
	}
	
	/**
	 * Player has to pick a treasure chest that is set visible and touchable
	 */
	public void treasureTile() {
		LOGGER.info("Player landed on treasure tile");
		//Make 6 chest images pop up on screen, randomize them with different rewards
		//Once the user clicks one, reward them with whatever is inside, then end turn
		text = "Pick one of the chests!";
		textBox.setText(text);
		chestBtn.setVisible(true);
		chestBtn2.setVisible(true);
		chestBtn3.setVisible(true);
		chestBtn4.setVisible(true);
		chestBtn5.setVisible(true);
		chestBtn6.setVisible(true);
		
		chestBtn.setTouchable(Touchable.enabled);
		chestBtn2.setTouchable(Touchable.enabled);
		chestBtn3.setTouchable(Touchable.enabled);
		chestBtn4.setTouchable(Touchable.enabled);
		chestBtn5.setTouchable(Touchable.enabled);
		chestBtn6.setTouchable(Touchable.enabled);
		
		diceBtn.setTouchable(Touchable.disabled);
		
	}
	
	/**
	 * Player gains 2 points
	 */
	public void blueTile() {
		LOGGER.info("Player landed on blue tile");
		//plus two points
		if (turn == 1) {
		iceCreamPoints+=2;
		pointsLabel1.setText(iceCreamPoints);
		text = "Ice cream gained 2 points!";
		textBox.setText(text);
		} else {
			popsiclePoints+=2;
			pointsLabel2.setText(popsiclePoints);
			text = "Popsicle gained 2 points!";
			textBox.setText(text);
		}
	}
	
	/**
	 * Player loses 2 points
	 */
	public void redTile() {
		LOGGER.info("Player landed on red tile");
		//minus two points
		if (turn == 1) {
		iceCreamPoints-=2;
		pointsLabel1.setText(iceCreamPoints);
		text = "Ice cream lost 2 points!";
		textBox.setText(text);
		} else {
			popsiclePoints-=2;
			pointsLabel2.setText(popsiclePoints);
			text = "Popsicle lost 2 points!";
			textBox.setText(text);
		}
	}
	/*
	 * Plays a minigame every turn rotation
	 */
	public void playGame() {	
			audioManager.stopAll();
			game.setScreen(new TransitionScreen(new TutorialMaster(game), game));
	}
	
	/**
	 * checks which tile the player is on amd switches the turn
	 */
	public void checkTile() {
		
		//A whole bunch of for loops that check if the space the character is on is equal to the array associated with the tile type...
		//It then carries out the instructions inside of the if statement
		//It is delayed by however long the above code takes to run, due to the delay it has inflicted on it, added by 0.2 seconds, because if it was exact, it would sometimes break, so I put a small buffer
		//Green tile isn't included, it does not do anything				
		Timer.schedule(new Timer.Task() {
		@Override
		public void run() {
		//Red tile
		for (int i = 0; i < redX.length; i++ ) {
			if ((spaceX == redX[i] && spaceY == redY[i])) {
				redTile();
			}
		} 
		//Blue tile
		for (int i = 0; i < blueX.length; i++ ) {
			if ((spaceX == blueX[i] && spaceY == blueY[i])) {
				blueTile();
			}
		}
		for (int i = 0; i < treasureX.length; i++ ) {
			if ((spaceX == treasureX[i] && spaceY == treasureY[i])) {
				treasureTile();
			}
		}
		//Star
		for (int i = 0; i < starX.length; i++ ) {
			if ((spaceX == starX[i] && spaceY == starY[i])) {
				starTile();
			}
		}
		//Versus tile
		for (int i = 0; i < versusX.length; i++ ) {
			if ((spaceX == versusX[i] && spaceY == versusY[i])) {
				sqlite.updateAll(sliderValue, turn, playing, tutorial, spaceX, spaceY, iceCreamPoints, popsiclePoints);
				versusTile();
			}
		}
		for (int i = 0; i < eventBlueX.length; i++ ) {
			if ((spaceX == eventBlueX[i] && spaceY == eventBlueY[i])) {
				eventBlue();
			}
		}
		for (int i = 0; i < decisionX.length; i++ ) {
			if ((spaceX == decisionX[i] && spaceY == decisionY[i])) {
				decisionTile();
			}
		}
		for (int i = 0; i < eventRedX.length; i++ ) {
			if ((spaceX == eventRedX[i] && spaceY == eventRedY[i])) {			
				eventRed();
			}
		}
		for (int i = 0; i < eventBadX.length; i++ ) {
			if ((spaceX == eventBadX[i] && spaceY == eventBadY[i])) {
				eventBad();
			}
		}
		
		if (spaceX == 42 && spaceY == 57) {
			sqlite.updateAll(sliderValue, turn, playing, tutorial, spaceX, spaceY, iceCreamPoints, popsiclePoints);
			midWayTile();
		}

		LOGGER.info("Tile has been checked.");
		
		//Switches turn, it does it immediately, so sometimes the delayed actions can look like they're giving it to the wrong player, but it does work...
		//If I put it on a timer like the rest however, then if it landed on a red/blue tile for example, it could break the program, so I'd have to put every tile on a timer		
		if (turn == 1) {
			turn = 0;
		} else {
			turn = 1;
		}
		
		LOGGER.info("Turn has switched.");
		
		} //End of run method inside timer
}, diceNumber+1f); //End of tile result delay
	}
	
	/**
	 * player moves in the vertical direction, negative or positive
	 * @param y amount moved in vertical direction
	 */
	public void moveVertical(int y) {
		
		
		TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
		tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
		
		spaceY += y;
		tileLayer.setCell(spaceX, spaceY, cell);
		tileLayer.setCell(oldSpaceX, oldSpaceY, null);
		oldSpaceY += y;
	}
	
	/**
	 * player moves in a horizontal direction, positive or negative
	 * @param x amount moved horizontally
	 */
	public void moveHorizontal(int x) {			
		
		TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
		tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
		
		spaceX += x;
		tileLayer.setCell(spaceX, spaceY, cell);
		tileLayer.setCell(oldSpaceX, oldSpaceY, null);
		oldSpaceX += x;
	}
	
	/**
	 * how the player moves around a corner after a decision
	 * @param x amount moved horizontally
	 * @param y amount moved vertically
	 */
	public void decisionMove(int x, int y) {
		
		TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
		tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
		
		spaceX += x;
		spaceY += y;
		tileLayer.setCell(spaceX, spaceY, cell);
		tileLayer.setCell(oldSpaceX, oldSpaceY, null);
		oldSpaceX += x;
		oldSpaceY += y;
	}
	
	/**
	 * stops the player
	 */
	public void stopMovement() {
		LOGGER.info("Character stopped");
		diceNumber+=1;
		leftBtn.setVisible(true);
		rightBtn.setVisible(true);
		leftBtn.setTouchable(Touchable.enabled);
		rightBtn.setTouchable(Touchable.enabled);
		Timer.instance().stop();	
	}
	
	/**
	 * moves the player based on whether they press the left or the right button
	 */
	public void decision() {
		
		//If the left/right button is touched, move the character past the arrow tile, onto the corner tile, and turn the corner if the remainder of the dice is greater than 0	
					
				//Left Button
				if ((Gdx.input.getX() >= BPArty.WIDTH - 930 && Gdx.input.getX() <= 559) && (Gdx.input.getY() >= 536 && Gdx.input.getY() <= 620)) {
					TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
					//Gets the tileLayer and places it on the second layer (1) of the map to place the character texture region tile
					tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
					//Set the buttons to invisible and untouchable
					leftBtn.setVisible(false);
					rightBtn.setVisible(false);	
					leftBtn.setTouchable(Touchable.disabled);
					rightBtn.setTouchable(Touchable.disabled);	
					//Increment every time the character is moved, add one to the diceNumber so the character can get past the arrow tile
					for (int i = 0; i < 1; diceNumber--, i++) {
						//if the space is on the decision tile
						if (spaceX == 33) {
							//Move 2 to the left to get onto the corner tile and deactivate the button
							if (diceNumber > 0) {				
								oldDice--;
								moveHorizontal(-2);
											
							} else {
								moveHorizontal(-1);
							}
						}
						//If the character makes it to the left corner tile, go forward one tile for the remainder of the dice
						else if (spaceX == 31) {
							moveVertical(1);				
						}
						//If on the second decision tile, move 2 up if the character is on Y = 49, only if the user inputs left
						
						else if (spaceY == 49) {
							if (diceNumber > 0) {
								oldDice--;
								moveVertical(2);			
							} else {
								moveVertical(1);
							}
						}
						//If the character makes it to the corner tile up, move to the right however high the remainder is, don't move if remainder = 0
						else if (spaceY == 51) {
							moveHorizontal(1);
						}
					 }
					Timer.instance().start();
				   }
				
				//Right Button
				if ((Gdx.input.getX() >= BPArty.WIDTH - 630 && Gdx.input.getX() <= 859) && (Gdx.input.getY() >= 536 && Gdx.input.getY() <= 620)) {
					TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
					tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
					leftBtn.setVisible(false);
					rightBtn.setVisible(false);
					leftBtn.setTouchable(Touchable.disabled);
					rightBtn.setTouchable(Touchable.disabled);
					for (int i = 0; i < 1; diceNumber--, i++) {
						//If the user is on the decision tile, move two to the right, to the corner
						if (spaceX == 33) {
							if (diceNumber > 0) {
								oldDice--;
								moveHorizontal(2);
							} else {
								moveHorizontal(1);
							}
						}
						//If the user is on the corner tile, move forward in the Y direction however much the dice remainder is
						else if (spaceX == 35) {
							moveVertical(1);			
						}
						//If the user is on the decision tile, move -2 Y down to the corner tile
						else if (spaceY == 49) {
							if (diceNumber > 0) {
								oldDice--;
								moveVertical(-2);
							} else {
								moveVertical(-1);
							}
						}
						//If the user is on the tile 2 tiles below the decision tile, then move +1 in the X direction however much the dice remainder is
						else if (spaceY == 47) {
							moveHorizontal(1);
						}				
					 }
					LOGGER.info("Decision made at split");
					Timer.instance().start();
				}
	}
	
	
	/**
	 * moves the character based on where they are on the board 
	 */
	public void moveTile() {
		
		if ((spaceX == 56 && spaceY == 56) && (numbNeeded <= diceNumber)) {
			moveHorizontal(1);
			Timer.instance().start();
		} else if ((spaceX == 56 && spaceY == 56) && (numbNeeded > diceNumber)) {
			numbNeeded -= diceNumber;
			moveVertical(-1);
			Timer.instance().start();
		}	
		
		if (leftBtn.isTouchable() && rightBtn.isTouchable()) {
			if ((Gdx.input.getX() >= BPArty.WIDTH - 930 && Gdx.input.getX() <= 559) && (Gdx.input.getY() >= 536 && Gdx.input.getY() <= 620)) {
				TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
				//Gets the tileLayer and places it on the second layer (1) of the map to place the character texture region tile
				tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
				//Set the buttons to invisible and untouchable
				leftBtn.setVisible(false);
				rightBtn.setVisible(false);	
				leftBtn.setTouchable(Touchable.disabled);
				rightBtn.setTouchable(Touchable.disabled);	
				//Increment every time the character is moved, add one to the diceNumber so the character can get past the arrow tile
				for (int i = 0; i < 1; diceNumber--, i++) {
					//if the space is on the decision tile
					if (spaceX == 33) {
						//Move 1 to the left to get onto the corner tile and deactivate the button
						if (diceNumber > 0) {				
						diceNumber++;
						moveHorizontal(-2);
										
						} else {
							moveHorizontal(-1);
						}
					}
					//If the character makes it to the left corner tile, go forward one tile for the remainder of the dice
					else if (spaceX == 31) {
						moveVertical(1);				
					}
					//If on the second decision tile, move 2 up if the character is on Y = 49, only if the user inputs left
					
					else if (spaceY == 49) {
						if (diceNumber > 0) {
						moveVertical(2);			
						} else {
							moveVertical(1);
						}
					}
					//If the character makes it to the corner tile up, move to the right however high the remainder is, don't move if remainder = 0
					else if (spaceY == 51) {
						moveHorizontal(1);
					}
				 }
				Timer.instance().start();
			   }
		}
						
		float delay = 1;
		float interval = 1;
		
		Timer.schedule(new Timer.Task() { 
			@Override
			public void run() {
			diceNumber--;
			TiledMapTileLayer tileLayer = new TiledMapTileLayer(tiledMap.getProperties().get("width",Integer.class), tiledMap.getProperties().get("height",Integer.class), 44, 32);
			
			//Associate the tile layers with layers on the tiledMap
			tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
						
				if (spaceX == 42 && spaceY == 57) {
					midWayTile();
					sqlite.updateAll(sliderValue, turn, playing, tutorial, 42, 56, iceCreamPoints, popsiclePoints);
				}
			
				if (spaceY == 47 && spaceX != 33 && spaceX != 50) {
					moveHorizontal(1);
				} 
				//Stops the character from moving on the end tile
				else if (spaceX == 57 && spaceY == 67) {
					Timer.instance().stop();
				}
				//Going to the right near the end
				else if (spaceY == 65 && spaceX != 57) {
					moveHorizontal(1);
				}
				//Going left after going up past the circle
				else if (spaceY == 62 && spaceX != 55){
					moveHorizontal(-1);
				}
				//Exit of circle
				else if (spaceX == 56 && spaceY == 56) {
					diceNumber = 0;
					Timer.instance().stop();
				}
				//Left siyeade of circle
				else if (spaceX == 51 && spaceY != 49 && spaceY != 58) {
					moveVertical(1);
				}
				//Right side of circle
				else if (spaceX == 56 && spaceY != 62 && spaceY != 65 && spaceY != 53) {
					moveVertical(-1);
				}
				//Top part of circle
				else if (spaceY == 58 && spaceX != 58 && spaceX != 42 && spaceX != 33 && spaceX != 56) {
					moveHorizontal(1);
				}
				//Going left on left arrow of first split
				else if (spaceX == 32 && spaceY == 52) {
					moveHorizontal(-1);
				}
				//Going up on left arrow of second split
				else if (spaceX == 45 && spaceY == 50) {
					moveVertical(1);
				}
				//Going right on right arrow of first split
				else if (spaceX == 34 && spaceY == 52) {
					moveHorizontal(1);
				}
				//Going down on right arrow of second split
				else if (spaceX == 45 && spaceY == 48) {
					moveVertical(-1);
				}
				else if (spaceX == 57 && spaceY == 56) {
					moveHorizontal(1);
				}
				//Top right of second split path
				else if (spaceX == 50 && spaceY == 51) {
					decisionMove(1,-2);
				}
				//Bottom right of second split path
				else if (spaceX == 50  && spaceY == 47) {
					decisionMove(1,2);
				}
				//Path below circle
				else if (spaceX == 53 && spaceY != 53 && spaceY != 58) {
					moveVertical(1);
				}
				//Bottom of circle
				else if (spaceY == 53 && spaceX !=42 && spaceX !=35 && spaceX !=31) {
					moveHorizontal(-1);
				}
				//Stops dice from decrementing if at decision tile 2
				else if (spaceX == 45 && spaceY == 49) {
					stopMovement();	
				} 
				//Stop dice from decrementing at decision tile 1
				else if (spaceX == 33 && spaceY == 52) {
					stopMovement();								
				}
				//
				else if (spaceY == 51 && spaceX != 42 && spaceX != 33) {
					moveHorizontal(1);
				}
				//
				else if (spaceY == 60 && spaceX != 42 && spaceX != 58 || (spaceY == 47 && spaceX != 33 && spaceX != 45) || (spaceX != 42 && spaceY == 50 && spaceY != 60 && spaceY != 47 && spaceX != 33)) {
					moveHorizontal(1);
				}
				//
				else if (spaceX == 42 && spaceY == 49) {
					moveHorizontal(1);
				}
				//
				else if (spaceX == 42) {
					moveVertical(-1);
				}
				//
				else if (spaceX != 42 && spaceY == 49 && spaceY != 60 && spaceY != 47 && spaceX != 33) {
					moveHorizontal(1);
				}
				//
				else if (spaceX == 31 && spaceY == 57) {
					decisionMove(2,1);
				}
				//
				else if (spaceX == 35 && spaceY == 57) {
					decisionMove(-2,1);
				}
				//If none of the if statements above apply, the character cell will move forward one tile
				else {						
					moveVertical(1);
				}
			}
		}, delay, interval, diceNumber-1);
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
}