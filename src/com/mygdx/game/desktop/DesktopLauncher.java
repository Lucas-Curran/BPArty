package com.mygdx.game.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.BPArty;

public class DesktopLauncher {
	
	public static final Logger LOGGER = LogManager.getLogger(DesktopLauncher.class);
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BPArty.WIDTH;
		config.height = BPArty.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new BPArty(), config);
		LOGGER.info("Desktop Launcher successfully ran.");
	}
}
