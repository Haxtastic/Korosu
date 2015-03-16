package com.haxtastic.korosu.components;

import com.artemis.Component;

public class Ammo extends Component {
	public int amount, damage;
	public String bullet;
	public float width = 0.2f;
	public boolean screened = false;
	
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
	
	@Override
	public boolean equals(Object o) {
		Ammo obj = (Ammo)o;
		if(obj.bullet == bullet)
			return true;
		return false;
	}
}
