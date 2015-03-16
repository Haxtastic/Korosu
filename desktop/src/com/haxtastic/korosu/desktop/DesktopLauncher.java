package com.haxtastic.korosu.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.haxtastic.korosu.Korosu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Korosu";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Korosu(), config);
	}
}
