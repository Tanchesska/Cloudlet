package io.github.cloudlet.domain.enemy;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;
public class SunEnemy extends Enemy {
    private float effectTimer = 0f;
    public SunEnemy(float x, float y) { super(x, y); }
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
                effectTimer = GameConstants.Enemy.SUN_EFFECT_DURATION;
            }
        }
        if (triggered) {
            effectTimer -= delta;
            world.getCloud().consumeWater(GameConstants.Enemy.SUN_WATER_DRAIN_PER_SEC * delta);
            if (effectTimer <= 0f) startDying();
        }
    }
}
