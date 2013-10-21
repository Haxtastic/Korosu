package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Input extends Component {
	public Vector2 velocity;
	public boolean shoot = false;
	public float angle = 0;
	public float lastShot = 0;
	public boolean cycle = false;
	public float lastCycle = 0;
	
	public Input() {
		velocity = new Vector2();
	}
	
	public void clean() {
		velocity.set(0, 0);
		shoot = false;
		cycle = false;
	}
}
