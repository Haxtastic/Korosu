package com.haxtastic.korosu.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.haxtastic.korosu.systems.SimulationSystem;



public class Actor extends Component {
	public Body actor;
	public String name;
	public SimulationSystem w;
	
	public Actor(SimulationSystem world, BodyType bt){
		w = world;
        BodyDef bd = new BodyDef();
        bd.type = bt;
        actor = w.simulation.createBody(bd);
	}
	
	public Actor(SimulationSystem world, BodyType bt, boolean bullet){
		w = world;
        BodyDef bd = new BodyDef();
        bd.type = bt;
        bd.bullet = bullet;
        actor = w.simulation.createBody(bd);
	}
	
	public Actor(SimulationSystem world, BodyDef bd){
		w = world;
        actor = w.simulation.createBody(bd);
        
	}
	
	public Actor(SimulationSystem world, float x, float y, BodyType bt){
		w = world;
        BodyDef bd = new BodyDef();
        bd.type = bt;
        bd.position.set(x, y);

        actor = w.simulation.createBody(bd);
	}
	
	public Actor(SimulationSystem world, float x, float y, BodyType bt, boolean bullet){
		w = world;
        BodyDef bd = new BodyDef();
        bd.type = bt;
        bd.position.set(x, y);
        bd.bullet = bullet;

        actor = w.simulation.createBody(bd);
	}
	
	public Actor(SimulationSystem world, Vector2 pos, BodyType bt){
		w = world;
        BodyDef bd = new BodyDef();
        bd.type = bt;
        bd.position.set(pos.x, pos.y);

        actor = w.simulation.createBody(bd);
	}
}
