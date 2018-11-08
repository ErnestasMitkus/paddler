package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.registry.SpriteRegistry;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_MAT_INV;

public class Paddle extends SimpleBox2DEntity {
    private static final float DEFAULT_PADDLE_SPPEED = 0.1f;
    private static final SpriteRegistry DEFAULT_SPRITE_REGISTRY = SpriteRegistry.PADDLE_ELECTRIC_1;

    private float paddleSpeed = DEFAULT_PADDLE_SPPEED;
    private List<Effects> currentEffects = new ArrayList<>();

    public Paddle(World world, TextureAtlas atlas) {
        super(PlatformSpawner.spawnPlatform(world,
            new Vector2(Gdx.graphics.getWidth() / 2f, DEFAULT_SPRITE_REGISTRY.getSizeY() / 2f).mul(PPM_MAT_INV),
            new Vector2(DEFAULT_SPRITE_REGISTRY.getSizeX(), DEFAULT_SPRITE_REGISTRY.getSizeY()).mul(PPM_MAT_INV)),
            DEFAULT_SPRITE_REGISTRY, atlas);
    }

    public void addEffect(Effects effect) {
        this.currentEffects.add(effect);
        effect.apply(this);
    }

    public boolean removeEffect(Effects effect) {
        boolean result = this.currentEffects.remove(effect);
        if (result) {
            Effects.removeAll(this);
            currentEffects.forEach(it -> it.apply(this));
        }
        return result;
    }

    public void clearEffects() {
        this.currentEffects.clear();
        Effects.removeAll(this);
    }

    public List<Effects> getEffects() {
        return this.currentEffects;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        boolean isLeftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean isRightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
        Vector2 bodyPos = box2DBody.getPosition();

        if (isRightPressed && sprite.getX() + sprite.getWidth() < Gdx.graphics.getWidth())
            bodyPos.x += this.paddleSpeed;
        if (isLeftPressed && sprite.getX() > 0)
            bodyPos.x -= this.paddleSpeed;

        box2DBody.setTransform(bodyPos, 0);
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
}
