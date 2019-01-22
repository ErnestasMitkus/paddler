package game.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.Level;
import game.registry.SpriteRegistry;

public class Platform extends SimpleBox2DEntity {

    private static final int HIT_COUNT_DAMAGED = 1;
    private static final int HIT_COUNT_DELETE = 2;

    private int hitCount = 0;
    private TextureRegion damagedTileRegion;

    private Level level;

    public Platform(Level level, Body box2DBody, Vector2 size, TextureAtlas atlas, SpriteRegistry tileColor, SpriteRegistry damagedTileColor) {
        super(box2DBody, tileColor, atlas);
        this.damagedTileRegion = damagedTileColor.findRegion(atlas);
        this.level = level;
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
            level.getCamShaker().shake(0.03f, 0.2f);
            level.getParticleRenderer().addExplosion(this.box2DBody.getPosition().x, this.box2DBody.getPosition().y);
        } else if (hitCount == HIT_COUNT_DAMAGED) {
            this.sprite.setRegion(damagedTileRegion);
        }
    }
}
