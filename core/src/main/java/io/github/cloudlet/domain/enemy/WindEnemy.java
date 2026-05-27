package io.github.cloudlet.domain.enemy;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;

public class WindEnemy extends Enemy {
    private float effectTimer = 0f;
    public WindEnemy(float x, float y) { super(x, y); }
    @Override
    public void update(float delta, GameWorld world) {
        if (!active) return;

        if (dying) {
            alpha -= delta * GameConstants.Enemy.DYING_FADE_SPEED;
            if (alpha <= 0f) active = false;
            return;
        }
        float touchDist = world.getCloud().getRadius() + GameConstants.Enemy.HIT_RADIUS;
        if (!triggered && position.dst(world.getCloud().getPosition()) < touchDist) {
            if (!world.getEffects().hasShield()) {
                triggered   = true;
                effectTimer = GameConstants.Enemy.WIND_DURATION;
                world.getEffects().setBlocked(true);
            }
        }
        if (triggered) {
            effectTimer -= delta;
            world.getCloud().getPosition().x -= GameConstants.Enemy.WIND_PUSH_SPEED * delta;
            world.getEffects().setWindRotation(
                world.getEffects().getWindRotation() - GameConstants.Enemy.WIND_ROTATION_SPEED * delta
            );
            if (effectTimer <= 0f) {
                cancelEffect(world);
                startDying();
            }
        }
    }

    @Override
    public void cancelEffect(GameWorld world) {
        world.getEffects().setBlocked(false);
        world.getEffects().setWindRotation(0f);
    }
}
