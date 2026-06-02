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
            alpha -= delta * GameConstants.Enemy.DYING_FADE_SPEED;
            if (alpha <= 0f) active = false;
            return;
        }

        float touchDist = world.getCloud().getRadius() + GameConstants.Enemy.HIT_RADIUS;
        if (!triggered && position.dst(world.getCloud().getPosition()) < touchDist) {
            if (!world.getEffects().hasShield()) {
                triggered  = true;
                blockTimer = GameConstants.Enemy.STORM_BLOCK_DURATION;
            } else {
                world.getGameStats().consecutiveStormsNoShield = 0;
                startDying();
            }
        }

        if (triggered) {
            if (blockTimer > 0f) {
                blockTimer -= delta;
                world.getEffects().setBlocked(true);
            } else {
                world.getGameStats().consecutiveStormsNoShield++;
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
