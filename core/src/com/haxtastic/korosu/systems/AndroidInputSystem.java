package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.TouchpadComp;

public class AndroidInputSystem extends EntityProcessingSystem implements InputProcessor {
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Input> im;
	
	private boolean touched = false;
	private int tx, ty = 0;
	public float time, pressTime = 0;
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
		stage.getViewport().setCamera(camera);
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
		
		System.out.println("x: "  + tx/Constants.PIXELS_PER_METER_X + " | y: " + ty/Constants.PIXELS_PER_METER_Y);
		if((tx >= (14.5f * Constants.PIXELS_PER_METER_X) && tx <= (19.5f * Constants.PIXELS_PER_METER_X) && ty >= (2.5f * Constants.PIXELS_PER_METER_Y) && ty <= (5f * Constants.PIXELS_PER_METER_Y)) && touched)
			input.cycle = true;
			
		
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
		tx = x;ty = y;touched = true;
		stage.touchDown(x, y, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		stage.touchUp(x, y, pointer, button);
		touched = false;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		tx = x; ty = y;
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
