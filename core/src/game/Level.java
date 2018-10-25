package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import static game.B2DVars.PPM;

public class Level extends ScreenAdapter {

    private static final int VELOCITY_ITERATIONS = 106;
    private static final int POSITION_ITERATIONS = 102;

    private World world;
    private Box2DDebugRenderer b2dr;

    private SpriteBatch batch;
    private OrthographicCamera cam;

    public Level(TextureAtlas atlas) {
        world = new World(new Vector2(0, -9.81f), true);
        b2dr = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);

    }

    @Override
    public void render(float delta) {
        update(delta);
        render(batch);
    }

    private void update(float delta) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    private void render(SpriteBatch batch) {
        b2dr.render(world, cam.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
