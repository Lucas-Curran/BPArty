package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class SnakeTronFood {
	
	private int x;
	private int y;
	
	public int getX() { 
		return x; 
	}
	
	public int getY() {
		return y; 
	}
	
	public SnakeTronFood(int boardSize) {
		randomizePosition(boardSize); 
	}
	
	public void randomizePosition(int boardSize) {
		x = MathUtils.random(boardSize-10);
		y = MathUtils.random(boardSize-10);
	}

}
