package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.registry.SpriteRegistry;

public class Ball extends SimpleBox2DEntity {

    public Ball(Body box2DBody, TextureAtlas atlas) {
        super(box2DBody, SpriteRegistry.BALL, atlas);
    }

    @Override
    public void updateSpriteScale() {
        super.updateSpriteScale();
        float scale = (this.box2DBody.getFixtureList().get(0).getShape().getRadius()) / (this.sprite.getWidth() * (1 / 100f) / 2);
        this.sprite.setScale(this.sprite.getScaleX() * scale, this.sprite.getScaleY() * scale);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            System.out.println("Now rotating the fucking ball!");
            rotateDirectionByAngle(5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            rotateDirectionByAngle(-5f);
        }
    }

    public void rotateDirectionByAngle(float angle) {
        Vector2 vec = this.box2DBody.getLinearVelocity();
        float theta = angle * MathUtils.degreesToRadians;
        float cs = MathUtils.cos(theta);
        float sn = MathUtils.sin(theta);
        float x = vec.x * cs - vec.y * sn;
        float y = vec.x * sn + vec.y * cs;
        this.box2DBody.setLinearVelocity(new Vector2(x, y));
    }
}
