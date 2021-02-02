package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class RoundSwitchScreen implements Screen{
	
	final BPArty game;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private String roundSwitch = "Round 1 Over";
	private String roundSwitch2 = "Player 2 Press Space to Begin";	
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	
	private BitmapFont roundSwitchDisplay;
	
	public RoundSwitchScreen(final BPArty game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		batch = new SpriteBatch();
        
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Vezla.ttf"));
        fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 24;
        fontParameter.borderWidth = 5;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.WHITE;
        roundSwitchDisplay = fontGenerator.generateFont(fontParameter);
//        parameter.characters = "Test";
//        BitmapFont font12 = generator.generateFont(parameter);
        fontGenerator.dispose();
        
	}
	
//	public int roundOneData {
//		roundOneEnded = true;
//	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		roundSwitchDisplay.draw(batch, roundSwitch, 250, 300);
        roundSwitchDisplay.draw(batch, roundSwitch2, 100, 250);
		batch.end();
		
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			game.setScreen(new CatchTheCream(game));
		}

	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
	
}
