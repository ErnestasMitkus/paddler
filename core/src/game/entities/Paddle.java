package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.registry.SpriteRegistry;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM;
import static game.B2DVars.PPM_MAT_INV;

public class Paddle extends SimpleBox2DEntity {
    private static final float DEFAULT_PADDLE_SPPEED = 0.1f;

    private float paddleSpeed = DEFAULT_PADDLE_SPPEED;
    private SpriteRegistry spriteRegistry = SpriteRegistry.PADDLE_ELECTRIC_1;
    private List<Effects> currentEffects = new ArrayList<>();

    public Paddle(World world, TextureAtlas atlas) {
        sprite = spriteRegistry.createSprite(atlas);
        Vector2 posVector = new Vector2(Gdx.graphics.getWidth() / 2f, spriteRegistry.getSizeY() / 2f).mul(PPM_MAT_INV);

        box2DBody = generateBody(world, posVector, spriteRegistry.getSizeX() / PPM / 2, spriteRegistry.getSizeY() / PPM / 2);
        box2DBody.setUserData(this);
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


    private Body generateBody(World world, Vector2 vector, float width, float height) {

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(vector.x, vector.y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;

        Body body = world.createBody(bdef);
        body.createFixture(fdef);
        body.setBullet(true);
        return body;
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
