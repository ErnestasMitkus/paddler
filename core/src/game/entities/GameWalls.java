package game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.WallSpawner;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_INV;

public class GameWalls {

    private List<Wall> walls = new ArrayList<>();

    public GameWalls(float screenWidth, float screenHeight, float wallSize, TextureAtlas atlas, World world) {
        generateWalls(screenWidth, screenHeight, wallSize, atlas);
        generateBox2DWalls(screenWidth, screenHeight, wallSize, world);
    }

    public void render(SpriteBatch batch) {
        walls.forEach(it -> it.render(batch));
    }

    private void generateWalls(float screenWidth, float screenHeight, float wallSize, TextureAtlas atlas) {
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

    private void generateBox2DWalls(float screenWidth, float screenHeight, float wallSize, World world) {
        WallSpawner.spawnChainWall(world,
            new Vector2(wallSize * PPM_INV, wallSize * PPM_INV),
            new Vector2((screenWidth - wallSize) * PPM_INV, wallSize * PPM_INV),
            new Vector2((screenWidth - wallSize) * PPM_INV, (screenHeight - wallSize) * PPM_INV),
            new Vector2(wallSize * PPM_INV, (screenHeight - wallSize) * PPM_INV),
            new Vector2(wallSize * PPM_INV, wallSize * PPM_INV)
        );
    }

}
