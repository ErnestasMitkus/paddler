package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Ball {

    private static final String BALL_SPRITE_NAME = "Ball";

    private Sprite sprite;

    public Ball(float x, float y, TextureAtlas atlas) {
        sprite = atlas.createSprite(BALL_SPRITE_NAME);
        sprite.setPosition(x, y);
    }

    public void push(float vx, float vy) {
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
