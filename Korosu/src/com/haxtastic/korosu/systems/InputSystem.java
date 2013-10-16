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

public class InputSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<Input> im;
	
	private boolean up, down, left, right;
	private boolean shoot;
	public float time, pressTime = 0;
	private float cooldown = 0.25f;
	private float speed = 5;
	
	@SuppressWarnings("unchecked")
	public InputSystem() {
		super(Aspect.getAspectForAll(Player.class, Input.class));
		time = 0;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void process(Entity e) {
		Player player = pm.get(e);
		Input input = im.get(e);
		time += world.delta;
	}

}
