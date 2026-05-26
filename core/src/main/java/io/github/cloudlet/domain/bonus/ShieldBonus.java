package io.github.cloudlet.domain.bonus;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;

public class ShieldBonus extends Bonus {
    public ShieldBonus(float x, float y) { super(x, y); }
    @Override
    public void onCollect(GameWorld world) {
        world.cancelEnemyEffects();
        world.getEffects().setShield(true);
        world.getEffects().setShieldTimer(GameConstants.SHIELD_DURATION);
    }
}
