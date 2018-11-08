package game.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import engine.box2d.utils.CastUtils;
import game.entities.Ball;
import game.entities.Platform;

public class B2DContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (userDataA != null && userDataB != null) {
            Ball ball = CastUtils.findFirstOfClass(Ball.class, userDataA, userDataB);
            Platform platform = CastUtils.findFirstOfClass(Platform.class, userDataA, userDataB);

            if (ball != null && platform != null) {
                platform.hit(ball);
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
