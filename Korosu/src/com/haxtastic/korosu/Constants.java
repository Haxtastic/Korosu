package com.haxtastic.korosu;

public class Constants {
	
	public class Groups {
		public static final String PLAYERS = "players";
		public static final String GUYS = "guys";
		public static final String AMMO = "ammo";
		public static final String BULLETS = "bullets";
		public static final String WEAPONS = "weapons";
		public static final String WALLS = "WALLS";
		public static final String SCREEN_CHANGE = "screen change";
		public static final String DISTANCE = "distance";
		public static final String TOUCHPAD = "touchpad";
	}
	
	public static float PIXELS_PER_METER_X;
	public static float PIXELS_PER_METER_Y;
	
	public class Layers {
		public static final int PLAYER = (1 << 0);
		public static final int ENEMY = (1 << 1);
		public static final int WALL = (1 << 2);
		public static final int BULLET = (1 << 3);
		public static final int AMMO = (1 << 4);
		public static final int WEAPON = (1 << 5);
	}
	
	public class Masks {
		public static final int PLAYER = Constants.Layers.ENEMY | Constants.Layers.WALL;
		public static final int ENEMY = Constants.Layers.PLAYER | Constants.Layers.WALL | Constants.Layers.BULLET;
		public static final int WALL = Constants.Layers.PLAYER | Constants.Layers.ENEMY | Constants.Layers.BULLET;
		public static final int BULLET = Constants.Layers.ENEMY | Constants.Layers.WALL;
		public static final int AMMO = (1 << 4);
		public static final int WEAPON = (1 << 5);
	}

}
