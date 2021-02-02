package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PlingPong implements Screen{
	
	final BPArty game;
	
	private Texture pongBallTexture;
	private Texture playerPaddleOneTexture;
	private Texture playerPaddleTwoTexture;
	private Texture background;
	private Rectangle playerPaddleOne;
	private Rectangle playerPaddleTwo;
	private Rectangle tempRectangle;
	private Rectangle tempRectangle2;
	
	private int playerOnePoints = 0;
	private int playerTwoPoints = 0;
	private BitmapFont playerOnePointsDisplay;
	private BitmapFont playerTwoPointsDisplay;
	private String playerOneText = "Player One Points : " + playerOnePoints;
	private String playerTwoText = "Player Two Points: " + playerTwoPoints;
	
	private int ballHits = 0;
	
	private Circle pongBall;
	private float xVel;
	private float yVel;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private boolean firstCollision = false;
	private boolean yUp = false;
	private boolean lastPointScoredLeft = true;
	AudioManager audioManager = new AudioManager();
	
	public PlingPong(final BPArty game) {
		this.game = game;
		playerPaddleOneTexture = new Texture(Gdx.files.internal("paddle.png"));
		playerPaddleTwoTexture = new Texture(Gdx.files.internal("paddle.png"));
		pongBallTexture = new Texture(Gdx.files.internal("pongBall.png"));
		background = new Texture(Gdx.files.internal("RetroBackground.jpg"));
		// Background texture is a free for commerical use background from vecteezy.com; Attribution: https://www.vecteezy.com/vector-art/180376-retro-vintage-80s-geometric-style-abstract-background-illustration
		// All other textures in this game were made by the BPArty team
		
		playerOnePointsDisplay = new BitmapFont();
		playerTwoPointsDisplay = new BitmapFont();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		batch = new SpriteBatch();
		pongBall = new Circle();
		pongBall.radius = 4;
		pongBall.x = 400;
		pongBall.y = 190;
		this.xVel = -200f;
		this.yVel = -200f;
		
		playerPaddleOne = new Rectangle();
		playerPaddleOne.x = 30;
		playerPaddleOne.y = 190;
		playerPaddleOne.width = 30;
		playerPaddleOne.height = 64;
		
		playerPaddleTwo = new Rectangle();
		playerPaddleTwo.x = 700;
		playerPaddleTwo.y = 190;
		playerPaddleTwo.width = 50;
		playerPaddleTwo.height = 64;
		
		tempRectangle = new Rectangle();
		tempRectangle2 = new Rectangle();
	}

	private void moveX(float deltaTime) {
		pongBall.x += this.xVel * deltaTime;
	}
	/**
	 * Method moves the ball along x axis
	 */

	private void moveY(float deltaTime) {
		pongBall.y += (this.yVel * deltaTime);
	}
	/**
	 * Method allows ball to move up and down
	 */
	
	private void reverseDirectionX() {
		this.xVel *= -1;
	}
	/**
	 * Reverses ball direction through multiplying it's x velocity
	 */

	private void reverseDirectionY() {
		this.yVel *= -1;
	}
	/**
	 * Reverses ball direction from up to down or down to up by multiplying it's y velocity
	 */
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		audioManager.playChillGame();
		
		batch.begin();
		batch.draw(background, 0, 0);
		batch.draw(playerPaddleOneTexture, playerPaddleOne.x, playerPaddleOne.y);
		batch.draw(playerPaddleTwoTexture, playerPaddleTwo.x, playerPaddleTwo.y);
		batch.draw(pongBallTexture, pongBall.x, pongBall.y);
		
		playerOnePointsDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		playerOnePointsDisplay.draw(batch, playerOneText, 50, 390);
		playerTwoPointsDisplay.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		playerTwoPointsDisplay.draw(batch, playerTwoText, 630, 390);

		batch.end();
		
		if (Gdx.input.isKeyPressed(Input.Keys.W))
			playerPaddleOne.y += 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.S))
			playerPaddleOne.y -= 300 * Gdx.graphics.getDeltaTime();
		if (playerPaddleOne.y < 0)
			playerPaddleOne.y = 0;
		if (playerPaddleOne.y > 340)
			playerPaddleOne.y = 340; //will need to reset the y's to proper places after finished
		
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			playerPaddleTwo.y -= 300 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			playerPaddleTwo.y += 300 * Gdx.graphics.getDeltaTime();
		if (playerPaddleTwo.y < 0)
			playerPaddleTwo.y = 0;
		if (playerPaddleTwo.y > 340)
			playerPaddleTwo.y = 340;
		
		updateBallMovement(delta);
		checkWin();
	}
	
	private void updateBallMovement(float deltaTime) {
        if (!(pongBall == null)) {
            moveX(deltaTime);
            checkPaddleCollision();
            checkWallCollision();
            checkGoalScored();
            if (firstCollision == true) {
                moveY(deltaTime);
                yUp = true;
            }
            updateBallSpeed();
        }
    }
	/**
	 * Starts ball movement, method also implements a variety of different methods that check for collisions.
	 * Initially the method will allow ball to move only on x-axis, after first collision the ball will move at angles.
	 */
	
	private void checkPaddleCollision() {
		if(Intersector.overlaps(pongBall, playerPaddleOne)) {
			tempRectangle.set(playerPaddleOne.x, playerPaddleOne.y + playerPaddleOne.height/2, playerPaddleOne.width, playerPaddleOne.height/2);
			if (Intersector.overlaps(pongBall, tempRectangle)) {
				reverseDirectionX();
				if (yUp == false) {
					reverseDirectionY();
					yUp = true;
				} 
				pongBall.x += 5;
				if (firstCollision == false) {
					firstCollision = true;
				}
			} else {
				reverseDirectionX();
				if (yUp == true) {
					reverseDirectionY();
					yUp = false;
				}
				pongBall.x += 5;
				if (firstCollision == false) {
					firstCollision = true;
				}
			}
			ballHits++;
			audioManager.playBallBounce();
		}
		
		if(Intersector.overlaps(pongBall, playerPaddleTwo)) {
			tempRectangle2.set(playerPaddleTwo.x, playerPaddleTwo.y + playerPaddleTwo.height/2, playerPaddleTwo.width, playerPaddleTwo.height/2);
			if (Intersector.overlaps(pongBall, tempRectangle2)) {
				reverseDirectionX();
				if (yUp == false) {
					reverseDirectionY();
					yUp = true;
				} 
				pongBall.x -= 1;
				if (firstCollision == false) {
					firstCollision = true;
				}
			} else {
				reverseDirectionX();
				if (yUp == true) {
					reverseDirectionY();
					yUp = false;
				}
				pongBall.x -= 1;
				if (firstCollision == false) {
					firstCollision = true;
				}
			}
			ballHits++;
			audioManager.playBallBounce();
		}
	}
	/**
	 * Method checks to see if ball has hit a paddle. Once a ball has hit a paddle the method will create
	 * a temporary rectangle on the top half of the paddle. This will determine if the ball hit the top half or 
	 * lower half of the paddle which determines the position it will bounce back at.
	 */
	
	private void checkWallCollision() {
		if(pongBall.y < 0) {
			reverseDirectionY();
			pongBall.y += 3;
		}
		
		if(pongBall.y > 395) {
			reverseDirectionY();
			pongBall.y -= 3;
		}
	}
	/**
	 * Method checks for wall collisions, walls being defined as top and bottom of screen. If the ball hits
	 * the wall it will reverse its direction.
	 */
	
	private void checkGoalScored() {
		if(pongBall.x < 0) {
			audioManager.playGainPoint();
			firstCollision = false;
			pongBall.x = 400;
			pongBall.y = 190;
			playerTwoPoints++;
			ballHits = 0;
			playerTwoText = "Player Two Points: " + playerTwoPoints;
			lastPointScoredLeft = true;
		}
		
		if(pongBall.x > 800) {
			audioManager.playGainPoint();
			firstCollision = false;
			pongBall.x = 400;
			pongBall.y = 190;
			playerOnePoints++;
			ballHits = 0;
			playerOneText = "Player One Points : " + playerOnePoints;
			lastPointScoredLeft = false;
		}	
	}
	/**
	 * Method checks for a goal, goals being defined as far left and right ends of the screen. If a ball
	 * hits a goal the method will reset the ball to the middle and update point display.
	 */
	
	private void checkWin() {
		if (playerOnePoints == 10) {
			audioManager.stopAll();
			game.setScreen(new IceCreamWin(game));
		} else if (playerTwoPoints == 10) {
			audioManager.stopAll();
			game.setScreen(new PopsicleWin(game));
		}
	}
	/**
	 * Checks for win by comparing player points. First player to reach 10 points will win the game
	 * and the method will display the correct win screen.
	 */
	
	private void updateBallSpeed() {
		if (ballHits == 30) {
			this.xVel *= 1.02;
		} else if (ballHits == 20) {
			this.xVel *= 1.01;
		} else if (ballHits == 10) {
			this.xVel *= 1.008;
		} else if (ballHits == 5) {
			this.xVel *= 1.005;
		} else if (ballHits == 0 && lastPointScoredLeft == true) {
			this.xVel = -200f;
		} else if (ballHits == 0 && lastPointScoredLeft == false) {
			this.xVel = 200f;
		}
	}
	/**
	 * Updates ball speed if no player is scoring, as players rally the more times the ball is passed
	 * back and forth the faster the ball will move by multiplying the x velocity by a small amount.
	 * Method also determines which direction the ball will move in after a player scores, the ball will]
	 * move towards the player who was scored on.
	 */
	
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


	@Override
	public void dispose() {
		playerPaddleOneTexture.dispose();
		playerPaddleTwoTexture.dispose();
		pongBallTexture.dispose();
		background.dispose();
	}

}
