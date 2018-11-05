package game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.registry.SpriteRegistry;

public class Platform extends SimpleBox2DEntity {
    private boolean isDamaged;
    private int hitCount = 0;
    private SpriteRegistry damagedTileColor;
    private int hitCountDamaged = 1;
    private int hitCountDeleted = 2;

    public Platform(Body box2DBody, Vector2 size, TextureAtlas atlas, SpriteRegistry tileColor, SpriteRegistry damagedTileColor) {
        super(box2DBody, tileColor, atlas);
        setDamagedTileColor(damagedTileColor);
        sprite.setScale(size.x / sprite.getWidth(), size.y / sprite.getHeight());
    }

    public void hit(Ball ball, TextureAtlas atlas) {
        updateHitCount();
        updateTexture(atlas);
    }

    private void updateTexture(TextureAtlas atlas) {
        if (isDamaged) {
            Sprite damagedTexture = this.damagedTileColor.createSprite(atlas);
            this.sprite.setRegion(damagedTexture);
        }
    }

    private void updateHitCount() {
        this.hitCount += 1;
        if (this.hitCount == hitCountDamaged) {
            this.isDamaged = true;
        } else if (this.hitCount == hitCountDeleted ) {
            this.isFlaggedForDelete = true;
        } else {
            this.isFlaggedForDelete = false;
            this.isDamaged = false;
            this.hitCount = 0;
        }
    }

    public void setDamagedTileColor(SpriteRegistry damagedTileColor) {
        this.damagedTileColor = damagedTileColor;
    }
}
