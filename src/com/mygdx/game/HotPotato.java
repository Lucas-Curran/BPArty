package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HotPotato implements Screen, InputProcessor {

	public int width = 1280;
	public int height = 720;

	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	private Viewport viewport;

	private Stage stage;
	private SpriteBatch batch = new SpriteBatch();
	private Texture texture = new Texture("FireyPotato.png");
	private Texture textureBackground = new Texture("HotPotatoBackground.png");
	private Image iceCream, popsicle;
	private Sprite hotPotato = new Sprite(texture);
	private Sprite background = new Sprite(textureBackground);
	private Label clicky;
	private Label iceClicks;
	private Label popClicks;
	private Label timeRemaining;
	private BitmapFont font;
	private int playerClicks = 0;
	private int clicks = 0;
	private int reach = 0;
	private int time = 25;
	private boolean popsicleHere = false;
	private boolean iceCreamHere = false;
	private boolean calculating = false;
	private boolean movingRight = false;
	private boolean movingLeft = false;



	Random rNum = new Random();

	final BPArty game;

	AudioManager audioManager = new AudioManager();
	
	public HotPotato(final BPArty game) {
		this.game = game;
		camera.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		viewport.apply();
	}
	/**
	 * this method is responsible for taking all the images that are used in this minigame and formatting them properly
	 * @param image takes in a image file to be formatted
	 * @return returns the image with all the proper formatting
	 */
	public Image imageSettings(Image image) {
		image.setScale(4f);
		stage.addActor(image);
		image.setVisible(true);

		return image;

	}

	@Override
	public void show() {
		stage = new Stage();

		Gdx.input.setInputProcessor(this);
		iceCream = new Image(texture = new Texture("IceCreamCharacter.png"));
		popsicle = new Image(texture = new Texture("PopsicleCharacter.png"));
		imageSettings(iceCream);
		iceCream.setPosition(100, 100);
		imageSettings(popsicle);
		popsicle.setPosition(1050, 100);

		hotPotato.setPosition(150, 100);

		font = new BitmapFont();

		clicky = new Label("", new Label.LabelStyle(font, Color.BLUE));
		iceClicks = new Label("", new Label.LabelStyle(font, Color.WHITE));
		popClicks = new Label("", new Label.LabelStyle(font, Color.WHITE));
		timeRemaining = new Label("", new Label.LabelStyle(font, Color.RED));

		

	}

	@Override
	public void render(float delta) {
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		batch.draw(hotPotato, hotPotato.getX(), hotPotato.getY());
		batch.end();
		stage.draw();
		
		audioManager.playIntenseGame();

		if (calculating) {
			clicks = 20 + rNum.nextInt(20);
		}

		if (movingRight) {
			if (hotPotato.getX() != 1030.0) {
				calculating = true;
				keyDown(Input.Keys.D);
				iceCreamHere = false;
				time = 10 + rNum.nextInt(11);
				Timer.instance().clear();
			}  else {
				movingRight = false;
				popsicleHere = true;
				calculating = false;
				playerClicks = 0;
				reach = clicks;

				float delay = 1;

				Timer.schedule(new Task(){
					@Override
					public void run() {
						time--;
					}
				}, delay, 1, time);

			}
		}

		if (time == 0) {
			if (hotPotato.getX() == 1030.0) {
				audioManager.stopAll();
				game.setScreen(new IceCreamWin(game));
			} else if (hotPotato.getX() == 150) {
				audioManager.stopAll();
				game.setScreen(new PopsicleWin(game));
			}
		}

		if (popsicleHere) {
			hotPotato.setPosition(1030, 100);
		}

		if (movingLeft) {	
			if (hotPotato.getX() != 150) {
				calculating = true;
				keyDown(Input.Keys.LEFT);
				popsicleHere = false;
				time = 20 + rNum.nextInt(11);
				Timer.instance().clear();
			} else {
				movingLeft = false;
				iceCreamHere = true;
				calculating = false;
				playerClicks = 0;
				reach = clicks;

				float delay = 1;

				Timer.schedule(new Task(){
					@Override
					public void run() {
						time--;
					}
				}, delay, 1, time);

			}
		}

		if (iceCreamHere) {
			hotPotato.setPosition(150, 100);
		}

		clicky.setText("Clicks to rid the Tater! = " + clicks);
		clicky.setPosition(200, 500);
		clicky.setFontScale(5);
		stage.addActor(clicky);

		if (iceCreamHere) {
			iceClicks.setText(playerClicks);
			iceClicks.setPosition(125, 250);
			iceClicks.setFontScale(3);
			stage.addActor(iceClicks);
		}

		if (popsicleHere) {
			popClicks.setText(playerClicks);
			popClicks.setPosition(1050, 250);
			popClicks.setFontScale(3);
			stage.addActor(popClicks);
		}

		timeRemaining.setText(time);
		timeRemaining.setPosition(600, 200);
		timeRemaining.setFontScale(3);
		stage.addActor(timeRemaining);

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

	@Override
	public boolean keyDown(int keycode) {

		// the translateY parameters in these if statements are a quadratic motion function
		// the x in the function is the x position of the potato converted into a suitable variable
		// this models the path of a projectile, to say the least

		float x = (hotPotato.getX()/100);

		if (keycode == Input.Keys.LEFT && playerClicks == reach) {
			movingLeft = true;
			hotPotato.translateX(-5f);
			hotPotato.translateY(-(float) (-0.08*(x)*(x)+3));
		} else if (keycode == Input.Keys.D && playerClicks == reach) {
			movingRight = true;
			hotPotato.translateX(5f);
			hotPotato.translateY((float) (-0.072*(x)*(x)+3));
		}

		if (keycode == Input.Keys.SPACE) {
			if (playerClicks < clicks) {
				playerClicks++;
			}
		}

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
