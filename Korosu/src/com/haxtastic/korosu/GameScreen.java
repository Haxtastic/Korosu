package com.haxtastic.korosu;

import com.artemis.World;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.haxtastic.korosu.systems.AndroidInputSystem;
import com.haxtastic.korosu.systems.AnimationRenderSystem;
import com.haxtastic.korosu.systems.CameraSystem;
import com.haxtastic.korosu.systems.CollisionSystem;
import com.haxtastic.korosu.systems.HudRenderSystem;
import com.haxtastic.korosu.systems.InterpolationSystem;
import com.haxtastic.korosu.systems.LevelSystem;
import com.haxtastic.korosu.systems.DesktopInputSystem;
import com.haxtastic.korosu.systems.SimulationSystem;
import com.haxtastic.korosu.systems.SpriteRenderSystem;
import com.haxtastic.korosu.EntityFactory;

public class GameScreen implements Screen {
	//private Game game;
	private World world;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;
	
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
		
		EntityFactory.cleanWorld(world);

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
		CollisionSystem col = new CollisionSystem();
		world.setSystem(col);
		
		world.initialize();
		world.setDelta(dt);
		
		simulationSystem.simulation.setContactListener(col);
		
		EntityFactory.createBackground(world, simulationSystem).addToWorld();
		EntityFactory.createPlayer(world, simulationSystem, 0, 0f).addToWorld();
		
		debugRenderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
