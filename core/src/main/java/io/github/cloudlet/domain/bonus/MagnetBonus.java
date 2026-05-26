package io.github.cloudlet.domain.bonus;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;

public class MagnetBonus extends Bonus {

    public MagnetBonus(float x, float y) { super(x, y); }
    @Override
    public void onCollect(GameWorld world) {
        world.getEffects().setMagnetTimer(GameConstants.MAGNET_DURATION);
    }
}
