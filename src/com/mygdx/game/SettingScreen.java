package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;


public class SettingScreen implements Screen {
	final BPArty game;
	SQLiteConnect sqlite = new SQLiteConnect();
	
	OrthographicCamera camera;
	Texture background;
	Stage stage;
	TextButton creditsBtn, returnBtn;
	Slider volumeSlider;
	Container<Slider> container;
	BitmapFont font;
	Label sliderLabel, volumeLabel;
	Table table;
	Skin skin;
	int sliderValue = sqlite.volume();
	
	AudioManager audioManager = new AudioManager();
	
	/**
	 * Sets camera and game
	 * @param game
	 */
	public SettingScreen(final BPArty game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, BPArty.WIDTH/2, BPArty.HEIGHT/2);
		background = new Texture("SettingsMenu.png");
		
	}
	
	/**
	 * Creates stage and actors
	 */
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		volumeSlider = new Slider(0, 100, 1, false, game.sliderStyles());
		container = new Container<Slider>(volumeSlider);
		container.setTransform(true);
		container.setScale(6f);
		
		font = new BitmapFont();
		sliderLabel = new Label(String.valueOf(sliderValue), new Label.LabelStyle(font, Color.ROYAL));
		volumeLabel = new Label("Volume", new Label.LabelStyle(font, Color.ROYAL));
		volumeLabel.setFontScale(4.5f);
		sliderLabel.setFontScale(4);
		volumeSlider.setValue(sliderValue);
		
		creditsBtn = new TextButton("Credits", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
		returnBtn = new TextButton("Return", game.ButtonStyles("MenuRectangle", "MenuRectangle"));
		creditsBtn.getLabel().setAlignment(Align.left);
		creditsBtn.getLabelCell().padLeft(20);
		creditsBtn.getLabel().setFontScale(2,2);
		creditsBtn.setTransform(true);
		creditsBtn.scaleBy(1);
		returnBtn.getLabel().setAlignment(Align.left);
		returnBtn.getLabelCell().padLeft(20);
		returnBtn.getLabel().setFontScale(2,2);
		returnBtn.setTransform(true);
		returnBtn.scaleBy(1);
		
		table = new Table(skin);
		table.setBounds(0, 0, BPArty.WIDTH, BPArty.HEIGHT);
		table.center().top().pad(120);
		table.add(volumeLabel).colspan(2);
		table.row();
		table.add(container).padTop(100).padRight(700).colspan(2);
		table.row();
		table.add(sliderLabel).colspan(2);
		table.row().padTop(100);
		table.add(returnBtn);
		table.add(creditsBtn).padRight(60);
		
//		table.debug();
		stage.addActor(table);
	}
	
	/**
	 * Runs stage and actors
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		audioManager.playMenu();
		stage.getBatch().begin();
		stage.getBatch().draw(background, 0, 0, BPArty.WIDTH, BPArty.HEIGHT);
		stage.getBatch().end();
		
		if (creditsBtn.isPressed()) {
			audioManager.playClick();
			game.setScreen(new TransitionScreen(new CreditScreen(game), game));
		}
		
		if (returnBtn.isPressed()) {
			audioManager.playClick();
			audioManager.stopAll();
			game.setScreen(new TransitionScreen(new MainMenuScreen(game), game));
		}
		
		if (volumeSlider.isDragging()) {
			sliderValue = (int) volumeSlider.getValue();
			sliderLabel.setText(sliderValue);
			sqlite.updateVolume(sliderValue);
			audioManager.updateAll();
		}
		
		stage.act(delta);
		stage.draw();
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
}
