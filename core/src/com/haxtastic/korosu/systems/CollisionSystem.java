package com.haxtastic.korosu.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Ammo;
import com.haxtastic.korosu.components.AnimationSprite;
import com.haxtastic.korosu.components.Bullet;
import com.haxtastic.korosu.components.Collision;
import com.haxtastic.korosu.components.Enemy;
import com.haxtastic.korosu.components.Health;
import com.haxtastic.korosu.components.Inventory;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Velocity;
import com.haxtastic.korosu.components.Weapon;

public class CollisionSystem extends EntityProcessingSystem implements ContactListener {
	@Mapper ComponentMapper<Actor> am;
	@Mapper ComponentMapper<Player> pm;
	@Mapper ComponentMapper<AnimationSprite> sm;
	@Mapper ComponentMapper<Position> posm;
	@Mapper ComponentMapper<Bullet> bm;
	@Mapper ComponentMapper<Enemy> em;
	@Mapper ComponentMapper<Health> hm;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Inventory> invm;
	@Mapper ComponentMapper<Ammo> amm;
	@Mapper ComponentMapper<Weapon> wm;
	private List<Collision> collision;

	@SuppressWarnings("unchecked")
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Actor.class));
		collision = new ArrayList<Collision>();
	}
	
	public void process(Entity e){		
		Collision contact = null;
		ListIterator<Collision> iterColl = collision.listIterator();
		if(iterColl.hasNext()){
			contact = iterColl.next();
			if(contact.has(Bullet.class)){
				handleBullet(contact);
			}else if(contact.has(Player.class)){
				handlePlayer(contact);
			}
		}
		collision.remove(contact);
	}
	
	private void handlePlayer(Collision c) {
		Entity a = c.a;
		Entity b = c.b;
		if(c.has(b, Player.class)){		//Make sure player is Entity a
			a = c.b;
			b = c.a;
		}
		
		if(c.has(Ammo.class)){		//Ammo collide
			Actor aBody = am.get(b);
			Inventory inv = invm.get(a);
			Ammo ammo = amm.get(b);
			inv.addAmmo(ammo);
			aBody.w.destroyBody(aBody.actor);
			aBody = null;
			b.deleteFromWorld();
			System.out.println("Picked up " + ammo.bullet + ". You know have " + ammo.amount);
		}
		
		if(c.has(Weapon.class)){	//Weapon collide
			Actor aBody = am.get(b);
			Inventory inv = invm.get(a);
			Weapon weapon = wm.get(b);
			inv.addWeapon(weapon);
			aBody.w.destroyBody(aBody.actor);
			aBody = null;
			b.deleteFromWorld();
			System.out.println("Picked up " + weapon.type + ". You know have " + weapon.amount);
		}
	}
	
	private void handleBullet(Collision c) {
		Entity a = c.a;
		Entity b = c.b;
		if(c.has(b, Bullet.class)){		//Make sure bullet is Entity a
			a = c.b;
			b = c.a;
		}
		
		if(c.has(Enemy.class)){		//Enemy collide
			Actor bullet = am.get(a);
			Actor enemy = am.get(b);
			bullet.w.destroyBody(bullet.actor);
			bullet = null;
			a.deleteFromWorld();
			
			Health hp = hm.get(b);
			float damage = bm.get(a).damage;
			hp.health-=damage;
			if(hp.health <= 0){
				enemy.w.destroyBody(enemy.actor);
				enemy = null;
				b.deleteFromWorld();
			}
		}
	}

	@Override
	public void beginContact(Contact contact){
		Fixture contactFixtureA = contact.getFixtureA();
		Fixture contactFixtureB = contact.getFixtureB();
		
		//Object ob = (Integer)contactFixtureA.getBody().getUserData() | (Integer)contactFixtureB.getUserData();
		//contactFixtureA.getBody().setUserData(ob);
		
		//ob = (Integer)contactFixtureB.getBody().getUserData() | (Integer)contactFixtureA.getUserData();
		//contactFixtureB.getBody().setUserData(ob);
		
		Collision a = new Collision((Entity)contactFixtureA.getBody().getUserData(), (Entity)contactFixtureB.getBody().getUserData());
		if(!collision.contains(a))
			collision.add(a);
	}
	
	public void endContact(Contact contact){
		contact.getFixtureA();
		contact.getFixtureB();
		
		/*Object ob = (Integer)contactFixtureA.getBody().getUserData() & ~(Integer)contactFixtureB.getUserData();
		contactFixtureA.getBody().setUserData(ob);

		ob = (Integer)contactFixtureB.getBody().getUserData() & ~(Integer)contactFixtureA.getUserData();
		contactFixtureB.getBody().setUserData(ob);
		
		Collision a = new Collision((Entity)contactFixtureA.getBody().getUserData(), (Entity)contactFixtureB.getBody().getUserData());
		if(collision.contains(a)){
			System.out.println("Removing.");
			collision.remove(a);
		}*/
	}
	
	public void preSolve(Contact contact, Manifold manifold){
	}
	
	public void postSolve(Contact contact, ContactImpulse impulse){
	}
}
