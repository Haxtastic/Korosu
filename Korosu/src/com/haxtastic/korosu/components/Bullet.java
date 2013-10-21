package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Component {
	public float damage;
	public String type;
	public float width;
	public float range;
	public Vector2 origin = new Vector2();
	
	public Bullet() {
	}
	
	public Bullet(float damage, String type, float width, float range) {
		this.damage = damage;
		this.type = type;
		this.width = width;
		this.range = range;
	}
}
