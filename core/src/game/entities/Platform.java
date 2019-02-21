package game.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.registry.SpriteRegistry;

public class Platform extends SimpleBox2DEntity {

    private static final int HIT_COUNT_DAMAGED = 1;
    private static final int HIT_COUNT_DELETE = 2;

    private int hitCount = 0;
    private TextureRegion damagedTileRegion;

    public Platform(Body box2DBody, Vector2 size, TextureAtlas atlas, SpriteRegistry tileColor, SpriteRegistry damagedTileColor) {
        super(box2DBody, tileColor, atlas);
        this.damagedTileRegion = damagedTileColor.findRegion(atlas);
    }

    @Override
    public void updateSpriteScale() {
        super.updateSpriteScale();
        this.sprite.setScale(this.sprite.getScaleX() * 0.2f, this.sprite.getScaleY() * 0.2f);
    }

    public void hit(Ball ball) {
        hitCount++;
        if (hitCount >= HIT_COUNT_DELETE) {
            this.isFlaggedForDelete = true;
        } else if (hitCount == HIT_COUNT_DAMAGED) {
            this.sprite.setRegion(damagedTileRegion);
        }
    }
}
