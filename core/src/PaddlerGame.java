import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.Box2DLevel;
import game.Level;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PaddlerGame extends ApplicationAdapter {
    private TextureAtlas atlas;
    private Level level;

    @Override
    public void create () {
        atlas = new TextureAtlas("BreakoutTileSetFree/SpriteSheet/Breakout_Tile_Free.atlas");
        level = new Level(atlas);

        final FPSLogger fpsLogger = new FPSLogger();
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(fpsLogger::log, 3, 1, TimeUnit.SECONDS);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose () {
        atlas.dispose();
        level.dispose();
    }
}
