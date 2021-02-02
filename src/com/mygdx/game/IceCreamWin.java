package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class IceCreamWin implements Screen {
	
	private int width = 1000;
	private int height = 1000; 
	
	private OrthographicCamera camera = new OrthographicCamera(width, height); 
	SQLiteConnect sqlite = new SQLiteConnect();
	
	boolean triple = false, versus = false;
	
	Texture victoryScreen;
	
	final BPArty game;
	
public IceCreamWin(final BPArty game) {
	this.game = game;
	camera.setToOrtho(false, width, height);
		
	victoryScreen = new Texture("IceCreamWin.png");
	Board board = new Board(game);
	
	for (int i = 0; i < board.versusX.length; i++ ) {
		if ((board.spaceX == board.versusX[i] && board.spaceY == board.versusY[i])) {
			board.iceCreamPoints+=10;
			versus = true;
			
		}
	}
	
	for (int i = 0; i < board.triplePointsX.length; i++ ) {
		if ((board.spaceX == board.triplePointsX[i] && board.spaceY == board.triplePointsY[i])) {
			board.iceCreamPoints+=15;	
			triple = true;
		}
	}
	
	if (versus == false && triple == false) {
		board.iceCreamPoints+=5;	
	}
	
	sqlite.updateAll(board.sliderValue, board.turn, board.playing, board.tutorial, board.spaceX, board.spaceY, board.iceCreamPoints, board.popsiclePoints);
	
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(victoryScreen, -5, -5, 1000, 1000);
		game.batch.end();
		
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			game.setScreen(new Board(game));			
	} 
		
//		System.out.println(Gdx.input.getX());
		
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
		game.batch.dispose();
		
	}

}
