package engine.box2d.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ShapeUtils {

    public static <T> T getShape(Body body, Class<T> clazz) {
        for (Fixture fixture : body.getFixtureList()) {
            if (clazz.isInstance(fixture.getShape())) {
                return clazz.cast(fixture.getShape());
            }
        }
        return null;
    }

}
