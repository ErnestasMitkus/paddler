package game.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import engine.box2d.spawner.PlatformSpawner;
import game.entities.Platform;
import java.util.ArrayList;
import java.util.List;
import static game.B2DVars.PPM_MAT_INV;

public class BricksGenerator {

    public BricksGenerator(){
    }

    List<Platform> bricksList;

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();






    public List<Platform> generateBricksList(World world, TextureAtlas atlas, float rows, float columns, float positionX, float positionY){
        Vector2 platformSize = new Vector2(384 >> 2, 128 >> 2);
        bricksList = new ArrayList<>();
        for(int i = 0; i < columns * 96; i += 96 ){
            Vector2 platformPosition = new Vector2(positionX + i , positionY).mul(PPM_MAT_INV);
            Body platformBody = PlatformSpawner.spawnPlatform(world, platformPosition, platformSize.cpy().mul(PPM_MAT_INV));
            bricksList.add(new Platform(platformBody, platformSize, atlas));
            for(int j = 0; j < rows * 32; j += 32){
                Vector2 platformPosition1 = new Vector2(positionX + i , positionY - j).mul(PPM_MAT_INV);
                Body platformBody1 = PlatformSpawner.spawnPlatform(world, platformPosition1, platformSize.cpy().mul(PPM_MAT_INV));
                bricksList.add(new Platform(platformBody1, platformSize, atlas));
            }
        }
        return bricksList;
    }

}
