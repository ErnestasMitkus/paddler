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
//        cam.translate(Gdx.graphics.getWidth() >> 1, Gdx.graphics.getHeight() >> 1);
        cam.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);
//        cam.update();

        // Platform
        BodyDef bdef = new BodyDef();
        bdef.position.set((Gdx.graphics.getWidth() >> 1) / PPM, 100f / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body platform = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(235f / PPM, 10f / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        platform.createFixture(fdef);

        fdef.density = 0.7f;
        shape.setAsBox(20f / PPM, 15f / PPM);
        bdef.position.set(((Gdx.graphics.getWidth() >> 1) - 15f) / PPM, 120f / PPM);
        Body block = world.createBody(bdef);
        block.createFixture(fdef);

        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set((Gdx.graphics.getWidth() >> 1) / PPM, 140f / PPM);
        shape.setAsBox(200f / PPM, 3f / PPM);
        Body plat = world.createBody(bdef);
        plat.createFixture(fdef);

        fdef.friction = 0.4f;

        // Box
        bdef.position.set(((Gdx.graphics.getWidth() >> 1) + 150) / PPM, 450f / PPM);
        shape.setAsBox(30f / PPM, 10f / PPM);
        fdef.restitution = 0.8f;

        Body box = world.createBody(bdef);
        boxx = box;
        box.createFixture(fdef);
//        https://en.wikipedia.org/wiki/List_of_moments_of_inertia
        MassData massData = new MassData();
        massData.mass = 1f;
        massData.I = 1/12f * ((30f / PPM) * (30f / PPM) + (10f / PPM) * (10f / PPM));
        box.setMassData(massData);
//        boxx.applyAngularImpulse(100f, true);

        // Circle
        bdef.position.set(((Gdx.graphics.getWidth() >> 1) - 130f) / PPM, 300f / PPM);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(15f / PPM);

        fdef.restitution = 0.3f;
        fdef.shape = cshape;

        Body circle = world.createBody(bdef);
        circle.createFixture(fdef);
        massData.mass = 3f;
        massData.I = 3 * (15f / PPM) * (15f / PPM);
        circle.setMassData(massData);
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
//        walls.forEach(it -> it.render(batch));
//        ball.render(batch);

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
