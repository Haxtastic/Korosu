package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.haxtastic.korosu.EntityFactory;
import com.haxtastic.korosu.systems.SimulationSystem;

public class Weapon extends Component {
	public float speed, rof, spread, range;
	public int amount = 1;
	public String type;
	public String ammo;
	public boolean screened = false;
	
	public Weapon() {
	}
	
	public Weapon(String weapon) {
		if(weapon.compareToIgnoreCase("shotgun") == 0){
			this.speed = 15f;
			this.rof = 0.5f;
			this.spread = 5f;
			this.range = 6f;
			this.type = "Shotgun";
			this.ammo = "shell";
		}
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
			for(int i = -20; i < 20; i+=4){
				EntityFactory.createBullet(world, sim, x, y, (angle + i) * MathUtils.degreesToRadians, speed, bullet).addToWorld();
			}
		}
	}
}
