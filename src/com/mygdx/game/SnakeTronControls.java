package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SnakeTronControls {

	private int currentDirection = 0; // 0, 1, 2, 3, => Up, Right, Down, Left
	private int nextDirection = 0;
	
	private int currentDirection2 = 0; // 0, 1, 2, 3, => Up, Right, Down, Left
	private int nextDirection2 = 0;
	
	public int getDirection() {
		currentDirection = nextDirection;
		return nextDirection;
	}
	
	public int getDirection2() {
		currentDirection2 = nextDirection2;
		return nextDirection2;

	}
	
	public void update() {
		
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && currentDirection != 2) nextDirection = 0;

		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && currentDirection != 3) nextDirection = 1;

		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && currentDirection != 0) nextDirection = 2;

		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && currentDirection != 1) nextDirection =3;
		
		if(Gdx.input.isKeyPressed(Input.Keys.W) && currentDirection2 != 2) nextDirection2 = 0;

		else if (Gdx.input.isKeyPressed(Input.Keys.D) && currentDirection2 != 3) nextDirection2 = 1;

		else if (Gdx.input.isKeyPressed(Input.Keys.S) && currentDirection2 != 0) nextDirection2 = 2;

		else if (Gdx.input.isKeyPressed(Input.Keys.A) && currentDirection2 != 1) nextDirection2 =3;
	}
}
