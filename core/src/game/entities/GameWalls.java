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

    //bottom wall
    Body b2dBottomWall;

    public GameWalls(float screenWidth, float screenHeight, float wallSize, TextureAtlas atlas, World world) {
        generateWalls(screenWidth, screenHeight, wallSize, atlas);
        //generateBox2DWalls(screenWidth, screenHeight, wallSize, world);
        generateBox2SWalls2(screenWidth*PPM_INV, screenHeight*PPM_INV, wallSize*PPM_INV, world);
    }

    public void render(SpriteBatch batch) {
        walls.forEach(it -> it.render(batch));
    }

    public void update(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if (b2dBottomWall.isActive()){
                b2dBottomWall.setActive(false);
            }else{
                b2dBottomWall.setActive(true);
            }
        }
    }

    private void generateWalls(float screenWidth, float screenHeight, float wallSize, TextureAtlas atlas) {
        // top walls
        float topY = screenHeight - wallSize;
        for (float topX = 0f; topX < screenWidth; topX += wallSize) {
            walls.add(new Wall(topX, topY, wallSize, wallSize, atlas));

            // bottom walls
           // walls.add(new Wall(topX, 0, wallSize, wallSize, atlas));
        }

        // left and right walls
        float rightX = screenWidth - wallSize;
        for (float wallY = screenHeight - wallSize; wallY > -wallSize; wallY -= wallSize) {
            walls.add(new Wall(0, wallY, wallSize, wallSize, atlas));
            walls.add(new Wall(rightX, wallY, wallSize, wallSize, atlas));
        }
    }

    private void generateBox2DWalls(float screenWidth, float screenHeight, float wallSize, World world) {
        WallSpawner.spawnChainWall(world,
            new Vector2(wallSize * PPM_INV, wallSize * PPM_INV),
            new Vector2((screenWidth - wallSize) * PPM_INV, wallSize * PPM_INV),
            new Vector2((screenWidth - wallSize) * PPM_INV, (screenHeight - wallSize) * PPM_INV),
            new Vector2(wallSize * PPM_INV, (screenHeight - wallSize) * PPM_INV),
            new Vector2(wallSize * PPM_INV, wallSize * PPM_INV)
        );
    }

    private void generateBox2SWalls2(float worldWidth, float worldHeight, float padding, World world){

        System.out.println("world_width: "+worldWidth+"  world_height: "+worldHeight+"  padding: "+padding);
        Array<Vector2> points = new Array<Vector2>();
        points.add(new Vector2(padding,padding));
        points.add(new Vector2((worldWidth - padding) , padding ));
        points.add(new Vector2((worldWidth - padding), (worldHeight - padding) ));
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
