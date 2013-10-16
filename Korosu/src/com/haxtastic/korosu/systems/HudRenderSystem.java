package com.haxtastic.korosu.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.haxtastic.korosu.Korosu;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Actor;
import com.haxtastic.korosu.components.Distance;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Sprite;
import com.haxtastic.korosu.components.TouchpadComp;
import com.haxtastic.korosu.components.Velocity;

public class HudRenderSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;
	
	public HudRenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Player.class, Position.class));
		this.camera = camera;
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
	}
	
	@Override
	protected void initialize() {
		batch = new SpriteBatch();
		
		Texture fontTexture = new Texture(Gdx.files.internal("fonts/hud_0.png"));
		fontTexture.setFilter(TextureFilter.Linear, TextureFilter.MipMapLinearLinear);
		TextureRegion fontRegion = new TextureRegion(fontTexture);
		font = new BitmapFont(Gdx.files.internal("fonts/hud.fnt"), fontRegion, false);
		font.setUseIntegerPositions(false);
	}
	
	@Override
	protected void begin() {
		if(Gdx.app.getType() == ApplicationType.Android){
			Stage pad = world.getManager(GroupManager.class).getEntities(Constants.Groups.TOUCHPAD).get(0).getComponent(TouchpadComp.class).pad;
			pad.draw();
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected void process(Entity e) {
		batch.setColor(1, 1, 1, 1);
		Position pos = pm.get(e);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, Korosu.FRAME_HEIGHT - (font.getBounds("F").height) - 8);
		font.draw(batch, "Position: " + pos.x + ":" + pos.y + " - " + (pos.x+pos.y), 20, Korosu.FRAME_HEIGHT - (font.getBounds("F").height*2) - (8*2));
		font.draw(batch, "Haxtastic gaming 2013 by mAgz", 20, 30);
	}
	
	@Override
	protected void end() {
		batch.end();
		camera.update();
	}
	
}
