package com.haxtastic.korosu.components;

import com.artemis.Component;

public class Bullet extends Component {
	public float damage;
	public String type;
	public float width;
	
	public Bullet() {
	}
	
	public Bullet(float damage, String type, float width) {
		this.damage = damage;
		this.type = type;
		this.width = width;
	}
}
