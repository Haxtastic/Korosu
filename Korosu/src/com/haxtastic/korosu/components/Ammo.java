package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Ammo extends Component {
	public int amount, damage;
	public String bullet;
	public float width = 0.2f;
	
	public Ammo() {
	}
	
	public Ammo(int damage, int amount, String bullet) {
		this.damage = damage;
		this.amount = amount;
		this.bullet = bullet;
	}
	
	public String getBullet() {
		return bullet;
	}
}
