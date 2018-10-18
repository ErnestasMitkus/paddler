package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
        sprite.setScale(width / sprite.getWidth(), height / sprite.getHeight());
        sprite.setOrigin(0, 0);
        sprite.setPosition(x, y);
        return sprite;
    }
}
