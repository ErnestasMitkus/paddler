package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.registry.SpriteRegistry;

import static game.B2DVars.PPM_MAT_INV;
import static java.lang.Math.abs;

public class Paddle extends SimpleBox2DEntity {
    private static final SpriteRegistry DEFAULT_SPRITE_REGISTRY = SpriteRegistry.PADDLE_ELECTRIC_1;
    private static final float SCALE = 1/2f;
    private static final float PADDLE_ACCELERATION = 1.2f;
    private static final float PADDLE_MOVE_SPEED = 20f;

    //PaddleSpeedVector is a fraction value of its movement speed (it should go from -1 to 1)
    private float paddleSpeedVector = 0f;

    public Paddle(World world, TextureAtlas atlas) {
        super(PlatformSpawner.spawnPlatform(world,
            new Vector2(Gdx.graphics.getWidth() / 2f, DEFAULT_SPRITE_REGISTRY.getSizeY() / 2f).mul(PPM_MAT_INV),
            new Vector2(DEFAULT_SPRITE_REGISTRY.getSizeX() * SCALE, DEFAULT_SPRITE_REGISTRY.getSizeY() * SCALE).mul(PPM_MAT_INV)),
            DEFAULT_SPRITE_REGISTRY, atlas);
        sprite.setScale(SCALE, SCALE);
    }

    public void updateSpeed(float delta, int xInputVector) {
        float newSpeedVector = paddleSpeedVector;

        if (xInputVector == 0) {
            //if no inputs are pressed we slow down the paddle
            int dragVector = newSpeedVector > 0 ? -1 : 1;
            newSpeedVector += dragVector * delta;

            if (abs(newSpeedVector) <= 0.05f) {
                newSpeedVector = 0f;
            }
        } else {
            //with inputs present we increase paddle speed
            newSpeedVector += xInputVector * PADDLE_ACCELERATION * delta;
        }
        paddleSpeedVector = MathUtils.clamp(newSpeedVector, -1f, 1f);
    }

    public void move(float delta) {
        Body body = getBox2DBody();
        float deltaX = delta * Paddle.PADDLE_MOVE_SPEED * paddleSpeedVector;
        body.setTransform(body.getPosition().add(deltaX, 0), body.getAngle());
    }
}
