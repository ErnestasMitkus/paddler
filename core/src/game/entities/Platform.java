package game.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.registry.SpriteRegistry;

public class Platform extends SimpleBox2DEntity {

    public Platform(Body box2DBody, Vector2 size, TextureAtlas atlas) {
        super(box2DBody, SpriteRegistry.GREEN_TILE, atlas);
        sprite.setScale(size.x / sprite.getWidth(), size.y / sprite.getHeight());
    }
}
