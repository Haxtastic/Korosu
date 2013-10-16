package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Sprite;

public class CameraSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Sprite> sm;
	
	OrthographicCamera camera;
	float cameraOffset;
	
	@SuppressWarnings("unchecked")
	public CameraSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Player.class));
		this.camera = camera;
		cameraOffset = 6f;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void process(Entity e) {
		Position pos = pm.get(e);
		Sprite sprite = sm.get(e);
		camera.translate(((sprite.x) * Constants.PIXELS_PER_METER_X) - camera.position.x, ((sprite.y) * Constants.PIXELS_PER_METER_Y) - camera.position.y);
		camera.update();
	}

}
