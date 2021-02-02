package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


public class BPArty extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	
	Stage stage;
	TextButtonStyle textButtonStyle;
	Skin skin;
	TextureAtlas textureAtlas;
	SliderStyle sliderStyle;
	LabelStyle labelStyle;



	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new IntroScreen(this));
	}
	
	public TextButtonStyle ButtonStyles(String upStyle, String overStyle) {
		font = new BitmapFont();
		skin = new Skin();
		textureAtlas = new TextureAtlas("buttonMoreData.txt");
		skin.addRegions(textureAtlas);
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.up = skin.getDrawable(upStyle);
		textButtonStyle.over = skin.getDrawable(overStyle);
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		return textButtonStyle;
	}
	
	public SliderStyle sliderStyles() {
		skin = new Skin();
		textureAtlas = new TextureAtlas("defaultAtlas.txt");
		skin.addRegions(textureAtlas);
		sliderStyle = new SliderStyle();
		sliderStyle.knob = skin.getDrawable("default-slider-knob");
		sliderStyle.background = skin.getDrawable("default-slider");
		return sliderStyle;
	}
	

	
	public void PauseMenu() {
		
	}
	
	public void render() {
		super.render();
	}
	
	public void disposer() {
		batch.dispose();
		font.dispose();
	}
}
