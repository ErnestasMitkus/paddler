package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import static game.B2DVars.PPM_INV;

public class Wall {

    private static final String WALL_SPRITE_NAME = "Gray-Tile-Small";

    private Sprite sprite;

    public Wall(float x, float y, float width, float height, TextureAtlas atlas) {
        this.sprite = generateWallSprite(x, y, width, height, atlas);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private Sprite generateWallSprite(float x, float y, float width, float height, TextureAtlas atlas) {
        Sprite sprite = atlas.createSprite(WALL_SPRITE_NAME);
       // sprite.setScale((width / sprite.getWidth())*PPM_INV, (height / sprite.getHeight())*PPM_INV);
        sprite.setScale(PPM_INV*0.2f, PPM_INV*0.2f);
        sprite.setOrigin(0, 0);
        sprite.setPosition(x, y);
        return sprite;
    }
}
