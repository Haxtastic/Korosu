package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Ammo;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Inventory;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Velocity;
import com.haxtastic.korosu.components.Weapon;

public class InputSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Inventory> invm;
	@Mapper ComponentMapper<Input> im;
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pom;
	@Mapper ComponentMapper<Actor> am;
	@Mapper ComponentMapper<Weapon> wm;
	@Mapper ComponentMapper<Ammo> amm;
	
	public float time, pressTime = 0;
	@SuppressWarnings("unchecked")
	public InputSystem() {
		super(Aspect.getAspectForAll(Player.class, Input.class, Velocity.class, Position.class));
		time = 0;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void process(Entity e) {
		Inventory inv = invm.get(e);
		Input input = im.get(e);
		Position pos = pom.get(e);
		Weapon weapon = inv.getWeapon();
		Ammo ammo = inv.getAmmo();
		SimulationSystem sim = am.get(e).w;
		//Bullet ammo = amm.get(e);
		time += world.delta;
		
		vm.get(e).velocity.set(input.velocity);
		pos.r = input.angle;
		
		if((ammo.amount > 0 || ammo.amount == -1) && (weapon.rof < (time - input.lastShot)) && (input.shoot)) {
			weapon.shoot(world, sim, pos.x, pos.y, pos.r, inv.getBullet());
			input.lastShot = time;
		}
		if(0.2f < (time - input.lastCycle) && input.spawn){
			EntityFactory.createGuy(world, sim, 2, 5f+MathUtils.random(-3f, 3f), 5f+MathUtils.random(-3f, 3f), "guy").addToWorld();
			input.lastCycle = time;
		}
		
		if(0.2f < (time - input.lastCycle) && input.cycle){
			inv.cycleWeapon(false);
			input.lastCycle = time;
		}
		input.clean();		
	}

}
