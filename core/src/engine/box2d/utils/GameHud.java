package engine.box2d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import game.Level;
import game.registry.SpriteRegistry;

public class GameHud {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private BitmapFont font;

    private Array<String> msgs;

    private Sprite heartSprite;

    private boolean debugText;

    public GameHud(Level game, TextureAtlas atlas) {
        this.batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ScreenViewport(camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font = new BitmapFont();
        msgs = new Array<>();
        heartSprite = SpriteRegistry.HEART.createSprite(atlas);
        debugText = true;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            debugText = !debugText;
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void draw() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawHearts();
        drawMessages();

        batch.end();
        msgs.clear();
    }

    private void drawHearts() {
        heartSprite.setScale(0.5f, 0.5f);
        for (int i = 0; i < Level.lives; i++) {
            heartSprite.setPosition(10 + (50 * i), Gdx.graphics.getHeight() - 50);
            heartSprite.draw(batch);
        }
    }

    private void drawMessages() {
        if (debugText) {
            for (int i = 0; i < msgs.size; i++) {
                font.draw(batch, msgs.get(i), 10, viewport.getScreenHeight() - 50 - (15 * i));
            }
        }
    }


    public void addMsg(String msg) {
        this.msgs.add(msg);
    }
}
