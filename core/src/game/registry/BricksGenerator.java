package game.registry;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.Level;
import game.entities.Platform;

import java.util.ArrayList;
import java.util.List;

import static game.B2DVars.PPM_INV;
import static game.B2DVars.PPM_MAT;
import static game.B2DVars.PPM_MAT_INV;

public class BricksGenerator {

    public static List<Platform> generateBricksList(Level level, World world, TextureAtlas atlas, float rows, float columns, float positionX, float positionY) {
        float posX = positionX * PPM_INV;
        float posY = positionY * PPM_INV;
        Vector2 platformSize = new Vector2(SpriteRegistry.BLUE_TILE.getSizeX() / 5f, SpriteRegistry.BLUE_TILE.getSizeY() / 5f).mul(PPM_MAT_INV);
        List<Platform> bricksList = new ArrayList<>();

        for (float i = 0; i < columns * platformSize.x; i += platformSize.x) {
            bricksList.add(createPlatform(level, new Vector2(posX + i, posY), platformSize, world, atlas));
            for (float j = 0; j < rows * platformSize.y; j += platformSize.y) {
                bricksList.add(createPlatform(level, new Vector2(posX + i, posY - j), platformSize, world, atlas));
            }
        }
        return bricksList;
    }

    private static Platform createPlatform(Level level, Vector2 platformPosition, Vector2 platformSize, World world, TextureAtlas atlas) {
        Body platformBody = PlatformSpawner.spawnPlatform(world, platformPosition, platformSize);
        return new Platform(level, platformBody, platformSize.cpy().mul(PPM_MAT), atlas, SpriteRegistry.BLUE_TILE, SpriteRegistry.BLUE_TILE_DAMAGED);
    }
}
