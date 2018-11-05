package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import engine.box2d.spawner.BallSpawner;
import engine.box2d.spawner.PlatformSpawner;
import game.entities.Ball;
import game.entities.GameWalls;
import game.entities.Platform;
import game.entities.SimpleBox2DEntity;
import game.listeners.B2DContactListener;
import game.registry.SpriteRegistry;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_INV;
import static game.B2DVars.PPM_MAT_INV;

public class Level extends ScreenAdapter {

    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 6;

    private World world;
    private Box2DDebugRenderer b2dr;

    private SpriteBatch batch;
    private OrthographicCamera cam;

    private B2DContactListener b2dContactListener;

    private GameWalls gameWalls;
    private Ball ball;
    private List<Platform> platforms;

    public Level(TextureAtlas atlas) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float wallSize = screenWidth / 50f;
        float ballSize = 16f;

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        b2dContactListener = new B2DContactListener();
        b2dContactListener.setAtlas(atlas);
        world.setContactListener(b2dContactListener);

        batch = new SpriteBatch();
        cam = new OrthographicCamera(screenWidth, screenHeight);
        cam.setToOrtho(false, screenWidth * PPM_INV, screenHeight * PPM_INV);

        gameWalls = new GameWalls(screenWidth, screenHeight, wallSize, atlas, world);

        Vector2 ballPosition = new Vector2(screenWidth / 2, screenHeight / 2).mul(PPM_MAT_INV);
        Body ballBody = BallSpawner.spawnBall(ballPosition, ballSize * PPM_INV, world);
        ball = new Ball(ballBody, atlas);
        ballBody.setLinearVelocity(20f, 10f);

        platforms = new ArrayList<>();

        Vector2 platformPosition = new Vector2(screenWidth / 3, screenHeight / 2).mul(PPM_MAT_INV);
        Vector2 platformSize = new Vector2(384 >> 2, 128 >> 2);
        Body platformBody = PlatformSpawner.spawnPlatform(world, platformPosition, platformSize.cpy().mul(PPM_MAT_INV));
        platforms.add(new Platform(platformBody, platformSize, atlas, SpriteRegistry.GREEN_TILE, SpriteRegistry.GREEN_TILE_DAMAGED));
    }

    @Override
    public void render(float delta) {
        update(delta);
        render(batch);
    }

    private void update(float delta) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        // delete all platforms flagged for delete
        sweepDeadBodies();

        ball.update(delta);
    }

    private void render(SpriteBatch batch) {
        batch.begin();
        gameWalls.render(batch);
        ball.render(batch);
        platforms.forEach(it -> it.render(batch));
        batch.end();

        b2dr.render(world, cam.combined);
    }

    public void sweepDeadBodies() {
        Array<Body> bodies = new Array<>(world.getBodyCount());
        world.getBodies(bodies);
        bodies.forEach(it -> {
            if (it.getUserData() instanceof SimpleBox2DEntity) {
                SimpleBox2DEntity entity = (SimpleBox2DEntity) it.getUserData();
                if (entity.isFlaggedForDelete()) {
                    world.destroyBody(it);
                    it.setUserData(null);
                    if (entity instanceof Platform) {
                        platforms.remove(entity);
                    }
                }
            }
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
