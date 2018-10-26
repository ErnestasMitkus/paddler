package engine.box2d.spawner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlatformSpawner {

    public static Body spawnPlatform(World world, Vector2 position, Vector2 size) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(position);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1f;
        fdef.density = 0.8f;
        fdef.friction = 0;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);
        fdef.shape = shape;

        Body walls = world.createBody(bdef);
        walls.createFixture(fdef);
        return walls;
    }
}
