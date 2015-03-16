package com.haxtastic.korosu;

import com.artemis.World;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.haxtastic.korosu.components.Ammo;
import com.haxtastic.korosu.components.Weapon;
import com.haxtastic.korosu.systems.AndroidInputSystem;
import com.haxtastic.korosu.systems.BulletSystem;
import com.haxtastic.korosu.systems.CameraSystem;
import com.haxtastic.korosu.systems.CollisionSystem;
import com.haxtastic.korosu.systems.HudRenderSystem;
import com.haxtastic.korosu.systems.InputSystem;
import com.haxtastic.korosu.systems.InterpolationSystem;
import com.haxtastic.korosu.systems.DesktopInputSystem;
import com.haxtastic.korosu.systems.SimulationSystem;
import com.haxtastic.korosu.systems.SpriteRenderSystem;
import com.haxtastic.korosu.EntityFactory;

public class GameScreen implements Screen {
	//private Game game;
	private World world;
	private OrthographicCamera camera;
	//private Box2DDebugRenderer debugRenderer;
	
	private float accum = 0;
	private float dt = 1.0f / 45.0f;
	//private float t = 0;
	
	private SimulationSystem simulationSystem;
	private HudRenderSystem hudRenderSystem;
	private SpriteRenderSystem spriteRenderSystem;
	private InterpolationSystem interpolationSystem;
	private CameraSystem cameraSystem;
	
	
	public GameScreen(Game g, World w, OrthographicCamera c) {
		//game = g;
		world = w;
		camera = c;
		
		//EntityFactory.cleanWorld(world);

		cameraSystem 			= 	world.setSystem(new CameraSystem(camera), true);
		interpolationSystem 	= 	world.setSystem(new InterpolationSystem(), true);
		simulationSystem 		= 	world.setSystem(new SimulationSystem(0.0f, 0.0f));
		spriteRenderSystem 		= 	world.setSystem(new SpriteRenderSystem(camera), true);
		OrthographicCamera hudCam = new OrthographicCamera(Korosu.FRAME_WIDTH, Korosu.FRAME_HEIGHT);
		hudRenderSystem 		= 	world.setSystem(new HudRenderSystem(hudCam), true);
		if(Gdx.app.getType() == ApplicationType.Desktop)
			world.setSystem(new DesktopInputSystem());
		else if(Gdx.app.getType() == ApplicationType.Android)
			world.setSystem(new AndroidInputSystem(hudCam));
		world.setSystem(new InputSystem());
		world.setSystem(new BulletSystem());
		CollisionSystem col = new CollisionSystem();
		world.setSystem(col);
		
		world.initialize();
		world.setDelta(dt);
		
		simulationSystem.simulation.setContactListener(col);
		
		EntityFactory.createBackground(world, simulationSystem).addToWorld();
		EntityFactory.createPlayer(world, simulationSystem, 5f, 5f).addToWorld();
		EntityFactory.createGuy(world, simulationSystem, 2f, 7f, 5f, "guy").addToWorld();
		EntityFactory.createAmmo(world, simulationSystem, 7f, 7f, new Ammo(1, 25, "shell")).addToWorld();
		EntityFactory.createWeapon(world, simulationSystem, 9f, 9f, new Weapon("shotgun")).addToWorld();
		
		//debugRenderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		if(delta > 0.45f)
			delta = 0.45f;
		
		accum += delta;
		while(accum >= dt){
			world.process();
			//t += dt;
			accum -= dt;
		}
		simulationSystem.simulation.clearForces();
		interpolationSystem.alpha = accum/dt;
		interpolationSystem.process();
		
		cameraSystem.process();
		
		spriteRenderSystem.process();
		hudRenderSystem.process();
		//debugRenderer.render(simulationSystem.simulation, camera.combined.scale(Constants.PIXELS_PER_METER_X, Constants.PIXELS_PER_METER_Y, 1.0f));
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
