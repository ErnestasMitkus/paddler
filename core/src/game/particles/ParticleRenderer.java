package game.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import game.Level;

import static game.B2DVars.PPM_INV;

public class ParticleRenderer {

    private Level level;
    private TextureAtlas atlas;

    ///
    OrthographicCamera camera;
    SpriteBatch batch;
    //Viewport viewport;

    //Particle Effects
    ParticleEffect sparkleEffect;

    //tile explosions
    ParticleEffectPool explosionPool;
    Array<PooledEffect> explosionEffects = new Array<>();
    ParticleEffect explosionEffect;

    //mouse position
    Vector3 mousePos = new Vector3();
    Vector3 mousePosWorld = new Vector3();

    public ParticleRenderer(Level level, TextureAtlas atlas) {
        this.level = level;
        this.atlas = atlas;
        camera = level.getCamera();
        batch = level.getBatch();

        //init, laod and start ball sparkle particle effect
        sparkleEffect = new ParticleEffect();
        sparkleEffect.load(Gdx.files.internal("Particles/SparkleEmitter.p"), Gdx.files.internal("Particles"));
        sparkleEffect.scaleEffect(PPM_INV);
        sparkleEffect.start();

        //load explosion effect and create a pool of f***ing explosions. Hell YEAH!
        explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal("Particles/TileExplosion.p"), Gdx.files.internal("Particles"));
        explosionEffect.scaleEffect(PPM_INV);
        explosionPool = new ParticleEffectPool(explosionEffect, 2, 5);
    }

    public void update(float delta) {
        sparkleEffect.setPosition(level.getBall().getBox2DBody().getPosition().x, level.getBall().getBox2DBody().getPosition().y);
    }

    public void render() {
        //draw ball sparkle effect
        sparkleEffect.draw(batch, Gdx.graphics.getDeltaTime());

        //draw explosions, if any
        for (PooledEffect effect : explosionEffects) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
            if (effect.isComplete()) {
                effect.free();
                explosionEffects.removeValue(effect, true);
            }
        }
    }

    public void addExplosion(float x, float y) {
        PooledEffect explosion = explosionPool.obtain();
        explosion.setPosition(x, y);
        explosionEffects.add(explosion);
        explosion.start();
        System.out.println("addind explosion: " + explosion);
    }


    public void dispose() {
        System.out.println("ParticleRenderer is disposing of its junk.");
        sparkleEffect.dispose();
        explosionEffect.dispose();
    }
}
