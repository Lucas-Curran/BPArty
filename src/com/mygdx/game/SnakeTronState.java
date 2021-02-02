package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Queue;

public class SnakeTronState {
	
	private int boardSize = 75;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int yOffset = 0;
	private float mTimer = 0;
	boolean win = false;
	boolean win2 = false;
	
	private int snakeLength = 5;
	private int snakeLength2 = 5;
	
	private Queue<Bodypart> mBody = new Queue<>();
	private Queue<Bodypart2> mBody2 = new Queue<>();
	
	private SnakeTronControls controls = new SnakeTronControls();
	
	private SnakeTronFood mFood = new SnakeTronFood(boardSize);
	
	AudioManager audioManager = new AudioManager();
	
	public SnakeTronState() {
		mBody.addLast(new Bodypart(15,15,boardSize)); //head 
		mBody.addLast(new Bodypart(15,14,boardSize)); 
		mBody.addLast(new Bodypart(15,13,boardSize));
		mBody.addLast(new Bodypart(15,12,boardSize));
		mBody.addLast(new Bodypart(15,11,boardSize));//tail
		
		mBody2.addLast(new Bodypart2(10,15,boardSize)); //head 
		mBody2.addLast(new Bodypart2(10,14,boardSize)); 
		mBody2.addLast(new Bodypart2(10,13,boardSize));
		mBody2.addLast(new Bodypart2(10,12,boardSize));
		mBody2.addLast(new Bodypart2(10,11,boardSize));//tail

	}
	
	public void update(float delta) { // update game logic
		mTimer += delta;
		if (mTimer > 0.1f) {
			mTimer = 0;
			advance();
		}
		
		controls.update();
	}
	
	public void advance() {
		int headX = mBody.first().getX(); 
		int headY = mBody.first().getY();
		
		switch(controls.getDirection()) {
			case 0: //up 
				mBody.addFirst(new Bodypart(headX, headY+1, boardSize));
				break; 
			case 1: //right 
				mBody.addFirst(new Bodypart(headX+1, headY, boardSize));
				break;
			case 2: //down 
				mBody.addFirst(new Bodypart(headX, headY-1, boardSize));
				break;
			case 3: //left 
				mBody.addFirst(new Bodypart(headX-1, headY, boardSize));
				break;
			default://should never happen 
				mBody.addFirst(new Bodypart(headX, headY+1, boardSize));
				break;
		}
		
		if (mBody.first().getX() == mFood.getX() && mBody.first().getY() == mFood.getY()) {
			audioManager.playCoin();
		    snakeLength++;
		    mFood.randomizePosition(boardSize);
		}
		if (mBody.size - 1 == snakeLength) {
		    mBody.removeLast();
		}
		
		if ((mBody.first().getX() == 74) || (mBody.first().getX() == 0)) {
			 win2 = true;
		}
		
		if ((mBody.first().getY() == 74) || (mBody.first().getY() == 0)) {
			win2 = true;
		}
		
//		System.out.println(Gdx.graphics.getWidth());
		
		int headX2 = mBody2.first().getX2(); 
		int headY2 = mBody2.first().getY2();
		switch(controls.getDirection2()) {
			case 0: //up 
				mBody2.addFirst(new Bodypart2(headX2, headY2+1, boardSize));
				break; 
			case 1: //right 
				mBody2.addFirst(new Bodypart2(headX2+1, headY2, boardSize));
				break;
			case 2: //down 
				mBody2.addFirst(new Bodypart2(headX2, headY2-1, boardSize));
				break;
			case 3: //left 
				mBody2.addFirst(new Bodypart2(headX2-1, headY2, boardSize));
				break;
			default://should never happen 
				mBody2.addFirst(new Bodypart2(headX2, headY2+1, boardSize));
				break;
		}
		
		if (mBody2.first().getX2() == mFood.getX() && mBody2.first().getY2() == mFood.getY()) {
		    snakeLength2++;
		    audioManager.playCoin();
		    mFood.randomizePosition(boardSize);
		}
		if (mBody2.size - 1 == snakeLength2) {
		    mBody2.removeLast();
		}
		
		if ((mBody2.first().getX2() == 74) || (mBody2.first().getX2() == 0)) {
			 win = true;
		}
		
		if ((mBody2.first().getY2() == 74) || (mBody2.first().getY2() == 0)) {
			 win = true;
		}
		
		for (int i = 1; i < mBody.size; i++) {
		    if (mBody.get(i).getX() == mBody.first().getX()
		            && mBody.get(i).getY() == mBody.first().getY()) {
		    	win2 = true;
		    }
		    
		    if (mBody.get(i).getX() == mBody2.first().getX2()
		            && mBody.get(i).getY() == mBody2.first().getY2()) {
		    	win2 = true;
		    }
		}
		
		while (mBody.size - 1 >= snakeLength) {
		    mBody.removeLast();
		}
		
		for (int i = 1; i < mBody2.size; i++) {
		    if (mBody2.get(i).getX2() == mBody2.first().getX2()
		            && mBody2.get(i).getY2() == mBody2.first().getY2()) {
		        win = true;
		    }
		    
		    if (mBody2.get(i).getX2() == mBody.first().getX()
		            && mBody2.get(i).getY2() == mBody.first().getY()) {
		        win = true;
		    }
		}
		
		while (mBody2.size - 1 >= snakeLength2) {
		    mBody2.removeLast();
		}
		
	}
	
	public void draw(int width, int height, OrthographicCamera camera) {
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//rectangle drawing happens here
		
		shapeRenderer.setColor(0,0,0,1);
		shapeRenderer.rect(0, 0, width, width);
		
		shapeRenderer.rect(500, 500, 5, 5);
		
		shapeRenderer.setColor(Color.BROWN);
		
		float scaleSnake = width/boardSize;
		for (Bodypart bp : mBody) {
			shapeRenderer.rect(bp.getX()*scaleSnake, bp.getY()*scaleSnake + yOffset, scaleSnake, scaleSnake);

		}
		
		shapeRenderer.setColor(0, 0, 1, 0);
		
		for (Bodypart2 bp : mBody2) {
			shapeRenderer.rect(bp.getX2()*scaleSnake, bp.getY2()*scaleSnake + yOffset, scaleSnake, scaleSnake);

		}
		
		shapeRenderer.setColor(0,1,0,0);
		
		shapeRenderer.rect(mFood.getX() * scaleSnake, mFood.getY()*scaleSnake + yOffset, scaleSnake, scaleSnake);
		
		shapeRenderer.end();
		
	}

}
