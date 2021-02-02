package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RockPaperScissors implements Screen, InputProcessor{

	public int width = 1280;
	public int height = 720;

	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	private Viewport viewport;
	private Stage stage;
	private Skin skin;
	private Texture texture;
	private TextureAtlas cardAtlas = new TextureAtlas("RPSSpriteSheet.txt");
	private ImageButtonStyle imageButtonStyle;

	private ImageButton airBtn, devilBtn, fireBtn, lightningBtn, rockBtn, treeBtn, waterBtn,
	airBtn2, devilBtn2, fireBtn2, lightningBtn2, rockBtn2, treeBtn2, waterBtn2;
	private Image air, devil, fire, lightning, rock, tree, water, iceCreamWin, popsicleWin,
	air2, devil2, fire2, lightning2, rock2, tree2, water2, iceCream, popsicle;

	private Label timeRemaining;
	private Label icePoints;
	private Label popPoints;
	private Label playersReady;
	private int iceWins = 0;
	private int popWins = 0;
	private BitmapFont font;
	private int choice1;
	private int choice2;
	private boolean addPoints = false;
	private int time = 60;

	AudioManager audioManager = new AudioManager();
	
	InputMultiplexer inputMultiplxer = new InputMultiplexer();

	final BPArty game;

	public RockPaperScissors(final BPArty game) {
		this.game = game;
		camera.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		viewport.apply();
	}
	/**
	 * This method provides all the formatting for each button that is used in the game
	 * @param upStyle this parameter takes the String name of the file containing the texture and applies it
	 * @return returns a formatted button with the correct image
	 */
	public ImageButtonStyle ButtonStyles(String upStyle) {
		skin = new Skin();
		skin.addRegions(cardAtlas);
		imageButtonStyle = new ImageButtonStyle();
		imageButtonStyle.up = skin.getDrawable(upStyle);

		return imageButtonStyle;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		inputMultiplxer.addProcessor(this);
		inputMultiplxer.addProcessor(stage);

		iceCream = new Image(texture = new Texture("IceCreamCharacter.png"));
		popsicle = new Image(texture = new Texture("PopsicleCharacter.png"));
		characterSettings(iceCream);
		iceCream.setPosition(125, 300);
		characterSettings(popsicle);
		popsicle.setPosition(1050, 300);

		font = new BitmapFont();

		timeRemaining = new Label("", new Label.LabelStyle(font, Color.RED));
		icePoints = new Label("", new Label.LabelStyle(font, Color.GREEN));
		popPoints = new Label("", new Label.LabelStyle(font, Color.GREEN));
		playersReady = new Label("", new Label.LabelStyle(font, Color.YELLOW));

		airBtn = new ImageButton(ButtonStyles("Air"));
		devilBtn = new ImageButton(ButtonStyles("Devil"));
		fireBtn = new ImageButton(ButtonStyles("Fire"));
		lightningBtn = new ImageButton(ButtonStyles("Lightning"));
		rockBtn = new ImageButton(ButtonStyles("Rock"));
		treeBtn = new ImageButton(ButtonStyles("Tree"));
		waterBtn = new ImageButton(ButtonStyles("Water"));
		airBtn2 = new ImageButton(ButtonStyles("Air"));
		devilBtn2 = new ImageButton(ButtonStyles("Devil"));
		fireBtn2 = new ImageButton(ButtonStyles("Fire"));
		lightningBtn2 = new ImageButton(ButtonStyles("Lightning"));
		rockBtn2 = new ImageButton(ButtonStyles("Rock"));
		treeBtn2 = new ImageButton(ButtonStyles("Tree"));
		waterBtn2 = new ImageButton(ButtonStyles("Water"));

		air = new Image(texture = new Texture("Air.png"));
		devil = new Image(texture = new Texture("Devil.png"));
		fire = new Image(texture = new Texture("Fire.png"));
		lightning = new Image(texture = new Texture("Lightning.png"));
		rock = new Image(texture = new Texture("Rock.png"));
		tree = new Image(texture = new Texture("Tree.png"));
		water = new Image(texture = new Texture("Water.png"));
		air2 = new Image(texture = new Texture("Air.png"));
		devil2 = new Image(texture = new Texture("Devil.png"));
		fire2 = new Image(texture = new Texture("Fire.png"));
		lightning2 = new Image(texture = new Texture("Lightning.png"));
		rock2 = new Image(texture = new Texture("Rock.png"));
		tree2 = new Image(texture = new Texture("Tree.png"));
		water2 = new Image(texture = new Texture("Water.png"));
		iceCreamWin = new Image(texture = new Texture("IceCreamWin.png"));
		popsicleWin = new Image(texture = new Texture("PopsicleWin.png"));

		iceCreamWin.setPosition(400, 200);
		stage.addActor(iceCreamWin);
		iceCreamWin.setVisible(false);
		popsicleWin.setPosition(400, 200);
		stage.addActor(popsicleWin);
		popsicleWin.setVisible(false);

		airBtn.setPosition(50, 200);
		devilBtn.setPosition(130, 200);
		fireBtn.setPosition(200, 200);
		lightningBtn.setPosition(50, 125);
		rockBtn.setPosition(130, 125);
		treeBtn.setPosition(200, 125);
		waterBtn.setPosition(130, 50);
		airBtn2.setPosition(950, 200);
		devilBtn2.setPosition(1030, 200);
		fireBtn2.setPosition(1100, 200);
		lightningBtn2.setPosition(950, 125);
		rockBtn2.setPosition(1030, 125);
		treeBtn2.setPosition(1100, 125);
		waterBtn2.setPosition(1030, 50);

		buttonSettings(airBtn);
		buttonSettings(devilBtn);
		buttonSettings(fireBtn);
		buttonSettings(lightningBtn);
		buttonSettings(rockBtn);
		buttonSettings(treeBtn);
		buttonSettings(waterBtn);
		buttonSettings(airBtn2);
		buttonSettings(devilBtn2);
		buttonSettings(fireBtn2);
		buttonSettings(lightningBtn2);
		buttonSettings(rockBtn2);
		buttonSettings(treeBtn2);
		buttonSettings(waterBtn2);

		imageSettings(air);
		imageSettings(devil);
		imageSettings(fire);
		imageSettings(lightning);
		imageSettings(rock);
		imageSettings(tree);
		imageSettings(water);
		imageSettings2(air2);
		imageSettings2(devil2);
		imageSettings2(fire2);
		imageSettings2(lightning2);
		imageSettings2(rock2);
		imageSettings2(tree2);
		imageSettings2(water2);

		float delay = 1; // seconds
		Timer.schedule(new Task(){
			@Override
			public void run() {
				time--;
			}
		}, delay, 1, 60);

	}
	/**
	 * This method resizes all buttons 
	 * @param button this parameter accepts a button that has been initialized but is too small
	 * @return returns a resized a button
	 */
	public ImageButton buttonSettings(ImageButton button) {

		button.setTransform(true);
		button.setScale(2);
		stage.addActor(button);
		button.setVisible(true);

		return button;
	}
	/**
	 * This method resizes and positions all the images for different options
	 * @param image this parameter accepts an image that has been initialized but is too small and in the wrong place
	 * @return returns a resized image correctly positioned on the screen
	 */
	public Image imageSettings(Image image) {
		image.setScale(4);
		image.setPosition(100, 500);
		stage.addActor(image);
		image.setVisible(false);

		return image;
	}
	/**
	 * This method does the same thing as the previous method, but for all the images of player 2
	 * @param image this parameter accepts an image that has been initialized but is too small and in the wrong place
	 * @return returns a resized image correctly positioned on the screen
	 */
	public Image imageSettings2(Image image) {
		image.setScale(4);
		image.setPosition(975, 500);
		stage.addActor(image);
		image.setVisible(false);

		return image;
	}
	/**
	 * This method resizes and adds the two character images
	 * @param image this parameter accepts a character image to resize and add to the stage
	 * @return returns a resized character image
	 */
	public Image characterSettings(Image image) {
		image.setScale(3.5f);
		stage.addActor(image);
		image.setVisible(true);

		return image;
	}
	/**
	 * This method handles the majority of the minigame logic and gameplay, resetting choices and determing wins and losses
	 * @param choice1 this is the choice of the first player, converted into an int corresponding to an option
	 * @param choice2 this is the choice of the second player, converted into an int corresponding to an option
	 */
	public void play(int choice1, int choice2) {

		air.setVisible(false);
		devil.setVisible(false);
		fire.setVisible(false);
		lightning.setVisible(false);
		rock.setVisible(false);
		tree.setVisible(false);
		water.setVisible(false);
		air2.setVisible(false);
		devil2.setVisible(false);
		fire2.setVisible(false);
		lightning2.setVisible(false);
		rock2.setVisible(false);
		tree2.setVisible(false);
		water2.setVisible(false);

		if (choice2 == 0) {
			air2.setVisible(true);
		} else if (choice2 == 1) {
			devil2.setVisible(true);
		} else if (choice2 == 2) {
			fire2.setVisible(true);
		} else if (choice2 == 3) {
			lightning2.setVisible(true);
		} else if (choice2 == 4) {
			rock2.setVisible(true);
		} else if (choice2 == 5) {
			tree2.setVisible(true);
		} else if (choice2 == 6) {
			water2.setVisible(true);
		}

		if (choice1 == 0) {
			air.setVisible(true);
		} else if (choice1 == 1) {
			devil.setVisible(true);
		} else if (choice1 == 2) {
			fire.setVisible(true);
		} else if (choice1 == 3) {
			lightning.setVisible(true);
		} else if (choice1 == 4) {
			rock.setVisible(true);
		} else if (choice1 == 5) {
			tree.setVisible(true);
		} else if (choice1 == 6) {
			water.setVisible(true);
		}

		if (addPoints) {
			if (choice1== choice2) {
				// something like you tied
			} else if (choice1== 0 && choice2 == 1) {
				iceWins++;

			} else if (choice1== 0 && choice2 == 2) {
				popWins++;

			} else if (choice1== 0 && choice2 == 3) {
				popWins++;

			} else if (choice1== 0 && choice2 == 4) {
				iceWins++;

			} else if (choice1== 0 && choice2 == 5) {
				popWins++;

			} else if (choice1== 0 && choice2 == 6) {
				iceWins++;

			} else if (choice1== 1 && choice2 == 0) {
				popWins++;

			} else if (choice1== 1 && choice2 == 2) {
				iceWins++;

			} else if (choice1== 1 && choice2 == 3) {
				iceWins++;

			} else if (choice1== 1 && choice2 == 4) {
				iceWins++;

			} else if (choice1== 1 && choice2 == 5) {
				popWins++;

			} else if (choice1== 1 && choice2 == 6) {
				popWins++;

			} else if (choice1== 2 && choice2 == 0) {
				iceWins++;

			} else if (choice1== 2 && choice2 == 1) {
				popWins++;

			} else if (choice1== 2 && choice2 == 3) {
				popWins++;

			} else if (choice1== 2 && choice2 == 4) {
				iceWins++;

			} else if (choice1== 2 && choice2 == 5) {
				iceWins++;

			} else if (choice1== 2 && choice2 == 6) {
				popWins++;

			} else if (choice1== 3 && choice2 == 0) {
				iceWins++;

			} else if (choice1== 3 && choice2 == 1) {
				popWins++;

			} else if (choice1== 3 && choice2 == 2) {
				iceWins++;

			} else if (choice1== 3 && choice2 == 4) {
				popWins++;

			} else if (choice1== 3 && choice2 == 5) {
				iceWins++;

			} else if (choice1== 3 && choice2 == 6) {
				popWins++;

			} else if (choice1== 4 && choice2 == 0) {
				popWins++;

			} else if (choice1== 4 && choice2 == 1) {
				popWins++;

			} else if (choice1== 4 && choice2 == 2) {
				popWins++;

			} else if (choice1== 4 && choice2 == 3) {
				iceWins++;

			} else if (choice1== 4 && choice2 == 5) {
				iceWins++;

			} else if (choice1== 4 && choice2 == 6) {
				iceWins++;

			} else if (choice1== 5 && choice2 == 0) {
				iceWins++;

			} else if (choice1== 5 && choice2 == 1) {
				iceWins++;

			} else if (choice1== 5 && choice2 == 2) {
				popWins++;

			} else if (choice1== 5 && choice2 == 3) {
				popWins++;

			} else if (choice1== 5 && choice2 == 4) {
				popWins++;

			} else if (choice1== 5 && choice2 == 6) {
				iceWins++;

			} else if (choice1== 6 && choice2 == 0) {
				popWins++;

			} else if (choice1== 6 && choice2 == 1) {
				iceWins++;

			} else if (choice1== 6 && choice2 == 2) {
				iceWins++;

			} else if (choice1== 6 && choice2 == 3) {
				iceWins++;

			} else if (choice1== 6 && choice2 == 4) {
				popWins++;

			} else if (choice1== 6 && choice2 == 5) {
				popWins++;

			}
		}

		addPoints = false;

	}
	@Override
	public void render(float delta) {
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0.5f, 0.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
		audioManager.playIntenseGame();
		
		Gdx.input.setInputProcessor(inputMultiplxer);	

		if (limit % 2 == 0) {
			playersReady.setText("Players Not Ready!");
			play(choice1, choice2);
		} else {
			playersReady.setText("Players Ready!");
		}

		if (time == 0) {
			if (iceWins > popWins) {
				audioManager.stopAll();
				game.setScreen(new IceCreamWin(game));
			} else if (iceWins < popWins) {
				audioManager.stopAll();
				game.setScreen(new PopsicleWin(game));
			} else if (iceWins == popWins) {
				Random rNum = new Random();
				int idkMan = rNum.nextInt(1);
				if (idkMan == 0) {
					game.setScreen(new IceCreamWin(game));
				} else if (idkMan == 1) {
					game.setScreen(new PopsicleWin(game));
				}
			}
		}

		timeRemaining.setText(time);
		timeRemaining.setPosition(575, 200);
		timeRemaining.setFontScale(3);
		stage.addActor(timeRemaining);

		icePoints.setText(iceWins);
		icePoints.setPosition(250, 350);
		icePoints.setFontScale(3);
		stage.addActor(icePoints);

		popPoints.setText(popWins);
		popPoints.setPosition(950, 350);
		popPoints.setFontScale(3);
		stage.addActor(popPoints);
		
		playersReady.setPosition(450, 300);
		playersReady.setFontScale(3);
		stage.addActor(playersReady);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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

	int limit = 0;

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.NUM_1) {
			choice1 = 0;

		} else if (keycode == Input.Keys.NUM_2) {
			choice1 = 1;
			limit++;
		} else if (keycode == Input.Keys.NUM_3) {
			choice1 = 2;
			limit++;
		} else if (keycode == Input.Keys.Q) {
			choice1 = 3;
			limit++;
		} else if (keycode == Input.Keys.W) {
			choice1 = 4;
			limit++;
		} else if (keycode == Input.Keys.E) {
			choice1 = 5;
			limit++;
		} else if (keycode == Input.Keys.S) {
			choice1 = 6;
			limit++;
		}

		if (keycode == Input.Keys.NUM_8) {
			choice2 = 0;
			limit++;
		} else if (keycode == Input.Keys.NUM_9) {
			choice2 = 1;
			limit++;
		} else if (keycode == Input.Keys.NUM_0) {
			choice2 = 2;
			limit++;
		} else if (keycode == Input.Keys.I) {
			choice2 = 3;
			limit++;
		} else if (keycode == Input.Keys.O) {
			choice2 = 4;
			limit++;
		} else if (keycode == Input.Keys.P) {
			choice2 = 5;
			limit++;
		} else if (keycode == Input.Keys.L) {
			choice2 = 6;
			limit++;
		}


		addPoints = true;

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
		// TODO Auto-generated method stub
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
