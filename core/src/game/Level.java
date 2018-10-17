package game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

public class Level extends ScreenAdapter {

    private List<Sprite> spriteList = new ArrayList<>();

    public Level(TextureAtlas atlas) {
        spriteList.add(atlas.createSprite("Gray-Tile-Small"));
        spriteList.get(0).setScale(0.5f, 0.5f);
        // TODO: generate sprites that surround left, top and right border of the game screen
    }

    public void render(SpriteBatch batch, float delta) {
        spriteList.get(0).draw(batch);
        // TODO: render sprites
    }

}
