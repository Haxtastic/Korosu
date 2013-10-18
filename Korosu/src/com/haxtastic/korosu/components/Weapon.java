package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Weapon extends Component {
	public float speed, rof, spread, range;
	public int amount;
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
}
