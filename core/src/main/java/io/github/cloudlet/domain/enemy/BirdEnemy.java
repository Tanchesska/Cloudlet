package io.github.cloudlet.domain.enemy;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;

public class BirdEnemy extends Enemy {
    public BirdEnemy(float x, float y) { super(x, y); }
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
                triggered = true;
                world.getCloud().consumeWater(GameConstants.BIRD_WATER_STEAL);
                startDying();
            }
        }
    }
}
