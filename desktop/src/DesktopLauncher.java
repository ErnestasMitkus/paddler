import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = config.width * 9 / 16;
        config.foregroundFPS = 0;
        config.backgroundFPS = 10;
        config.vSyncEnabled = false;
		new LwjglApplication(new PaddlerGame(), config);
	}
}
