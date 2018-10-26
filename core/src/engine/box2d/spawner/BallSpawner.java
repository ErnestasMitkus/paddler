package engine.box2d.spawner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BallSpawner {

    public static Body spawnBall(Vector2 position, float radius, World world) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(position.x, position.y);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.density = 0.8f;
        fdef.friction = 0;

        Body ball = world.createBody(bdef);
        ball.createFixture(fdef);
        ball.setBullet(true);
        return ball;
    }
}
