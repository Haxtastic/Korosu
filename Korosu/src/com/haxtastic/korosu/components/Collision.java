package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;

public class Collision extends Component {
	public Body a, b;
	public boolean done = false;
	
	public Collision(Body a, Body b) {
		this.a = a;
		this.b = b;
	}
	
	public boolean equals(Object o) {
		Collision col = (Collision)o;
		if(col.a == this.a || col.a == this.b && col.b == this.a || col.b == this.b)
			return true;
		return false;
	}
	
	public boolean contains(Object o) {
		Collision col = (Collision)o;
		System.out.print("HEJ");
		if(col.a == this.a || col.a == this.b || col.b == this.a || col.b == this.b)
			return true;
		return false;
	}
	
	public boolean remove(Object o) {
		Body body = (Body)o;
		if(a == body || b == body)
			return true;
		return false;
	}
}
