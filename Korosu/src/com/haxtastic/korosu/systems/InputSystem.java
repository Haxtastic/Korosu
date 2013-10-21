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
import com.haxtastic.korosu.components.Ammo;
import com.haxtastic.korosu.components.Bullet;
import com.haxtastic.korosu.components.Input;
import com.haxtastic.korosu.components.Inventory;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.TouchpadComp;
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
	
	private boolean up, down, left, right;
	private boolean shoot;
	public float time, pressTime = 0;
	private float cooldown = 0.25f;
	private float speed = 5;
	
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
		Bullet bullet = inv.getBullet();
		SimulationSystem sim = am.get(e).w;
		//Bullet ammo = amm.get(e);
		time += world.delta;
		
		vm.get(e).velocity.set(input.velocity);
		pos.r = input.angle;
		
		if(weapon.rof < (time - input.lastShot) && input.shoot) {
			weapon.shoot(world, sim, pos.x, pos.y, pos.r, bullet);
			input.lastShot = time;
			/*System.out.println("Speed: " + (weapon.speed));
			System.out.println("Rate of Fire: " + (weapon.rof));
			System.out.println("Name: " + (weapon.type));
			System.out.println("Bullet: " + (bullet.type));*/
		}
		if(0.2f < (time - input.lastCycle) && input.cycle){
			inv.cycleWeapon(false);
			input.lastCycle = time;
		}
		input.clean();		
	}

}
