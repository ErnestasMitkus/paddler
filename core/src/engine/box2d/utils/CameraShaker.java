package engine.box2d.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class CameraShaker {
    private float time;
    private float currentTime;
    private float power;
    private float currentPower;
    private Random random;
    private Vector3 originalPos;
    private Vector3 pos;
    private OrthographicCamera camera;
    private boolean shaking;

    public CameraShaker(OrthographicCamera camera) {
        this.camera = camera;
        pos = new Vector3();
        random = new Random();
        time = 0;
        currentTime = 0;
        power = 0;
        currentPower = 0;
    }

    public void shake(float shakePower, float shakeLength) {
        power = shakePower;
        time = shakeLength;
        currentTime = 0;
        if (shaking == false) {
            originalPos = new Vector3(camera.position);
        }
        shaking = true;
    }

    public void update(float delta) {
        if (currentTime <= time && shaking) {
            camera.position.set(originalPos);
            currentPower = power * ((time - currentTime) / time);
            pos.x = (random.nextFloat() - 0.5f) * 2 * currentPower;
            pos.y = (random.nextFloat() - 0.5f) * 2 * currentPower;
            camera.translate(pos);
            currentTime += delta;
        } else if (currentTime > time && shaking) {
            currentTime = 0;
            time = 0;
            shaking = false;
            camera.position.set(originalPos);
        }

    }
}
