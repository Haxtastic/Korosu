package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.artemis.ComponentType;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;

public class Collision extends Component {
	public Entity a, b;
	public boolean done = false;
	
	public Collision(Entity a, Entity b) {
		this.a = a;
		this.b = b;
	}
	
	public boolean has(java.lang.Class<? extends Component> c) {
		return !(a.getComponent(ComponentType.getTypeFor(c)) == null && b.getComponent(ComponentType.getTypeFor(c)) == null);
	}
	
	public boolean has(Entity e, java.lang.Class<? extends Component> c) {
		return !(e.getComponent(ComponentType.getTypeFor(c)) == null);
	}
	
	public boolean has(Body body) {
		Actor aBody = (Actor)a.getComponent(ComponentType.getTypeFor(Actor.class));
		Actor bBody = (Actor)b.getComponent(ComponentType.getTypeFor(Actor.class));
		return (aBody.actor == body || bBody.actor == body);
	}
	
	@Override
	public boolean equals(Object o) {
		Collision col = (Collision)o;
		if((col.a == this.a || col.a == this.b) && (col.b == this.a || col.b == this.b))
			return true;
		return false;
	}
	
	public boolean remove(Object o) {
		Entity body = (Entity)o;
		System.out.println("remove: " + a + " == " + body + " || " + b + " == " + body);
		if(a == body || b == body)
			return true;
		return false;
	}
}
