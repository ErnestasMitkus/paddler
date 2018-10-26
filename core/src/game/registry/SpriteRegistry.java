package game.registry;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum SpriteRegistry {

    BLUE_TILE("Blue-Tile", 384, 128),
    BLUE_TILE_DAMAGED("Blue-Tile-Damaged", 384, 128),
    BLUE_TILE_SMALL("Blue-Tile-Small", 128, 128),
    LIGHT_GREEN_TILE("Light-Green-Tile", 384, 128),
    LIGHT_GREEN_TILE_DAMAGED("Light-Green-Tile-Damaged", 384, 128),
    LIGHT_GREEN_TILE_SMALL("Light-Green-Tile-Small", 128, 128),
    PURPLE_TILE("Purple-Tile", 384, 128),
    PURPLE_TILE_DAMAGED("Purple-Tile-Damaged", 384, 128),
    PURPLE_TILE_SMALL("Purple-Tile-Small", 128, 128),
    RED_TILE("Red-Tile", 384, 128),
    RED_TILE_DAMAGED("Red-Tile-Damaged", 384, 128),
    RED_TILE_SMALL("Red-Tile-Small", 128, 128),
    ORANGE_TILE("Orange-Tile", 384, 128),
    ORANGE_TILE_DAMAGED("Orange-Tile-Damaged", 384, 128),
    ORANGE_TILE_SMALL("Orange-Tile-Small", 128, 128),
    CYAN_TILE("Cyan-Tile", 384, 128),
    CYAN_TILE_DAMAGED("Cyan-Tile-Damaged", 384, 128),
    CYAN_TILE_SMALL("Cyan-Tile-Small", 128, 128),
    YELLOW_TILE("Yellow-Tile", 384, 128),
    YELLOW_TILE_DAMAGED("Yellow-Tile-Damaged", 384, 128),
    YELLOW_TILE_SMALL("Yellow-Tile-Small", 128, 128),
    GREEN_TILE("Green-Tile", 384, 128),
    GREEN_TILE_DAMAGED("Green-Tile-Damaged", 384, 128),
    GREEN_TILE_SMALL("Green-Tile-Small", 128, 128),
    GRAY_TILE("Gray-Tile", 384, 128),
    GRAY_TILE_DAMAGED("Gray-Tile-Damaged", 384, 128),
    GRAY_TILE_SMALL("Gray-Tile-Small", 128, 128),
    BROWN_TILE("Brown-Tile", 384, 128),
    BROWN_TILE_DAMAGED("Brown-Tile-Damaged", 384, 128),
    BROWN_TILE_SMALL("Brown-Tile-Small", 128, 128),

    PADDLE_ELECTRIC_1("Paddle-Electric-1", 243, 64),
    PADDLE_ELECTRIC_2("Paddle-Electric-2", 243, 64),
    PADDLE_ELECTRIC_3("Paddle-Electric-3", 243, 64),
    PADDLE_ELECTRIC_4("Paddle-Electric-4", 243, 64),
    PADDLE_ELECTRIC_5("Paddle-Electric-5", 243, 64),
    PADDLE_ELECTRIC_6("Paddle-Electric-6", 243, 64),
    BALL("Ball", 64, 64),
    STAR("Star", 64, 61),
    HEART("Heart", 64, 58),
    PELLET("Pellet", 10, 21)
    ;

    private final String spriteName;
    private final int sizeX;
    private final int sizeY;

    SpriteRegistry(String spriteName, int sizeX, int sizeY) {
        this.spriteName = spriteName;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Sprite createSprite(TextureAtlas atlas) {
        return atlas.createSprite(spriteName);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
