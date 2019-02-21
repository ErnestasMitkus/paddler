import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.Level;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PaddlerGame extends Game {
    private TextureAtlas atlas;
    private Level level;

    @Override
    public void create () {
        atlas = new TextureAtlas("BreakoutTileSetFree/SpriteSheet/Breakout_Tile_Free.atlas");
        level = new Level(atlas);
        this.setScreen(level);
        final FPSLogger fpsLogger = new FPSLogger();
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(fpsLogger::log, 3, 1, TimeUnit.SECONDS);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
        atlas.dispose();
        level.dispose();
    }
}
