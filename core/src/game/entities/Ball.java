package game.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import game.registry.SpriteRegistry;

public class Ball extends SimpleBox2DEntity {

    public Ball(Body box2DBody, TextureAtlas atlas) {
        super(box2DBody, SpriteRegistry.BALL, atlas);
    }

}
