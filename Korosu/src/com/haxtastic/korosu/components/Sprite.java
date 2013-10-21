package com.haxtastic.korosu.components;

import com.artemis.Component;

public class Sprite extends Component {
	public enum Layer {
		DEFAULT,
		BACKGROUND,
		WALLS,
		GUYS,
		BULLETS,
		PARTICLES;
		
		public int getLayerId() {
			return ordinal();
		}
	}
	
	public String name;
	public float scaleX = 1;
	public float scaleY = 1;
	public float rotation;
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	public float x;
	public float y;
	public boolean centered = false;
	public Layer layer = Layer.DEFAULT;
}
