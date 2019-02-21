package game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import engine.box2d.spawner.WallSpawner;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_INV;

public class GameWalls {

    private List<Wall> walls = new ArrayList<>();

    private Body b2dBottomWall;

    public GameWalls(float worldWidth, float worldHeight, float wallSize, TextureAtlas atlas, World world) {
        generateWalls(worldWidth, worldHeight, wallSize, atlas);
        generateBox2SWalls(worldWidth, worldHeight, wallSize, world);
    }

    public void render(SpriteBatch batch) {
        walls.forEach(it -> it.render(batch));

    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            b2dBottomWall.setActive(!b2dBottomWall.isActive());
        }
    }

    private void generateWalls(float WorldWidth, float WorldHeight, float wallSize, TextureAtlas atlas) {
        // top walls
        float topY = WorldHeight - wallSize;
        for (float topX = 0f; topX < WorldWidth; topX += wallSize) {
            walls.add(new Wall(topX, topY, wallSize, wallSize, atlas));
        }

        // left and right walls
        float rightX = WorldWidth - wallSize;
        for (float wallY = WorldHeight - wallSize; wallY > -wallSize; wallY -= wallSize) {
            walls.add(new Wall(0, wallY, wallSize, wallSize, atlas));
            walls.add(new Wall(rightX, wallY, wallSize, wallSize, atlas));
        }
    }

    private void generateBox2SWalls(float worldWidth, float worldHeight, float padding, World world) {
        Array<Vector2> points = new Array<>();

        points.add(new Vector2(padding, padding));
        points.add(new Vector2((worldWidth - padding), padding));
        points.add(new Vector2((worldWidth - padding), (worldHeight - padding)));
        points.add(new Vector2(padding, (worldHeight - padding)));
        points.add(new Vector2(padding, padding));

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1f;
        fdef.density = 0.8f;
        fdef.friction = 0;

        EdgeShape edgeShape = new EdgeShape();
        fdef.shape = edgeShape;
        Body walls = world.createBody(bdef);

        for (int i = 2; i < points.size; i++) {
            edgeShape.set(points.get(i - 1), points.get(i));
            walls.createFixture(fdef);
        }

        //create bottom wall
        b2dBottomWall = world.createBody(bdef);
        edgeShape.set(points.get(0), points.get(1));
        b2dBottomWall.createFixture(fdef);
    }

}
