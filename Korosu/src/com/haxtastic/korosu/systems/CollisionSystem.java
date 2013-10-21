package com.haxtastic.korosu.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.AnimationSprite;
import com.haxtastic.korosu.components.Bullet;
import com.haxtastic.korosu.components.Collision;
import com.haxtastic.korosu.components.Enemy;
import com.haxtastic.korosu.components.Health;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Weapon;

public class CollisionSystem extends EntityProcessingSystem implements ContactListener {
	@Mapper ComponentMapper<Actor> am;
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<AnimationSprite> sm;
	@Mapper ComponentMapper<Position> posm;
	@Mapper ComponentMapper<Bullet> bm;
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Health> hm;
	private float time = 0;
	private List<Collision> collision;

	@SuppressWarnings("unchecked")
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Actor.class));
		collision = new ArrayList<Collision>();
	}
	
	public void process(Entity e){
		Body actor = am.get(e).actor;
		if(actor.getUserData() == null)
			return;
		
		if(collision.contains(new Collision(actor, actor))){
			//if(bm.has(e))
			//	handleBullet(e);
			if(em.has(e))
				handleEnemy(e);
		}
		/*ListIterator<Collision> iterColl = collision.listIterator();
		while(iterColl.hasNext()){
			Collision contact = iterColl.next();
			System.out.println(contact.a);
			System.out.println(contact.b);
			if(actor == contact.a || actor == contact.b){
				if(bm.has(e))
					handleBullet(e);
				if(em.has(e))
					handleEnemy(e);
			}
		}*/
		//collision.clear();
		
		/*
		Player player = pm.get(e);
		AnimationSprite sprite = sm.get(e);
		Position pos = posm.get(e);
		time += world.delta;
		Integer collision = (Integer)actor.getUserData();
		try{
			if(player.alive && ((collision & 2) != 0 || (collision & 4) != 0)){
				player.alive = false;
				player.started = false;
				player.resetTime = time;
				sprite.active = false;
				EntityFactory.createExplosion(world, pos.x + (sprite.scaleX/2), pos.y + (sprite.scaleY/2)).addToWorld();
			}
		} catch(NullPointerException ex){
			System.out.println(ex);
		}*/
	}
	
	public void handleEnemy(Entity e) {
		Actor actor = am.get(e);
		Health hp = hm.get(e);
		System.out.println(actor.name);
		Integer col = (Integer)actor.actor.getUserData();
		if((col & Constants.Layers.BULLET) != 0) {
			System.out.println(hp.health);
			hp.health = hp.health - 1;
			if(hp.health < 0){
				actor.w.simulation.destroyBody(actor.actor);
	            actor.actor.setUserData(null);
	            actor = null;
	            e.deleteFromWorld();
			}
		}
	}
	
	private void handleBullet(Entity e) {
		Actor actor = am.get(e);
		Integer col = (Integer)actor.actor.getUserData();
		if((col & Constants.Layers.ENEMY) != 0) {
			actor.w.simulation.destroyBody(actor.actor);
            actor.actor.setUserData(null);
            actor = null;
            e.deleteFromWorld();
            Contact contact = new Contact(actor.w.simulation, )
		}
	}

	@Override
	public void beginContact(Contact contact){
		Fixture contactFixtureA = contact.getFixtureA();
		Fixture contactFixtureB = contact.getFixtureB();
		
		if((Integer)contactFixtureB.getUserData() == Constants.Layers.BULLET){
			Object ob = (Integer)contactFixtureA.getBody().getUserData() | (Integer)contactFixtureB.getUserData();
			contactFixtureA.getBody().setUserData(ob);
			ob = (Integer)contactFixtureB.getBody().getUserData() | (Integer)contactFixtureA.getUserData();
			contactFixtureB.getBody().setUserData(ob);
			
			Collision a = new Collision(contactFixtureA.getBody(), contactFixtureB.getBody());
			if(!collision.contains(a)){
				System.out.println("Adding.");
				collision.add(a);
			}
		}
	}
	
	public void endContact(Contact contact){
		Fixture contactFixtureA = contact.getFixtureA();
		Fixture contactFixtureB = contact.getFixtureB();
		
		Object ob = (Integer)contactFixtureA.getBody().getUserData() & ~(Integer)contactFixtureB.getUserData();
		contactFixtureA.getBody().setUserData(ob);

		ob = (Integer)contactFixtureB.getBody().getUserData() & ~(Integer)contactFixtureA.getUserData();
		contactFixtureB.getBody().setUserData(ob);
		Collision a = new Collision(contactFixtureA.getBody(), contactFixtureB.getBody());
		System.out.println("HEJSAN IGEN");
		if(collision.contains(a)){
			System.out.println("Removing.");
			collision.remove(a);
		}
	}
	
	public void preSolve(Contact contact, Manifold manifold){
	}
	
	public void postSolve(Contact contact, ContactImpulse impulse){
	}
}
