package com.mygdx.game;

import com.badlogic.gdx.Screen;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

public class MainMenuScreen implements Screen {
	
	public static final Logger LOGGER = LogManager.getLogger(MainMenuScreen.class);
	
	private SQLiteConnect sqlite = new SQLiteConnect();
	
	final BPArty game;
	Texture background;
	Stage stage;
	TextButton startBtn, quitBtn, settingBtn, resumeBtn;
	Table table;
	Skin skin;

	AudioManager audioManager = new AudioManager();
	
	OrthographicCamera camera;
	
	public MainMenuScreen(final BPArty game) {
		this.game = game;

		background = new Texture("MainMenu.png");		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, BPArty.WIDTH/2, BPArty.HEIGHT/2);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		audioManager.playMenu();
		
		game.batch.begin();
		game.batch.draw(background, 0, 0, BPArty.WIDTH/2, BPArty.HEIGHT/2);
		game.batch.end();

		stage.act(delta);
		stage.draw();
		
		try {
			
		if  (startBtn.isPressed()) {
			audioManager.playClick();			
			sqlite.updateAll(50, 0, 0, 0, 27,40, 0, 0);
			audioManager.stopAll();		
			LOGGER.info("Start button pressed.");
			game.setScreen(new Board(game));
		} else if (settingBtn.isPressed()) {
			audioManager.playClick();
			audioManager.stopAll();
			game.setScreen(new TransitionScreen(new SettingScreen(game), game));
			LOGGER.info("Settings button pressed");
		} else if (quitBtn.isPressed()) {
			audioManager.playClick();
			LOGGER.info("Game exited.");
			Gdx.app.exit();
		} else if (resumeBtn.isPressed()) {
			audioManager.playClick();
			audioManager.stopAll();
			LOGGER.info("Start button pressed.");			
			game.setScreen(new Board(game));
		  }
		} catch (Exception e) {
			try {
				LOGGER.fatal("A fatal error has occured. Game crashed.");
				CrashInfo ci = new CrashInfo(e, game);
				ci.writeCrash();
			} catch (Exception n) {
				
			}	
		}
	}

	@Override
	public void show() {
		stage = new Stage();
		
		table = new Table(skin);
		table.setBounds(0,0, BPArty.WIDTH, BPArty.HEIGHT);
		Gdx.input.setInputProcessor(stage);
		startBtn = new TextButton("Start", game.ButtonStyles("MenuRectangle", "PlayButton"));
		quitBtn = new TextButton("Quit", game.ButtonStyles("MenuRectangle", "QuitButton"));
		settingBtn = new TextButton("Settings", game.ButtonStyles("MenuRectangle", "SettingButton"));
		resumeBtn = new TextButton("Resume", game.ButtonStyles("MenuRectangle", "ResumeButton"));
		buttonSettings(startBtn);
		buttonSettings(quitBtn);
		buttonSettings(settingBtn);
		buttonSettings(resumeBtn);

		table.left().padLeft(10);
		table.add(startBtn).width(420).height(170);
		table.row();
		table.add(resumeBtn).width(420).height(170);
		table.row();
		table.add(settingBtn).width(420).height(170);
		table.row();
		table.add(quitBtn).width(420).height(170);
	
		stage.addActor(table);
		
	}

	public TextButton buttonSettings(TextButton button) {
		button.getLabel().setAlignment(Align.left);
		button.getLabelCell().padLeft(35);
		button.getLabel().setFontScale(4,4);
		return button;
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
		//game.batch.dispose();	
	}
	
}