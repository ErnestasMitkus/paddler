package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.entities.Wall;

import java.util.ArrayList;
import java.util.List;

public class Level extends ScreenAdapter {

    private List<Wall> walls = new ArrayList<>();

    public Level(TextureAtlas atlas) {
        generateWalls(atlas, Gdx.graphics.getWidth() / 50f);
    }

    public void render(SpriteBatch batch, float delta) {
        walls.forEach(it -> it.render(batch));
    }

    private void generateWalls(TextureAtlas atlas, float wallSize) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // top walls
        float topY = screenHeight - wallSize;
        for (float topX = 0f; topX < screenWidth; topX += wallSize) {
            walls.add(new Wall(topX, topY, wallSize, wallSize, atlas));
        }

        // left and right walls
        float rightX = screenWidth - wallSize;
        for (float wallY = screenHeight - wallSize; wallY > -wallSize; wallY -= wallSize) {
            walls.add(new Wall(0, wallY, wallSize, wallSize, atlas));
            walls.add(new Wall(rightX, wallY, wallSize, wallSize, atlas));
        }
    }
}
