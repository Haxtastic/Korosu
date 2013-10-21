package com.haxtastic.korosu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Korosu";
		cfg.useGL20 = false;
		cfg.vSyncEnabled = false;
		cfg.width = 1280;
		cfg.height = 768;
		
		new LwjglApplication(new Korosu(), cfg);
	}
}
