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
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import game.entities.Ball;
import game.entities.Wall;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM;

public class Level extends ScreenAdapter {

    private static final int VELOCITY_ITERATIONS = 106;
    private static final int POSITION_ITERATIONS = 102;

    private final float wallSize;
    private final float ballSize;

    private World world;
    private Box2DDebugRenderer b2dr;

    private SpriteBatch batch;
    private OrthographicCamera cam;

    private List<Wall> walls = new ArrayList<>();
    private Ball ball;
    private Body ballBody;

    public Level(TextureAtlas atlas) {
        wallSize = Gdx.graphics.getWidth() / 50f;
        ballSize = 16f;

        generateWalls(atlas, wallSize);
        ball = new Ball(Gdx.graphics.getWidth() >> 1, Gdx.graphics.getHeight() >> 1, ballSize, atlas);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(ball.getPosition().x / PPM, ball.getPosition().y / PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(ballSize / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.density = 0.8f;
        fdef.friction = 0;

        ballBody = world.createBody(bdef);
        ballBody.createFixture(fdef);
        ballBody.setBullet(true);

        ball.setBox2DBody(ballBody);
        ballBody.setUserData(ball);

        // walls
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);

        EdgeShape edgeShape = new EdgeShape();
        fdef.shape = edgeShape;
        Body walls = world.createBody(bdef);


        // bottom
        edgeShape.set(wallSize / PPM, wallSize / PPM, (Gdx.graphics.getWidth() - wallSize) / PPM, wallSize / PPM);
        walls.createFixture(fdef);

        // left
        edgeShape.set(wallSize / PPM, wallSize / PPM, wallSize / PPM, (Gdx.graphics.getHeight() - wallSize) / PPM);
        walls.createFixture(fdef);

        // right
        edgeShape.set((Gdx.graphics.getWidth() - wallSize) / PPM, wallSize / PPM, (Gdx.graphics.getWidth() - wallSize) / PPM, (Gdx.graphics.getHeight() - wallSize) / PPM);
        walls.createFixture(fdef);

        // left
        edgeShape.set(wallSize / PPM, (Gdx.graphics.getHeight() - wallSize) / PPM, (Gdx.graphics.getWidth() - wallSize) / PPM, (Gdx.graphics.getHeight() - wallSize) / PPM);
        walls.createFixture(fdef);

        ballBody.applyForce(500f, 300f, ballBody.getWorldCenter().x, ballBody.getWorldCenter().y, true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        render(batch);
    }

    private void update(float delta) {
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        ball.update(delta);
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
