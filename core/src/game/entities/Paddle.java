package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.registry.SpriteRegistry;

import static game.B2DVars.PPM_MAT_INV;

public class Paddle extends SimpleBox2DEntity {
    private static final float DEFAULT_PADDLE_SPPEED = 0.1f;
    private static final SpriteRegistry DEFAULT_SPRITE_REGISTRY = SpriteRegistry.PADDLE_ELECTRIC_1;
    private static final float SCALE = 1/2f;

    private float paddleSpeed = DEFAULT_PADDLE_SPPEED;

    private float changeX;
    private float changeY;
    private Vector2 previousPos = new Vector2();
    private Vector2 currentPos = new Vector2();

    public Paddle(World world, TextureAtlas atlas) {
        super(PlatformSpawner.spawnPlatform(world,
            new Vector2(Gdx.graphics.getWidth() / 2f, DEFAULT_SPRITE_REGISTRY.getSizeY() / 2f).mul(PPM_MAT_INV),
            new Vector2(DEFAULT_SPRITE_REGISTRY.getSizeX() * SCALE, DEFAULT_SPRITE_REGISTRY.getSizeY() * SCALE).mul(PPM_MAT_INV)),
            DEFAULT_SPRITE_REGISTRY, atlas);
        sprite.setScale(SCALE, SCALE);
        changeX = 0;
        changeY = 0;
        previousPos.set(this.box2DBody.getPosition());
        currentPos.set(this.box2DBody.getPosition());
    }

    public void addEffect(Effects effect) {
        effect.apply(this);
    }

    public enum Effects {
        Speed(p -> p.paddleSpeed += DEFAULT_PADDLE_SPPEED),
        Slowness(p -> p.paddleSpeed -= DEFAULT_PADDLE_SPPEED),
        ;

        private final EffectDef effect;

        Effects(EffectDef effect) {
            this.effect = effect;
        }

        void apply(Paddle paddle) {
            effect.apply(paddle);
        }

        static void removeAll(Paddle paddle) {
            paddle.paddleSpeed = DEFAULT_PADDLE_SPPEED;
        }
    }

    interface EffectDef {
        void apply(Paddle paddle);
    }

    @Override
    public void update(float delta){
        super.update(delta);

        if (currentPos != null && previousPos != null) {
            currentPos.set(box2DBody.getPosition());
            changeX = currentPos.x - previousPos.x;
            changeY = currentPos.y - previousPos.y;
            previousPos.set(currentPos.x, currentPos.y);
        }

    }

    public float getChangeX() {
        return changeX;
    }

    public void setChangeX(float changeX) {
        this.changeX = changeX;
    }

    public float getChangeY() {
        return changeY;
    }

    public void setChangeY(float changeY) {
        this.changeY = changeY;
    }
}
