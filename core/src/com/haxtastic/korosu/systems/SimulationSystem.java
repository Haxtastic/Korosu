package com.haxtastic.korosu.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Velocity;

public class SimulationSystem extends EntitySystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Actor> am;
	@Mapper ComponentMapper<Player> playm;
	
	public World simulation;
	public Vector2 gravity;
	private List<Body> deadBodies = new ArrayList<Body>();
	
	@SuppressWarnings("unchecked")
	public SimulationSystem(float x, float y){
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Actor.class));
		gravity = new Vector2(x, y);
	}
	
	@SuppressWarnings("unchecked")
	public SimulationSystem(Vector2 grav){
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Actor.class));
		gravity = grav;
	}
	
	@Override
	public void initialize() {
		simulation = new World(gravity, true);
		simulation.setAutoClearForces(false);
		/*
        BodyDef bd = new BodyDef();
        bd.position.set(0, 0);
        bd.type = BodyType.StaticBody;
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1600/Constants.PIXELS_PER_METER_X, 2);
        
        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.1f;
        fd.shape = shape;
        //fd.isSensor = true;
        
        Body body = simulation.createBody(bd);
        body.setUserData((Object)0);
        body.createFixture(fd).setUserData(2);
        bd.position.set(0, 9);
        body = simulation.createBody(bd);
        body.setUserData((Object)0);
        body.createFixture(fd).setUserData(2);
        //shape.setAsBox(160/Constants.PIXELS_PER_METER_X, 0.3f);
        //bd.position.set(1, 1.5f);
        //simulation.createBody(bd).createFixture(fd).setUserData(2);
        
        shape.dispose();
        */
	}
	
	public void destroyBody(Body body) {
		if(!deadBodies.contains(body))
			deadBodies.add(body);
	}
	
	@Override
	protected boolean checkProcessing() {
		Player player = world.getManager(GroupManager.class).getEntities(Constants.Groups.PLAYERS).get(0).getComponent(Player.class);
		if(player.started && player.alive)
			return true;
		else
			return false;
	}
	
	@Override
	protected void begin() {
		ListIterator<Body> iter = deadBodies.listIterator();
		while(iter.hasNext())
			simulation.destroyBody(iter.next());
		deadBodies.clear();
	}
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities){
		for(int i = 0; entities.size() > i; i++)
			processBefore(entities.get(i));
		simulation.step(world.delta, 10, 10);
		for(int i = 0; entities.size() > i; i++)
			processAfter(entities.get(i));
	}
	
	protected void processBefore(Entity e) {
		Body actor = am.get(e).actor;
		Vector2 velocity = vm.get(e).velocity;
		Position position = pm.get(e);
		position.px = position.x; position.py = position.y; position.pr = position.r;
		actor.setLinearVelocity(velocity);
		actor.setTransform(actor.getPosition(), position.r);
	}
	
	protected void processAfter(Entity e) {
		Body actor = am.get(e).actor;
		vm.get(e);
		Position position = pm.get(e);
		//if(velocity.x != 0 || velocity.y != 0)
		//	actor.setLinearVelocity(velocity);
		//velocity.set(0, 0);
		position.x = actor.getPosition().x;
		position.y = actor.getPosition().y;
		position.r = actor.getAngle();
		
	}
	
	@Override
	protected void end() {
	}
}
