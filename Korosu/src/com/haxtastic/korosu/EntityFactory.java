package com.haxtastic.korosu;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.haxtastic.korosu.systems.SimulationSystem;
import com.haxtastic.korosu.components.AnimationSprite;
import com.haxtastic.korosu.components.Bullet;
import com.haxtastic.korosu.components.Enemy;
import com.haxtastic.korosu.components.Floating;
import com.haxtastic.korosu.components.Health;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Inventory;
import com.haxtastic.korosu.components.Offset;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Sprite;
import com.haxtastic.korosu.components.Velocity;
import com.haxtastic.korosu.components.Actor;

public class EntityFactory {
	
	static BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("fixtures/fixtures.json"));
	
	public static void cleanWorld(World world){
		ImmutableBag<EntitySystem> systems = world.getSystems();
		for(int i = 0; systems.size() > i; i++) {
			world.deleteSystem(systems.get(i));
		}
	}
	
	public static Entity createBackground(World world, SimulationSystem sim) {
		Entity e = world.createEntity();
		
		Position position = new Position();
		position.x = 0;
		position.y = 0;
		e.addComponent(position);
		
		Sprite sprite = new Sprite();
		sprite.name = "back";
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.scaleX = 20.0f;
		sprite.scaleY = 12.0f;
		sprite.layer = Sprite.Layer.BACKGROUND;
		e.addComponent(sprite);
		
		return e;
	}
		
	public static Entity createPlayer(World world, SimulationSystem sim, float x, float y) {
		Entity e = world.createEntity();
		
		Position position = new Position();
		position.x = position.px = x;
		position.y = position.py = y;
		e.addComponent(position);
		
		e.addComponent(new Health(5));
		
		Sprite sprite = new Sprite();
		sprite.name = "player";
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.centered = true;
		sprite.scaleX = 0.5f;
		sprite.scaleY = 1.0f;
		sprite.layer = Sprite.Layer.GUYS;
		e.addComponent(sprite);
		
		Velocity velocity = new Velocity();
		velocity.velocity = new Vector2(0, 0);
		e.addComponent(velocity);
		
        FixtureDef fd = new FixtureDef();
        fd.density = 1.5f;
        fd.friction = 0.5f;
        fd.filter.categoryBits = Constants.Layers.PLAYER;
        fd.filter.maskBits = Constants.Masks.PLAYER;
        
        Actor actor = new Actor(sim, x, y, BodyType.DynamicBody);
        actor.name = "Player";
        actor.actor.setFixedRotation(true);
        
        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(actor.actor, "player", fd, 0.5f, Constants.Layers.PLAYER);
        actor.actor.setUserData((Object)0);

        e.addComponent(actor);
        
        e.addComponent(new Input());
        e.addComponent(new Inventory());
		
		Player player = new Player();
		player.alive = true;
		player.started = true;
		player.restart = false;
		e.addComponent(player);
		
		world.getManager(GroupManager.class).add(e, Constants.Groups.PLAYERS);
		
		return e;
	}
	
	public static Entity createGuy(World world, SimulationSystem sim, float x, float y, float speed, String type){
		Entity e = world.createEntity();
		
		Position position = new Position();
		position.x = position.px = x;
		position.y = position.py = y;
		e.addComponent(position);
		
		e.addComponent(new Enemy());
		
		e.addComponent(new Health(5));
		
		Sprite sprite = new Sprite();
		sprite.name = type;
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.centered = true;
		sprite.scaleX = 0.5f;
		sprite.scaleY = 1.0f;
		sprite.layer = Sprite.Layer.GUYS;
		e.addComponent(sprite);
		
		Velocity velocity = new Velocity();
		velocity.velocity = new Vector2(0, 0);
		e.addComponent(velocity);
		
        FixtureDef fd = new FixtureDef();
        fd.density = 1.5f;
        fd.friction = 0.5f;
        fd.filter.categoryBits = Constants.Layers.ENEMY;
        fd.filter.maskBits = Constants.Masks.ENEMY;
        
        Actor actor = new Actor(sim, x, y, BodyType.DynamicBody);
        actor.name = type;
        actor.actor.setFixedRotation(true);
        
        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(actor.actor, type, fd, 0.5f, Constants.Layers.ENEMY);
        actor.actor.setUserData((Object)0);
        
        e.addComponent(actor);

        world.getManager(GroupManager.class).add(e, Constants.Groups.GUYS);
        
		return e;
	}
	
	public static Entity createWall(World world, SimulationSystem sim, float x, float y, String type) {
		Entity e = world.createEntity();
		
		Position position = new Position();
		position.x = x;
		position.y = y;
		e.addComponent(position);
		
		Sprite sprite = new Sprite();
		sprite.name = type;
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.scaleX = 0.75f;
		sprite.scaleY = 5f;
		sprite.layer = Sprite.Layer.WALLS;
		e.addComponent(sprite);
		
		Velocity velocity = new Velocity();
		velocity.velocity = new Vector2(0, 0);
		e.addComponent(velocity);
		
        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        
        
        Actor actor = new Actor(sim, position.x, position.y, BodyType.StaticBody);
        actor.name = type;
        actor.actor.setFixedRotation(true);
        
        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(actor.actor, type, fd, 0.75f, Constants.Layers.WALL);
        actor.actor.setUserData((Object)0);
        
        e.addComponent(actor);
		
		world.getManager(GroupManager.class).add(e, Constants.Groups.GUYS);
		
		return e;
	}

	public static Entity createBullet(World world, SimulationSystem sim, float x, float y, float r, float speed, Bullet bullet) {
		Entity e = world.createEntity();
		
		bullet.origin.set(x, y);
		e.addComponent(bullet);
		
		Sprite sprite = new Sprite();
		sprite.name = bullet.type;
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.scaleX = 0.2f;
		sprite.scaleY = 0.1f;
		sprite.layer = Sprite.Layer.BULLETS;
		e.addComponent(sprite);
		
		Position position = new Position(x, y, r);
		e.addComponent(position);
		
		Velocity velocity = new Velocity();
		velocity.velocity = new Vector2(speed * (MathUtils.cos(position.r)), speed * (MathUtils.sin(position.r)));
		e.addComponent(velocity);
		
        FixtureDef fd = new FixtureDef();
        fd.density = 1.5f;
        fd.friction = 0.5f;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.Layers.BULLET;
        fd.filter.maskBits = Constants.Masks.BULLET;
        
        Actor actor = new Actor(sim, position.x, position.y, BodyType.DynamicBody);
        actor.name = bullet.type;
        //actor.actor.setTransform(actor.actor.getPosition(), position.r);
        actor.actor.setFixedRotation(true);
        
        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(actor.actor, bullet.type, fd, bullet.width, Constants.Layers.BULLET);
        actor.actor.setUserData((Object)0);
        
        e.addComponent(actor);
        
        world.getManager(GroupManager.class).add(e, Constants.Groups.BULLETS);
		
		return e;
	}

	public static Entity createExplosion(World world, float x, float y) {
		Entity e = world.createEntity();
		
		AnimationSprite sprite = new AnimationSprite();
		sprite.name = "explosion";
		sprite.width = 256;
		sprite.height = 128;
		sprite.cols = 6;
		sprite.rows = 6;
		sprite.frameTime = 0.025f;
		sprite.r = 255/255f;
		sprite.g = 255/255f;
		sprite.b = 255/255f;
		sprite.a = 255/255f;
		sprite.scaleX = 1.5f;
		sprite.scaleY = 1.5f;
		sprite.active = true;
		sprite.layer = AnimationSprite.Layer.PARTICLES;
		e.addComponent(sprite);
		
		Position position = new Position();
		position.x = x - sprite.scaleX/2;
		position.y = y - sprite.scaleY/2;
		e.addComponent(position);
		
		
		return e;
	}
}
