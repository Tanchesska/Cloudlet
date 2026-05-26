package io.github.cloudlet.domain.enemy;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;
public class StormEnemy extends Enemy {
    private float blockTimer = 0f;
    public StormEnemy(float x, float y) { super(x, y); }
    @Override
    public void update(float delta, GameWorld world) {
        if (!active) return;

        if (dying) {
            alpha -= delta * GameConstants.ENEMY_DYING_FADE_SPEED;
            if (alpha <= 0f) active = false;
            return;
        }
        float touchDist = world.getCloud().getRadius() + GameConstants.ENEMY_HIT_RADIUS;
        if (!triggered && position.dst(world.getCloud().getPosition()) < touchDist) {
            if (!world.getEffects().hasShield()) {
                triggered  = true;
                blockTimer = GameConstants.STORM_BLOCK_DURATION;
            }
        }
        if (triggered) {
            if (blockTimer > 0f) {
                blockTimer -= delta;
                world.getEffects().setBlocked(true);
            } else {
                cancelEffect(world);
                startDying();
            }
        }
    }

    @Override
    public void cancelEffect(GameWorld world) {
        world.getEffects().setBlocked(false);
    }
}
