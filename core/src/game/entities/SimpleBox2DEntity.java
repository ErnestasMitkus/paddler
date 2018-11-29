package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import engine.box2d.utils.ShapeUtils;
import game.registry.SpriteRegistry;

import static game.B2DVars.PPM;
import static game.B2DVars.PPM_MAT;

public class SimpleBox2DEntity {

    protected Sprite sprite;
    protected Body box2DBody;

    protected boolean isFlaggedForDelete;

    public SimpleBox2DEntity(Body box2DBody, SpriteRegistry spriteRegistry, TextureAtlas atlas) {
        super();
        sprite = spriteRegistry.createSprite(atlas);
        setBox2DBody(box2DBody);
        box2DBody.setUserData(this);
    }

    public void update(float delta) {
        if (box2DBody != null) {
            // position
            setSpritePosition(box2DBody.getWorldCenter().cpy().mul(PPM_MAT));

            // size
            updateSpriteScale();

            // angle
            setSpriteRotation(box2DBody.getAngle());
        }
    }

    private void updateSpriteScale() {
        box2DBody.getFixtureList().forEach(it -> {
            Shape shape = it.getShape();
            if (shape instanceof CircleShape) {
                setSpriteRadius(shape.getRadius() * PPM);
            }
        });
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
        CircleShape circleShape = getCircleShape();
        return circleShape != null ? circleShape.getRadius() * PPM : 0;
    }

    private void setSpriteRadius(float radius) {
        sprite.setScale(radius / (sprite.getWidth() / 2), radius / (sprite.getHeight() / 2));
    }

    private void setSpriteRotation(float angle) {
        sprite.setRotation(angle);
    }

    public Body getBox2DBody() {
        return box2DBody;
    }

    protected void setBox2DBody(Body ballBody) {
        this.box2DBody = ballBody;
        update(0f);
    }

    public void delete() {
        isFlaggedForDelete = true;
    }

    public boolean isFlaggedForDelete() {
        return isFlaggedForDelete;
    }

    private CircleShape getCircleShape() {
        return ShapeUtils.getShape(box2DBody, CircleShape.class);
    }
}
