package engine.box2d.utils;

public class CastUtils {

    public static <T> T findFirstOfClass(Class<T> clazz, Object... objects) {
        for (Object object : objects) {
            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
        }
        return null;
    }

}
