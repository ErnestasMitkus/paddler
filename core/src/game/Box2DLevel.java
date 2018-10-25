package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class Box2DLevel extends ScreenAdapter {

    private static final int PPM = 100;

    private static final int VELOCITY_ITERATIONS = 100;
    private static final int POSITION_ITERATIONS = 60;

    private OrthographicCamera camera;
    private Box2DDebugRenderer b2dr;

    private World world;

    private Body ball;

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, (float) (Gdx.graphics.getWidth() / PPM), (float) (Gdx.graphics.getHeight() / PPM));

        world = new World(new Vector2(0, -9.81f), true);
        b2dr = new Box2DDebugRenderer();
        init();
    }

    private void init() {
        // Platform
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM), 100f / PPM);

        Body platform = world.createBody(bdef);

        bdef.angle = (float) (-30f / 180 * Math.PI);
        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM) - 250f / PPM, 160f / PPM);
        Body platform2 = world.createBody(bdef);
        bdef.angle = (float) (30f / 180 * Math.PI);
        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM) + 250f / PPM, 160f / PPM);
        Body platform3 = world.createBody(bdef);

        PolygonShape rectShape = new PolygonShape();
        rectShape.setAsBox(150f / PPM, 10f / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = rectShape;

        platform.createFixture(fdef);
        platform2.createFixture(fdef);
        platform3.createFixture(fdef);

        // Ball
        bdef.type = BodyDef.BodyType.DynamicBody;
//        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM), 300f / PPM);

//        ball = world.createBody(bdef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(15f / PPM);
        fdef.shape = circleShape;

        MassData md = new MassData();

        for (int i = 0; i < 200; i++) {
            fdef.restitution = 0.6f;
            fdef.density = 7850f;
            float x = new Random().nextFloat() * ((float) Gdx.graphics.getWidth() / PPM);
            float y = new Random().nextFloat() * ((float) Gdx.graphics.getHeight() / PPM + 200f / PPM) + 200f / PPM;
            bdef.position.set(x, y);

            md.mass = 2;
            md.I = md.mass * circleShape.getRadius() * circleShape.getRadius();

            Body ballXY = world.createBody(bdef);
            ballXY.createFixture(fdef);
            ballXY.setMassData(md);
        }


        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM), 5000f / PPM);
        ball = world.createBody(bdef);

        fdef.restitution = 0.6f;
        fdef.density = 7850f;
        circleShape.setRadius(100f / PPM);

        md.mass = 50;
        md.I = md.mass * circleShape.getRadius() * circleShape.getRadius();

        ball.setMassData(md);
        ball.createFixture(fdef);


//        fdef.restitution = new Random().nextFloat() / 3 * 2 + 0.33f; // -> 0.33-1
//        fdef.restitution = 1; // -> 0.33-1
//        fdef.density = 0.7f;

//        ball.createFixture(fdef);
//        ball.applyForceToCenter(0.5f, 300f, true);

//        bdef.position.set((float) ((Gdx.graphics.getWidth() / 2) / PPM) + 0.03f, 350f / PPM);
//        Body ball2 = world.createBody(bdef);
//        ball2.createFixture(fdef);
    }

    @Override
    public void render(float delta) {
        // update
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        // render
        b2dr.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }
}
