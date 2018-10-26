package game;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class B2DVars {

    public static final float PPM = 100;
    public static final float PPM_INV = 1/PPM;
    public static final Vector2 PPM_VEC = new Vector2(PPM, PPM);
    public static final Vector2 PPM_VEC_INV = new Vector2(1/PPM, 1/PPM);
    public static final Matrix3 PPM_MAT = new Matrix3(new float[]{PPM, 0, 0, 0, PPM, 0, 0, 0, PPM});
    public static final Matrix3 PPM_MAT_INV = new Matrix3(new float[]{1/PPM, 0, 0, 0, 1/PPM, 0, 0, 0, 1/PPM});

}
