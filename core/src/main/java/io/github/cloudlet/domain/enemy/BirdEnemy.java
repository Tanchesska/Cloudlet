package io.github.cloudlet.domain.enemy;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;

public class BirdEnemy extends Enemy {
    public BirdEnemy(float x, float y) { super(x, y); }
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
                triggered = true;
                world.getCloud().consumeWater(GameConstants.Enemy.BIRD_WATER_STEAL);
                startDying();
            }
        }
    }
}
