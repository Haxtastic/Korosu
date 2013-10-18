package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Ammo extends Component {
	public int amount;
	public Bullet bullet;
	
	public Ammo() {
	}
	
	public Ammo(int amount, Bullet bullet) {
		this.amount = amount;
		this.bullet = bullet;
	}
	
	public Bullet getBullet() {
		return bullet;
	}
}
