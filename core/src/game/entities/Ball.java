package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import game.Level;
import game.registry.SpriteRegistry;

import static game.B2DVars.PPM_INV;

public class Ball extends SimpleBox2DEntity {

    public Ball(Body box2DBody, TextureAtlas atlas) {
        super(box2DBody, SpriteRegistry.BALL, atlas);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // check if dead.
        if (box2DBody.getPosition().y < 0){
            box2DBody.setTransform(Gdx.graphics.getWidth()/2f * PPM_INV, Gdx.graphics.getHeight()/2f * PPM_INV, 0f);
            Level.lives -= 1;
        }
    }
}
