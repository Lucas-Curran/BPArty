package com.mygdx.game;

public class Bodypart2 {

	private int x2;
	private int y2;
	
	public Bodypart2(int x, int y, int boardSize) {
		
		this.x2 = x % boardSize;
		if (this.x2 < 0) this.x2 += boardSize;
		this.y2 = y % boardSize; 
		if (this.y2<0) this.y2 += boardSize;
		
	}
	
	public int getX2() {
		return x2; 
	}
	public int getY2() { 
		return y2; 
	}
}
