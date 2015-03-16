package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Bullet;
import com.haxtastic.korosu.components.Position;

public class BulletSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Bullet> bm;
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Actor> am;

	
	@SuppressWarnings("unchecked")
	public BulletSystem() {
		super(Aspect.getAspectForAll(Bullet.class));
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void process(Entity e) {
		Bullet bullet = bm.get(e);
		Position pos = pm.get(e);
		Actor actor = am.get(e);
		
		float dist = bullet.origin.dst(new Vector2(pos.x, pos.y));
		
		if(dist > bullet.range){
            actor.w.simulation.destroyBody(actor.actor);
            actor.actor.setUserData(null);
            actor = null;
            e.deleteFromWorld();
		}
		
	}

}
