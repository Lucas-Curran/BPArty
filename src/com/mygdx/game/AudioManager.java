package com.mygdx.game;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	
		public static final Logger LOGGER = LogManager.getLogger(Board.class);
	
		SQLiteConnect sqlite = new SQLiteConnect();
		private HashMap<String, Music> music;
		private HashMap<String, Sound> sound;
		Music boardMusic;
		Music menuMusic;
		Music introMusic;
		Music chillGameMusic;
		Music intenseGameMusic;
		Sound buttonPress, buttonPress2, coinGet, pointCollect, loseLife, balloonPop, gainPoint, ballBounce;
		float volume;
		
		
	/**
	 * Creates hashmap of music objects
	 */
	public AudioManager() {
		music = new HashMap<String,Music>();
		boardMusic = Gdx.audio.newMusic(Gdx.files.internal("BPArty_Board.wav"));
		intenseGameMusic = Gdx.audio.newMusic(Gdx.files.internal("BPArty Major Minigame.wav"));
		chillGameMusic = Gdx.audio.newMusic(Gdx.files.internal("BPArty_Chill_Minigame.wav"));
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("BPArty_Main_Loop.wav"));
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("BPArty_Intro.wav"));
		music.put("1", boardMusic);
		music.put("2", intenseGameMusic);
		music.put("3", chillGameMusic);
		music.put("4", menuMusic);
		music.put("5", introMusic);
		
		volume = (float) sqlite.volume() / 1000;
		
		buttonPress = Gdx.audio.newSound(Gdx.files.internal("buttonClick.mp3"));
		buttonPress2 = Gdx.audio.newSound(Gdx.files.internal("buttonClick2.mp3"));
		coinGet = Gdx.audio.newSound(Gdx.files.internal("coinGet.mp3"));
		pointCollect = Gdx.audio.newSound(Gdx.files.internal("pointPickup.wav"));
		loseLife = Gdx.audio.newSound(Gdx.files.internal("loseLife.mp3"));
		balloonPop = Gdx.audio.newSound(Gdx.files.internal("balloonPop.mp3"));
		gainPoint = Gdx.audio.newSound(Gdx.files.internal("gainPoint.mp3"));
		ballBounce = Gdx.audio.newSound(Gdx.files.internal("ballBounce.mp3"));
		
		sound = new HashMap<String, Sound>();
		sound.put("1", buttonPress);
		sound.put("2", buttonPress2);
		sound.put("3", coinGet);
		sound.put("4", balloonPop);
		sound.put("5", gainPoint);
		sound.put("6", ballBounce);
		sound.put("7", loseLife);
		sound.put("8", pointCollect);

		

	}
		
	/**
	 * Get specific music object from hashmap
	 * @param key
	 * @return
	 */
	public Music getMusic(String key) {
		return music.get(key);
	}
		
	/**
	 * Update music volume
	 */
	public void updateAll() {
		volume = (float) sqlite.volume() / 1000;
		music.get("1").setVolume(volume);
		music.get("2").setVolume(volume);
		music.get("3").setVolume(volume);
		music.get("4").setVolume(volume);
		music.get("5").setVolume(volume);
	}
	
	/**
	 * Plays board object
	 */
	public void playBoard() {
		if (music.get("1").isPlaying()) {
			//Nothing
		} else {
			music.get("1").setVolume(volume);
			music.get("1").setLooping(true);
			music.get("1").play();
		}
	}
	
	/**
	 * Plays intro object
	 */
	public void playIntro() {
		music.get("5").setVolume(volume);
		music.get("5").play();
	}
	
	/**
	 * Plays menu object
	 */
	public void playMenu() {
		if (music.get("4").isPlaying()) {
			//Do nothing
		} else {
			music.get("4").setVolume(volume);
			music.get("4").setLooping(true);
			music.get("4").play();
		}
	}
	
	/**
	 * Plays chill game object
	 */
	public void playChillGame() {

		music.get("3").setVolume(volume);
		music.get("3").setLooping(true);
		music.get("3").play();
	}
	
	/**
	 * plays intense game object
	 */
	public void playIntenseGame() {
		music.get("2").setVolume(volume);
		music.get("2").setLooping(true);
		music.get("2").play();
	}
	
	/**
	 * Stops all music from playing
	 */
	public void stopAll() {
		music.get("1").stop();
		music.get("2").stop();
		music.get("3").stop();
		music.get("4").stop();
		music.get("5").stop();
	}
	
	/**
	 * Plays button press sound
	 */
	public void playClick() {
		sound.get("1").play();
	}
	
	/**
	 * Plays button press sound #2
	 */
	public void playClick2() {
		sound.get("2").play();
	}
	/**
	 * Play coin sound effect
	 */
	public void playCoin() {
		sound.get("3").play();
	}
	
	/**
	 * Plays a sound when a life is lost
	 */
	public void playBalloonPop() {
		sound.get("4").play(0.1f);
	}
	
	/**
	 * Plays a sound for gaining a point
	 */
	public void playGainPoint() {
		sound.get("5").play(0.1f);
	}
	
	/**
	 * Plays a a sound for a ball bounce
	 */
	public void playBallBounce() {
		sound.get("6").play(0.1f);
	}
	
	/**
	 * Plays a sound when a life is lost
	 */
	public void playLoseLife() {
		sound.get("7").play(0.1f);
	}
	
	/**
	 * Plays a point collection sound 
	 */
	public void playPointCollect() {
		sound.get("8").play(0.1f);
	}
}
