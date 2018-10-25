package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import game.entities.Ball;
import game.entities.Wall;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM;

public class Level extends ScreenAdapter {

    private static final int VELOCITY_ITERATIONS = 106;
    private static final int POSITION_ITERATIONS = 102;

    private World world;
    private Box2DDebugRenderer b2dr;

    private SpriteBatch batch;
    private OrthographicCamera cam;

    private List<Wall> walls = new ArrayList<>();
    private Ball ball;

    private Body boxx;

    public Level(TextureAtlas atlas) {
        generateWalls(atlas, Gdx.graphics.getWidth() / 50f);
        ball = new Ball(Gdx.graphics.getWidth() >> 1, Gdx.graphics.getHeight() >> 1, atlas);

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
        batch.begin();
        walls.forEach(it -> it.render(batch));
        ball.render(batch);
        batch.end();

        b2dr.render(world, cam.combined);
    }

    private void generateWalls(TextureAtlas atlas, float wallSize) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // top walls
        float topY = screenHeight - wallSize;
        for (float topX = 0f; topX < screenWidth; topX += wallSize) {
            walls.add(new Wall(topX, topY, wallSize, wallSize, atlas));

            // bottom walls
            walls.add(new Wall(topX, 0, wallSize, wallSize, atlas));
        }

        // left and right walls
        float rightX = screenWidth - wallSize;
        for (float wallY = screenHeight - wallSize; wallY > -wallSize; wallY -= wallSize) {
            walls.add(new Wall(0, wallY, wallSize, wallSize, atlas));
            walls.add(new Wall(rightX, wallY, wallSize, wallSize, atlas));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
