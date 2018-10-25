package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;

import static game.B2DVars.PPM;

public class Ball {

    private static final String BALL_SPRITE_NAME = "Ball";

    private Sprite sprite;

    private Body box2DBody;

    public Ball(float x, float y, float radius, TextureAtlas atlas) {
        sprite = atlas.createSprite(BALL_SPRITE_NAME);
        sprite.setOriginBasedPosition(x, y);
        setSpriteRadius(radius);
    }

    public void update(float delta) {
        if (box2DBody != null) {
            // position
            Vector2 worldCenter = box2DBody.getWorldCenter();
            setSpritePosition(new Vector2(worldCenter.x * PPM, worldCenter.y * PPM));

            // size
            setSpriteRadius(getCircleShape().getRadius() * PPM);

            // angle
            setSpriteRotation(box2DBody.getAngle());
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Vector2 getPosition() {
        return new Vector2(
            sprite.getX() + sprite.getOriginX(),
            sprite.getY() + sprite.getOriginY());
    }

    private void setSpritePosition(Vector2 pos) {
        sprite.setOriginBasedPosition(pos.x, pos.y);
    }

    public float getRadius() {
        if (box2DBody != null) {
            return getCircleShape().getRadius() * PPM;
        }
        return 0;
    }

    private void setSpriteRadius(float radius) {
        sprite.setScale(radius / (sprite.getWidth() / 2), radius / (sprite.getHeight() / 2));
    }

    private void setSpriteRotation(float angle) {
        sprite.setRotation(angle);
    }

    public void setBox2DBody(Body ballBody) {
        this.box2DBody = ballBody;
        update(0f);
    }

    private CircleShape getCircleShape() {
        if (box2DBody != null) {
            return (CircleShape) box2DBody.getFixtureList().get(0).getShape();
        }
        return null;
    }
}
