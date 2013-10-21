package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.systems.SimulationSystem;

public class Weapon extends Component {
	public float speed, rof, spread, range;
	public int amount = 1;
	public String type;
	public String ammo;
	
	public Weapon() {
	}
	
	public Weapon(float speed, float rof, float spread, float range, String type, String ammo) {
		this.speed = speed;
		this.rof = rof;
		this.spread = spread;
		this.range = range;
		this.type = type;
		this.ammo = ammo;
	}
	
	public void shoot(World world, SimulationSystem sim, float x, float y, float r, Bullet bullet) {
		if(type == "Pistol" || type == "Submachine" || type == "Colt" || type == "Turbo")
			EntityFactory.createBullet(world, sim, x, y, r, speed, bullet).addToWorld();
		else if(type == "Shotgun"){
			float angle = r * MathUtils.radiansToDegrees;
			for(int i = -20; i < 20; i+=8){
				EntityFactory.createBullet(world, sim, x, y, (angle + i) * MathUtils.degreesToRadians, speed, bullet).addToWorld();
			}
		}
	}
}
