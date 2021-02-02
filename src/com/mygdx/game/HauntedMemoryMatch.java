package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HauntedMemoryMatch implements Screen, InputProcessor{

	public int width = 1280;
	public int height = 720;

	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	private Viewport viewport;
	private Stage stage;
	private SpriteBatch batch = new SpriteBatch();
	private Texture texture2 = new Texture("Halloween_Haunted_Castle_Background.jpg");
	private Sprite background = new Sprite(texture2);
	public ButtonGroup<ImageButton> daButtons;
	private int turns = 0;
	private boolean playerTurnNumber;
	private boolean playerTurnNumber2;
	private boolean match;
	private int iceCreamMatches = 0;
	private int popsicleMatches = 0;
	private int done = 0;


	private ImageButton[] buttons = new ImageButton[36];

	private Skin skin;
	private TextureAtlas cardAtlas = new TextureAtlas("SpriteSheet.txt");
	private ImageButtonStyle imageButtonStyle;
	private Label playerTurn;
	private Label iceCreamWins;
	private Label popsicleWins;
	private Image iceCream;
	private Image popsicle;
	private Image iceCreamScore;
	private Image popsicleScore;
	private Texture texture;
	private BitmapFont font;

	InputMultiplexer inputMultiplxer = new InputMultiplexer();

	final BPArty game;

	AudioManager audioManager = new AudioManager();
	
	public HauntedMemoryMatch(final BPArty game) {
		this.game = game;
		camera.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		viewport.apply();
	}
/**
 * This method outfits and formats any buttons that go through it to make sure they have their card fronts and backs
 * @param checkedStyle this is the parameter for the file graphic that will be the bottom of the card
 * @param upStyle this is the parameter for the file graphic that will be the top of the card, or the back
 * @return returns a formatted button with a front and back
 */
	public ImageButtonStyle ButtonStyles(String checkedStyle, String upStyle) {
		skin = new Skin();
		skin.addRegions(cardAtlas);
		imageButtonStyle = new ImageButtonStyle();
		imageButtonStyle.checked = skin.getDrawable(checkedStyle);
		imageButtonStyle.up = skin.getDrawable(upStyle);

		return imageButtonStyle;
	}
/**
 * This method formats all images to the write size, positions them, and puts them on the stage
 * @param image this is an image that is not yes visible and must be resized
 * @return returns a formatted and proper size image located at the written position
 */
	public Image imageSettings(Image image) {
		image.setScale(4f);
		stage.addActor(image);
		image.setPosition(1130, 470);
		return image;

	}
/**
 * This method does the same as the previous, but for a different set of images
 * @param image this is an image that has not been set visible or scaled up yet
 * @return returns a visible and scaled up image
 */
	public Image imageScoreSettings(Image image) {
		image.setScale(3f);
		stage.addActor(image);
		image.setVisible(true);
		return image;

	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		inputMultiplxer.addProcessor(this);
		inputMultiplxer.addProcessor(stage);

		font = new BitmapFont();
		playerTurn = new Label("Turn: ", new Label.LabelStyle(font, Color.WHITE));
		playerTurn.setPosition(950, 500);
		stage.addActor(playerTurn);
		playerTurn.setFontScale(4.5f);
		iceCreamWins = new Label("", new Label.LabelStyle(font, Color.WHITE));
		iceCreamWins.setPosition(1070, 190);
		iceCreamWins.setFontScale(3f);
		stage.addActor(iceCreamWins);
		popsicleWins = new Label("", new Label.LabelStyle(font, Color.WHITE));
		popsicleWins.setPosition(1070, 90);
		popsicleWins.setFontScale(3f);
		stage.addActor(popsicleWins);

		iceCream = new Image(texture = new Texture("IceCreamCharacter.png"));
		popsicle = new Image(texture = new Texture("PopsicleCharacter.png"));
		imageSettings(iceCream);
		iceCream.setVisible(false);
		imageSettings(popsicle);
		popsicle.setVisible(false);
		iceCreamScore = new Image(texture = new Texture("IceCreamCharacter.png"));
		popsicleScore = new Image(texture = new Texture("PopsicleCharacter.png"));
		imageScoreSettings(iceCreamScore);
		iceCreamScore.setPosition(1010, 150);
		imageScoreSettings(popsicleScore);
		popsicleScore.setPosition(1025, 50);


		buttons[0] = new ImageButton(ButtonStyles("Card1", "CardBack"));
		buttons[1] = new ImageButton(ButtonStyles("Card1", "CardBack")); 
		buttons[2] = new ImageButton(ButtonStyles("Card10", "CardBack")); 
		buttons[3] = new ImageButton(ButtonStyles("Card10", "CardBack")); 
		buttons[4] = new ImageButton(ButtonStyles("Card11", "CardBack"));
		buttons[5] = new ImageButton(ButtonStyles("Card11", "CardBack")); 
		buttons[6] = new ImageButton(ButtonStyles("Card12", "CardBack")); 
		buttons[7] = new ImageButton(ButtonStyles("Card12", "CardBack")); 
		buttons[8] = new ImageButton(ButtonStyles("Card13", "CardBack")); 
		buttons[9] = new ImageButton(ButtonStyles("Card13", "CardBack")); 
		buttons[10] = new ImageButton(ButtonStyles("Card14", "CardBack")); 
		buttons[11] = new ImageButton(ButtonStyles("Card14", "CardBack")); 
		buttons[12] = new ImageButton(ButtonStyles("Card15", "CardBack")); 
		buttons[13] = new ImageButton(ButtonStyles("Card15", "CardBack")); 
		buttons[14] = new ImageButton(ButtonStyles("Card16", "CardBack")); 
		buttons[15] = new ImageButton(ButtonStyles("Card16", "CardBack")); 
		buttons[16] = new ImageButton(ButtonStyles("Card17", "CardBack"));
		buttons[17] = new ImageButton(ButtonStyles("Card17", "CardBack"));
		buttons[18] = new ImageButton(ButtonStyles("Card18", "CardBack"));
		buttons[19] = new ImageButton(ButtonStyles("Card18", "CardBack"));
		buttons[20] = new ImageButton(ButtonStyles("Card2", "CardBack")); 
		buttons[21] = new ImageButton(ButtonStyles("Card2", "CardBack")); 
		buttons[22] = new ImageButton(ButtonStyles("Card3", "CardBack")); 
		buttons[23] = new ImageButton(ButtonStyles("Card3", "CardBack")); 
		buttons[24] = new ImageButton(ButtonStyles("Card4", "CardBack")); 
		buttons[25] = new ImageButton(ButtonStyles("Card4", "CardBack")); 
		buttons[26] = new ImageButton(ButtonStyles("Card5", "CardBack")); 
		buttons[27] = new ImageButton(ButtonStyles("Card5", "CardBack")); 
		buttons[28] = new ImageButton(ButtonStyles("Card6", "CardBack")); 
		buttons[29] = new ImageButton(ButtonStyles("Card6", "CardBack")); 
		buttons[30] = new ImageButton(ButtonStyles("Card7", "CardBack")); 
		buttons[31] = new ImageButton(ButtonStyles("Card7", "CardBack")); 
		buttons[32] = new ImageButton(ButtonStyles("Card8", "CardBack")); 
		buttons[33] = new ImageButton(ButtonStyles("Card8", "CardBack")); 
		buttons[34] = new ImageButton(ButtonStyles("Card9", "CardBack")); 
		buttons[35] = new ImageButton(ButtonStyles("Card9", "CardBack"));

		ArrayList<Integer> positionValue = new ArrayList<Integer>();

		for (int i = 0; i <= 35; i++) {
			buttonSettings(buttons[i]);
			positionValue.add(i);
		}

		daButtons = new ButtonGroup<ImageButton>(buttons[0], 
				buttons[1], buttons[2], buttons[3], buttons[4], buttons[5], buttons[6], buttons[7], 
				buttons[8], buttons[9], buttons[10], buttons[11], buttons[12], buttons[13], buttons[14], 
				buttons[15], buttons[16], buttons[17], buttons[18], buttons[19], buttons[20], buttons[21], 
				buttons[22], buttons[23], buttons[24], buttons[25], buttons[26], buttons[27], buttons[28], 
				buttons[29], buttons[30], buttons[31], buttons[32], buttons[33], buttons[34], buttons[35]);

		// this shuffles a set of numerous values, each of which is tied to a specific location in a grid
		// then a for loop goes through the shuffled list and randomly positions the card buttons in a grid
		Collections.shuffle(positionValue);

		for(int i = 0; i <= 35; i++) {

			switch (positionValue.get(i)) {
			case 0:
				buttons[i].setPosition(300, 100);
				break;
			case 1:
				buttons[i].setPosition(400, 100);
				break;
			case 2:
				buttons[i].setPosition(500, 100);
				break;
			case 3:
				buttons[i].setPosition(600, 100);
				break;
			case 4:
				buttons[i].setPosition(700, 100);
				break;
			case 5:
				buttons[i].setPosition(800, 100);
				break;
			case 6:
				buttons[i].setPosition(300, 200);
				break;
			case 7:
				buttons[i].setPosition(400, 200);
				break;
			case 8:
				buttons[i].setPosition(500, 200);
				break;
			case 9:
				buttons[i].setPosition(600, 200);
				break;
			case 10:
				buttons[i].setPosition(700, 200);
				break;
			case 11:
				buttons[i].setPosition(800, 200);
				break;
			case 12:
				buttons[i].setPosition(300, 300);
				break;
			case 13:
				buttons[i].setPosition(400, 300);
				break;
			case 14:
				buttons[i].setPosition(500, 300);
				break;
			case 15:
				buttons[i].setPosition(600, 300);
				break;
			case 16:
				buttons[i].setPosition(700, 300);
				break;
			case 17:
				buttons[i].setPosition(800, 300);
				break;
			case 18:
				buttons[i].setPosition(300, 400);
				break;
			case 19:
				buttons[i].setPosition(400, 400);
				break;
			case 20:
				buttons[i].setPosition(500, 400);
				break;
			case 21:
				buttons[i].setPosition(600, 400);
				break;
			case 22:
				buttons[i].setPosition(700, 400);
				break;
			case 23:
				buttons[i].setPosition(800, 400);
				break;
			case 24:
				buttons[i].setPosition(300, 500);
				break;
			case 25:
				buttons[i].setPosition(400, 500);
				break;
			case 26:
				buttons[i].setPosition(500, 500);
				break;
			case 27:
				buttons[i].setPosition(600, 500);
				break;
			case 28:
				buttons[i].setPosition(700, 500);
				break;
			case 29:
				buttons[i].setPosition(800, 500);
				break;
			case 30:
				buttons[i].setPosition(300, 600);
				break;
			case 31:
				buttons[i].setPosition(400, 600);
				break;
			case 32:
				buttons[i].setPosition(500, 600);
				break;
			case 33:
				buttons[i].setPosition(600, 600);
				break;
			case 34:
				buttons[i].setPosition(700, 600);
				break;
			case 35:
				buttons[i].setPosition(800, 600);
				break;

			}

		}

	}


/**
 * this method scales buttons and makes them visible
 * @param button this parameter receives a button that is invisible or not the right size
 * @return returns a visible proper-size button
 */
	public ImageButton buttonSettings(ImageButton button) {

		button.setTransform(true);
		button.setScale(3);
		stage.addActor(button);
		button.setVisible(true);

		return button;
	}
/**
 * this method carries the bulk of the logic of this minigame. 
 * It assigns each button a number and then compares the numbers to see if the buttons are a match
 * @param choice1 this is the first clicked button
 * @param choice2 this is the second clicked button
 * @return returns true or false depending on whether the buttons are a match or not
 */
	public boolean checkMatch(ImageButton choice1, ImageButton choice2) {

		match = false;
		int id1 = 0;
		int id2 = 0;

		if (choice1.equals(buttons[0])) {
			id1 = 0;
		} else if (choice1.equals(buttons[2])) {
			id1 = 1;
		}else if (choice1.equals(buttons[4])) {
			id1 = 2;
		}else if (choice1.equals(buttons[6])) {
			id1 = 3;
		}else if (choice1.equals(buttons[8])) {
			id1 = 4;
		}else if (choice1.equals(buttons[10])) {
			id1 = 5;
		}else if (choice1.equals(buttons[12])) {
			id1 = 6;
		}else if (choice1.equals(buttons[14])) {
			id1 = 7;
		}else if (choice1.equals(buttons[16])) {
			id1 = 8;
		}else if (choice1.equals(buttons[18])) {
			id1 = 9;
		}else if (choice1.equals(buttons[20])) {
			id1 = 10;
		}else if (choice1.equals(buttons[22])) {
			id1 = 11;
		}else if (choice1.equals(buttons[24])) {
			id1 = 12;
		}else if (choice1.equals(buttons[26])) {
			id1 = 13;
		}else if (choice1.equals(buttons[28])) {
			id1 = 14;
		}else if (choice1.equals(buttons[30])) {
			id1 = 15;
		}else if (choice1.equals(buttons[32])) {
			id1 = 16;
		}else if (choice1.equals(buttons[34])) {
			id1 = 17;
		}

		if (choice2.equals(buttons[1])) {
			id2 = 0;
		} else if (choice2.equals(buttons[3])) {
			id2 = 1;
		}else if (choice2.equals(buttons[5])) {
			id2 = 2;
		}else if (choice2.equals(buttons[7])) {
			id2 = 3;
		}else if (choice2.equals(buttons[9])) {
			id2 = 4;
		}else if (choice2.equals(buttons[11])) {
			id2 = 5;
		}else if (choice2.equals(buttons[13])) {
			id2 = 6;
		}else if (choice2.equals(buttons[15])) {
			id2 = 7;
		}else if (choice2.equals(buttons[17])) {
			id2 = 8;
		}else if (choice2.equals(buttons[19])) {
			id2 = 9;
		}else if (choice2.equals(buttons[21])) {
			id2 = 10;
		}else if (choice2.equals(buttons[23])) {
			id2 = 11;
		}else if (choice2.equals(buttons[25])) {
			id2 = 12;
		}else if (choice2.equals(buttons[27])) {
			id2 = 13;
		}else if (choice2.equals(buttons[29])) {
			id2 = 14;
		}else if (choice2.equals(buttons[31])) {
			id2 = 15;
		}else if (choice2.equals(buttons[33])) {
			id2 = 16;
		}else if (choice2.equals(buttons[35])) {
			id2 = 17;
		}

		if (id1 == id2) {
			match = true;
		} else {
			match = false;
		}


		return match;
	}

	@Override
	public void render(float delta) {
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0, width, height);
		batch.end();
		
		audioManager.playChillGame();
//		Gdx.gl.glClearColor(1, 1, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		Gdx.input.setInputProcessor(inputMultiplxer);
		

		match = false;

		
		if (done == 126) {
			if (iceCreamMatches > popsicleMatches) {
				audioManager.stopAll();
				game.setScreen(new IceCreamWin(game));
			} else if (iceCreamMatches < popsicleMatches) {
				audioManager.stopAll();
				game.setScreen(new PopsicleWin(game));
			}
		}
				
		daButtons.setMaxCheckCount(2);
		daButtons.setMinCheckCount(0);

		if (daButtons.getAllChecked().size == 2) {

			if (checkMatch(daButtons.getAllChecked().first(), daButtons.getAllChecked().peek()) == true) {

				match = false;	
				daButtons.getAllChecked().first().setVisible(false);
				daButtons.getAllChecked().peek().setVisible(false);
				done++;

				if (playerTurnNumber) {
					iceCreamMatches++;
					playerTurnNumber = false;
				}
				if (playerTurnNumber2) {
					popsicleMatches++;
					playerTurnNumber2 = false;
				}

				float delay = 0.1f; // seconds

				Timer.schedule(new Task(){
					@Override
					public void run() {
						daButtons.uncheckAll();
					}
				}, delay);

			} else {
				if (playerTurnNumber) {
					playerTurnNumber = false;
				}
				if (playerTurnNumber2) {
					playerTurnNumber2 = false;
				}
				
				float delay = 0.1f; // seconds

				Timer.schedule(new Task(){
					@Override
					public void run() {
						daButtons.uncheckAll();
					}
				}, delay);

			}

		}
		
		if (turns%4 == 0 || turns%4 == 1) {
			playerTurnNumber = true;
			playerTurnNumber2 = false;
		} else {
			playerTurnNumber2 = true;
			playerTurnNumber = false;
		}

		if (playerTurnNumber) {
			iceCream.setVisible(true);
			popsicle.setVisible(false);
		} 
		if (playerTurnNumber2) {
			iceCream.setVisible(false);
			popsicle.setVisible(true);
		}

		iceCreamWins.setText(" = " + iceCreamMatches);
		popsicleWins.setText(" = " + popsicleMatches);

	}

	@Override
	public void resize(int width, int height) {
		//		viewport.update(width, height);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		turns++;
		return false;
	}

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
		// TODO Auto-generated method stub
		return false;
	}




}

