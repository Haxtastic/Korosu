package com.haxtastic.korosu;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Korosu extends Game {
	public static final int FRAME_WIDTH = 1920;
	public static final int FRAME_HEIGHT = 1080;
	
	@Override
	public void create() {
		World world = new World();
		world.setManager(new GroupManager());
		OrthographicCamera camera = new OrthographicCamera(Korosu.FRAME_WIDTH, Korosu.FRAME_HEIGHT);
		Constants.PIXELS_PER_METER_X = Korosu.FRAME_WIDTH/16;
		Constants.PIXELS_PER_METER_Y = Korosu.FRAME_HEIGHT/9;
		camera.position.set(0, camera.viewportHeight/2, 0);
		camera.update();
		setScreen(new GameScreen(this, world, camera));
	}
}
