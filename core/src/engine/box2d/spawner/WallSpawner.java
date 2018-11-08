package engine.box2d.spawner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class WallSpawner {

    public static Body spawnChainWall(World world, Vector2... points) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(0, 0);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1f;
        fdef.density = 0.8f;
        fdef.friction = 0;

        EdgeShape edgeShape = new EdgeShape();
        fdef.shape = edgeShape;
        Body walls = world.createBody(bdef);

        for (int i = 1; i < points.length; i++) {
            edgeShape.set(points[i - 1], points[i]);
            walls.createFixture(fdef);
        }
        edgeShape.dispose();
        return walls;
    }

}
