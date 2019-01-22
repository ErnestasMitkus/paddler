package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import engine.box2d.spawner.BallSpawner;
import engine.box2d.utils.CameraShaker;
import engine.box2d.utils.GameHud;
import game.entities.Ball;
import game.entities.GameWalls;
import game.entities.Paddle;
import game.entities.Paddle.Effects;
import game.entities.Platform;
import game.entities.SimpleBox2DEntity;
import game.listeners.B2DContactListener;
import game.particles.ParticleRenderer;
import game.registry.BricksGenerator;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_INV;
import static game.B2DVars.PPM_MAT_INV;

public class Level extends ScreenAdapter {

    private static final int VELOCITY_ITERATIONS = 50;
    private static final int POSITION_ITERATIONS = 30;
    private static final Vector2 OFFSET_BALL_PADDLE = new Vector2(0, 35).mul(PPM_MAT_INV);
    private static final Vector2 INITIAL_BALL_VELOCITY = new Vector2(0, 4f);
    private static final float WORLD_WIDTH = 13f;
    private static final float WORLD_HEIGHT = 7.31f;
    private final Paddle paddle;
    //shouldn't be static. oh well
    public static int lives = 5;
    public static int score = 0;

    private World world;
    private Box2DDebugRenderer b2dr;
    private TextureAtlas atlas;

    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Viewport viewport;

    private B2DContactListener b2dContactListener;

    private GameWalls gameWalls;
    private Ball ball;
    private List<Platform> platforms;

    private ParticleRenderer particleRenderer;
    private CameraShaker camShaker;
    //game HUD
    private GameHud hud;

    private final float ballSize = 12f;
    private boolean ballAttached; // don't check ball collision while attached
    private boolean drawDebugShapes = true;

    public Level(TextureAtlas atlas) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float wallSize = (screenWidth / 50f)*PPM_INV;
        float ballSize = 12f;

        world = new World(new Vector2(0, 0f), true);
        b2dr = new Box2DDebugRenderer();
        this.atlas = atlas;

        b2dContactListener = new B2DContactListener();
        world.setContactListener(b2dContactListener);

        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2, 0);
        viewport.apply();

        gameWalls = new GameWalls(WORLD_WIDTH, WORLD_HEIGHT, wallSize, atlas, world);

        paddle = new Paddle(world, atlas);
        paddle.addEffect(Effects.Speed);

        platforms = new ArrayList<>();
        platforms.addAll(BricksGenerator.generateBricksList(this, world, atlas, 3, 12, 200, 600));

        hud = new GameHud(this, atlas);

        spawnBall();
        particleRenderer = new ParticleRenderer(this, atlas);
        camShaker = new CameraShaker(cam);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2, 0);
        hud.resize(width, height);
    }

    @Override
    public void render(float delta) {
        update(delta);
        render(batch);
    }

    private void update(float delta) {
        // keyboard inputs
        updateInputs(delta);

        // game logic
        world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        removeOutOfBoundsBalls();
        if (ball == null) {
            spawnBall();
        }
        if (ballAttached) {
            updateAttachedBall();
        }

        cam.update();
        // delete all platforms flagged for delete
        sweepDeadBodies();
        ball.update(delta);
        paddle.update(delta);
        gameWalls.update(delta);
        particleRenderer.update(delta);
        hud.update(delta);
        camShaker.update(delta);
    }

    private void updateInputs(float delta) {
        // Update paddle

        // acceleration:
        // track acc vector per updates
        // input adds/subtracts from acc vector
        // bound it to a max speed`
        // if no input, multiply acc vector by slowdown percentage
        // if Math.abs(acc vector) is lower than some value and input not pressed -> acc vector = 0

        Vector2 bodyPos = paddle.getBox2DBody().getPosition();
        float paddleMovSpeed = 10f;

        int vecX = 0;
        vecX += Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1 : 0;
        vecX += Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0;

        if (vecX != 0) {
            paddle.getBox2DBody().setTransform(bodyPos.add(vecX * delta * paddleMovSpeed, 0), paddle.getBox2DBody().getAngle());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            drawDebugShapes = !drawDebugShapes;
        }
    }

    private void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        gameWalls.render(batch);
        ball.render(batch);
        paddle.render(batch);
        platforms.forEach(it -> it.render(batch));
        particleRenderer.render();
        batch.end();
        if (drawDebugShapes){
            b2dr.render(world, cam.combined);
        }
        hud.addMsg("FPS: "+Gdx.graphics.getFramesPerSecond());
        hud.addMsg("Lives: "+lives);
        hud.addMsg("Score: "+score);
        hud.addMsg("Bodies: "+world.getBodyCount());
        hud.draw();
    }

    private void updateAttachedBall() {
        ball.getBox2DBody().setTransform(paddle.getBox2DBody().getPosition().add(OFFSET_BALL_PADDLE), ball.getBox2DBody().getAngle());

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ballAttached = false;
            ball.getBox2DBody().setLinearVelocity(INITIAL_BALL_VELOCITY);
        }
    }

    private void removeOutOfBoundsBalls() {
        if (ball.getBox2DBody().getPosition().y < 0) {
            ball.delete();
            ball = null;
            Level.lives -= 1;
        }
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

    private void spawnBall() {
        ballAttached = true;

        Vector2 ballPosition = paddle.getPosition();
        Body ballBody = BallSpawner.spawnBall(ballPosition, ballSize * PPM_INV, world);
        ball = new Ball(ballBody, atlas);

    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera(){
        return cam;
    }

    public GameHud getHud(){
        return hud;
    }

    public Ball getBall() {
        return ball;
    }

    public CameraShaker getCamShaker(){
        return this.camShaker;
    }

    public ParticleRenderer getParticleRenderer() {
        return particleRenderer;
    }

    @Override
    public void dispose() {
        particleRenderer.dispose();
        batch.dispose();
    }
}
