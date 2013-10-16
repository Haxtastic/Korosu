package com.haxtastic.korosu.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.haxtastic.korosu.Constants;
import com.haxtastic.korosu.components.Player;
import com.haxtastic.korosu.components.Position;
import com.haxtastic.korosu.components.Sprite;

public class SpriteRenderSystem extends EntitySystem {
	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Sprite> sm;
	@Mapper
	ComponentMapper<Player> plm;

	private HashMap<String, AtlasRegion> regions;
	private TextureAtlas textureAtlas;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Bag<AtlasRegion> regionsByEntity;
	private List<Entity> sortedEntities;
	
	@SuppressWarnings("unchecked")
	public SpriteRenderSystem(OrthographicCamera camera) {
		super(Aspect.getAspectForAll(Position.class, Sprite.class));
		this.camera = camera;
	}

	@Override
	protected void initialize() {
		regions = new HashMap<String, AtlasRegion>();
		textureAtlas = new TextureAtlas(Gdx.files.internal("textures/textures.atlas"));
		for (AtlasRegion r : textureAtlas.getRegions())
			regions.put(r.name, r);
		regionsByEntity = new Bag<AtlasRegion>();

		batch = new SpriteBatch();
		
		sortedEntities = new ArrayList<Entity>();
	}
	
	@Override
	protected void begin() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		for(int i = 0; sortedEntities.size() > i; i++) {
			process(sortedEntities.get(i));
		}
	}

	protected void process(Entity e) {
		if(pm.has(e)) {
			Position position = pm.getSafe(e);
			Sprite sprite = sm.get(e);
	
			AtlasRegion spriteRegion = regionsByEntity.get(e.getId());
			batch.setColor(sprite.r, sprite.g, sprite.b, sprite.a);
			
			float posX = sprite.x * Constants.PIXELS_PER_METER_X;// - (spriteRegion.getRegionWidth() / 2 * sprite.scaleX);
			float posY = sprite.y * Constants.PIXELS_PER_METER_Y;// - (spriteRegion.getRegionHeight() / 2 * sprite.scaleX);
			float originX = 0;
			float originY = 0;
			if(sprite.centered){
				posX = posX - ((sprite.scaleX * Constants.PIXELS_PER_METER_X)/2);
				posY = posY - ((sprite.scaleY * Constants.PIXELS_PER_METER_Y)/2);
				originX = ((sprite.scaleX * Constants.PIXELS_PER_METER_X)/2);
				originY = ((sprite.scaleY * Constants.PIXELS_PER_METER_Y)/2);
			}
				
			batch.draw(spriteRegion, posX, posY, originX, originY, (sprite.scaleX * Constants.PIXELS_PER_METER_X), (sprite.scaleY * Constants.PIXELS_PER_METER_Y), 1, 1, sprite.rotation * MathUtils.radiansToDegrees);
		}
	}

	protected void end() {
		batch.end();
	}

	@Override
	protected void inserted(Entity e) {
		Sprite sprite = sm.get(e);
		regionsByEntity.set(e.getId(), regions.get(sprite.name));

		sortedEntities.add(e);
		
		Collections.sort(sortedEntities, new Comparator<Entity>() {
			@Override
			public int compare(Entity e1, Entity e2) {
				Sprite s1 = sm.get(e1);
				Sprite s2 = sm.get(e2);
				return s1.layer.compareTo(s2.layer);
			}
		});
	}

	@Override
	protected void removed(Entity e) {
		regionsByEntity.set(e.getId(), null);
		sortedEntities.remove(e);
	}

}
