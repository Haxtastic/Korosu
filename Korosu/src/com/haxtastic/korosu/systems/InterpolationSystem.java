package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Sprite;

public class InterpolationSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Sprite> sm;
	
	public float alpha;

	@SuppressWarnings("unchecked")
	public InterpolationSystem() {
		super(Aspect.getAspectForAll(Position.class, Sprite.class));
	}

	@Override
	protected void initialize() {
	}
	
	@Override
	protected void begin() {
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	protected void process(Entity e) {
		Position position = pm.get(e);
		Sprite sprite = sm.get(e);
		sprite.x = (position.x*alpha) + (position.px * (1.0f - alpha));
		sprite.y = (position.y*alpha) + (position.py * (1.0f - alpha));
		if((position.r - position.pr) > -6 && (position.r - position.pr) < 6){
			sprite.rotation = (position.r*alpha) + (position.pr * (1.0f - alpha));
		}
	}

	protected void end() {
	}
}
