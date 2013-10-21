package com.haxtastic.korosu.components;

import com.artemis.Component;

public class Position extends Component {
	public float x, y, px, py, r, pr = 0;
	public float ix, iy, ipx, ipy = 0;
	
	public Position(){
	}
	
	public Position(float x, float y, float r) {
		this.x = this.px = x;
		this.y = this.py = y;
		this.r = this.pr = r;
	}
}
