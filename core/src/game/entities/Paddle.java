package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.registry.SpriteRegistry;

import static game.B2DVars.PPM_MAT_INV;
import static java.lang.Math.abs;

public class Paddle extends SimpleBox2DEntity {
    private static final float DEFAULT_PADDLE_SPPEED = 0f;
    private static final SpriteRegistry DEFAULT_SPRITE_REGISTRY = SpriteRegistry.PADDLE_ELECTRIC_1;
    private static final float SCALE = 1/2f;

    //PaddleSpeedVector is a fraction value of max movement speed (it should go from -1 to 1)
    private float paddleSpeedVector = DEFAULT_PADDLE_SPPEED;
    public static final float PADDLE_MOVE_SPEED = 20f;

    public Paddle(World world, TextureAtlas atlas) {
        super(PlatformSpawner.spawnPlatform(world,
            new Vector2(Gdx.graphics.getWidth() / 2f, DEFAULT_SPRITE_REGISTRY.getSizeY() / 2f).mul(PPM_MAT_INV),
            new Vector2(DEFAULT_SPRITE_REGISTRY.getSizeX() * SCALE, DEFAULT_SPRITE_REGISTRY.getSizeY() * SCALE).mul(PPM_MAT_INV)),
            DEFAULT_SPRITE_REGISTRY, atlas);
        sprite.setScale(SCALE, SCALE);
    }

    public float getPaddleSpeedVector() {
        return paddleSpeedVector;
    }

    public void updateSpeed(float delta, int xInputVector) {
        float newSpeed = paddleSpeedVector;

        if (xInputVector == 0) {
            float drag = 1f;
            int dragVector = newSpeed > 0 ? -1 : 1;
            newSpeed += drag * dragVector * delta;

            if (abs(newSpeed) <= 0.05f) {
                newSpeed = 0f;
            }
        } else {
            newSpeed += xInputVector * 1.2f * delta;

            if (abs(newSpeed) > 1) {
                newSpeed = newSpeed > 0 ? 1f : -1f;
            }
        }

        paddleSpeedVector = newSpeed;
    }

    public void addEffect(Effects effect) {
        effect.apply(this);
    }

    public enum Effects {
        Speed(p -> p.paddleSpeedVector += DEFAULT_PADDLE_SPPEED),
        Slowness(p -> p.paddleSpeedVector -= DEFAULT_PADDLE_SPPEED),
        ;

        private final EffectDef effect;

        Effects(EffectDef effect) {
            this.effect = effect;
        }

        void apply(Paddle paddle) {
            effect.apply(paddle);
        }

        static void removeAll(Paddle paddle) {
            paddle.paddleSpeedVector = DEFAULT_PADDLE_SPPEED;
        }
    }

    interface EffectDef {
        void apply(Paddle paddle);
    }
}
