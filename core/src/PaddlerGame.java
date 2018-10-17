import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.Level;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PaddlerGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Sprite sprite;
    private Sprite sprite2;
    private Sprite sprite3;
    private TextureAtlas atlas;
    private Level level;

    @Override
    public void create () {
        batch = new SpriteBatch();
        atlas = new TextureAtlas("BreakoutTileSetFree/SpriteSheet/Breakout_Tile_Free.atlas");
        level = new Level(atlas);

        sprite = atlas.createSprite("Star");
        sprite.setPosition(200f, 200f);
        sprite2 = atlas.createSprite("Heart");
        sprite2.setPosition(300f, 200f);
        sprite3 = atlas.createSprite("Ball");
        sprite3.setPosition(400f, 200f);

        final FPSLogger fpsLogger = new FPSLogger();
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(fpsLogger::log, 3, 1, TimeUnit.SECONDS);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        level.render(batch, Gdx.graphics.getDeltaTime());
        sprite.draw(batch);
        sprite2.draw(batch);
        sprite3.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        atlas.dispose();
    }
}
