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
  Level game;
  SpriteBatch batch;
  OrthographicCamera camera;
  Viewport viewport;

  BitmapFont font;

  //flash messages
  private Array<String> msgs;

  //
  TextureAtlas atlas;
  Sprite spr_heart;

  boolean debugText;

  public GameHud(Level game, TextureAtlas atlas) {
    this.game = game;
    this.batch = game.getBatch();
    this.atlas = atlas;
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    viewport = new ScreenViewport(camera);
    viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    font = new BitmapFont();
    msgs = new Array<>();
    spr_heart = SpriteRegistry.HEART.createSprite(atlas);
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

    //draw hearts
    spr_heart.setScale(0.5f, 0.5f);
    for (int i = 0; i < Level.lives; i++) {
      spr_heart.setPosition(10 + (50 * i), Gdx.graphics.getHeight() - 50);
      spr_heart.draw(batch);
    }

    //draw 'flash' messages
    if (debugText) {
      for (int i = 0; i < msgs.size; i++) {
        font.draw(batch, msgs.get(i), 10, viewport.getScreenHeight() - 50 - (15 * i));
      }
    }

    batch.end();
    msgs.clear();
  }


  public void addMsg(String msg) {
    this.msgs.add(msg);
  }
}
