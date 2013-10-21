package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.Korosu;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.TouchpadComp;
import com.haxtastic.korosu.components.Velocity;

public class AndroidInputSystem extends EntityProcessingSystem implements InputProcessor {
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Input> im;
	
	private boolean jump, jumpPress, startPress, restartPress = false;
	private boolean up, down, left, right;
	private boolean shoot;
	public float time, pressTime = 0;
	private float cooldown = 0.25f;
	private float cdcount = 0.0f;
	private boolean cycle = false;
	private Touchpad touchpadLeft, touchpadRight;
	private Stage stage;
	private OrthographicCamera camera;
	
	@SuppressWarnings("unchecked")
	public AndroidInputSystem(OrthographicCamera c) {
		super(Aspect.getAspectForAll(Player.class, Input.class));
		time = 0;
		camera = c;
	}
	
	@Override
	protected void initialize() {
		Gdx.input.setInputProcessor(this);
		stage = new Stage();
		stage.setCamera(camera);
		Skin skin = new Skin(Gdx.files.internal("skins/touchpad.json"));
		touchpadLeft = new Touchpad(15, skin);
		touchpadLeft.setBounds((2f * Constants.PIXELS_PER_METER_X), (1.5f * Constants.PIXELS_PER_METER_Y), (2.5f * Constants.PIXELS_PER_METER_X), (2.5f * Constants.PIXELS_PER_METER_Y));
		stage.addActor(touchpadLeft);
		touchpadRight = new Touchpad(15, skin);
		touchpadRight.setBounds((15f * Constants.PIXELS_PER_METER_X), (1.5f * Constants.PIXELS_PER_METER_Y), (2.5f * Constants.PIXELS_PER_METER_X), (2.5f * Constants.PIXELS_PER_METER_Y));
		stage.addActor(touchpadRight);
		Entity e = world.createEntity();
		TouchpadComp touch = new TouchpadComp();
		touch.pad = stage;
		e.addComponent(touch);
		world.getManager(GroupManager.class).add(e, Constants.Groups.TOUCHPAD);
		e.addToWorld();
	}

	@Override
	protected void process(Entity e) {
		stage.act(world.delta);
		Player player = pm.get(e);
		Input input = im.get(e);
		time += world.delta;
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		if(cycle){
			input.cycle = true;
			cycle = false;
		}
			
		
		float angle = (float) Math.toDegrees(Math.atan2(touchpadLeft.getKnobPercentY(), touchpadLeft.getKnobPercentX()));
    	if(angle < 0) {
    		angle += 360;
        }
    	
    	if(touchpadRight.getKnobPercentY() != 0 || touchpadRight.getKnobPercentX() != 0){
    		angle = (float) Math.toDegrees(Math.atan2(touchpadRight.getKnobPercentY(), touchpadRight.getKnobPercentX()));
        	if(angle < 0) {
        		angle += 360;
            }
    	}
    	
    	if(touchpadLeft.isTouched() || touchpadRight.isTouched()){
    		input.angle = angle * MathUtils.degreesToRadians;
    	}
    	
    	if(touchpadRight.isTouched())
    		input.shoot = true;
    	input.velocity.set(touchpadLeft.getKnobPercentX()*player.speed, touchpadLeft.getKnobPercentY()*player.speed);
	}

	@Override
	public boolean keyDown(int keycode) {
		stage.keyDown(keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		stage.keyUp(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		stage.keyTyped(character);
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		stage.touchDown(x, y, pointer, button);
		if(x >= (16 * Constants.PIXELS_PER_METER_X) && x <= (20 * Constants.PIXELS_PER_METER_X) && y >= (4 * Constants.PIXELS_PER_METER_Y) && y <= (8 * Constants.PIXELS_PER_METER_Y))
			cycle = true;
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		stage.touchUp(x, y, pointer, button);
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		stage.touchDragged(x, y, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		stage.mouseMoved(x, y);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		stage.scrolled(amount);
		return false;
	}

}
