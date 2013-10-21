package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.Korosu;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Inventory;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Velocity;

public class DesktopInputSystem extends EntityProcessingSystem implements InputProcessor {
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Input> im;
	@Mapper ComponentMapper<Inventory> ivm;
	
	private boolean[] keys = new boolean[259];
	private int mLeft = 256, mRight = 257, mMiddle = 258;
	private int up = Keys.W, down = Keys.S, left = Keys.A, right = Keys.D;
	private int shoot = Keys.SPACE;
	private float cooldown = 0.25f;
	private float cdcount = 0.0f;
	private Vector2 mouse;
	public float time, pressTime = 0;
	
	@SuppressWarnings("unchecked")
	public DesktopInputSystem() {
		super(Aspect.getAspectForAll(Player.class, Input.class));
		//this.camera = camera;
		time = 0;
		mouse = new Vector2(0.0f, 0.0f);
		//prevTime = System.nanoTime();
	}
	
	@Override
	protected void initialize() {
		Gdx.input.setInputProcessor(this);
		for(int i = 0; i > 258; i++)
			keys[i] = false;
	}

	@Override
	protected void process(Entity e) {
		Player player = pm.get(e);
		Input input = im.get(e);
		float speed = player.speed;
		time += world.delta;
		cdcount += world.delta;
		
		if((keys[up] || keys[down]) && (keys[right] || keys[left]))
			speed = ((speed/3)*2);
		if(keys[up])
			input.velocity.set(input.velocity.x, speed);
		if(keys[down])
			input.velocity.set(input.velocity.x, -speed);
		if(keys[right])
			input.velocity.set(speed, input.velocity.y);
		if(keys[left])
			input.velocity.set(-speed, input.velocity.y);
		
		if(!keys[left] && !keys[right])
			input.velocity.set(0f, input.velocity.y);
		if(!keys[up] && !keys[down])
			input.velocity.set(input.velocity.x, 0f);
		
		float angle = (float)Math.toDegrees(Math.atan2(-(mouse.y - (Gdx.graphics.getHeight()/2)), mouse.x - (Gdx.graphics.getWidth()/2)));
    	if(angle < 0)
    		angle += 360;
    	input.angle = angle * MathUtils.degreesToRadians;
    	
    	if(keys[mLeft] == true) {
    		input.shoot = true;
    	}
    	
    	
    	if(keys[Keys.H]){
    		input.cycle = true;
    	}
	}

	@Override
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys[keycode] = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		keys[button + 256] = true;
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		keys[button + 256] = false;
		return true;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		mouse.set(x, y);
		return false;
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		mouse.set(x, y);
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
}
