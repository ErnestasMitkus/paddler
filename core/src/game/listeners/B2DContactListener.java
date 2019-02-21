package game.listeners;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import engine.box2d.utils.CastUtils;
import game.Level;
import game.entities.Ball;
import game.entities.Platform;

import java.util.Random;

public class B2DContactListener implements ContactListener {

    private Random random;
    private float randomFactor;
    private Level level;

    public B2DContactListener(Level level) {
        this.level = level;
        this.random = new Random();
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        Ball ball = CastUtils.findFirstOfClass(Ball.class, userDataA, userDataB);
        Platform platform = CastUtils.findFirstOfClass(Platform.class, userDataA, userDataB);

        if (ball != null) {
            randomFactor = 3f;
            float degrees = MathUtils.radiansToDegrees * (MathUtils.atan2(ball.getBox2DBody().getLinearVelocity().y, ball.getBox2DBody().getLinearVelocity().x));
            if (Math.abs(degrees) > 175 || Math.abs(degrees) < 5) {
                randomFactor = 20f;
            }

            float angle = -randomFactor + (randomFactor * random.nextFloat());
            ball.rotateDirectionByAngle(angle);
        }

        if (ball != null && platform != null) {
            platform.hit(ball);
            if (platform.isFlaggedForDelete()) {
                level.getCamShaker().shake(0.03f, 0.2f);
                level.getParticleRenderer().addExplosion(platform.getPosition().x, platform.getPosition().y);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
