package io.github.cloudlet.domain.bonus;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.world.GameWorld;
public class RainbowBonus extends Bonus {

    public RainbowBonus(float x, float y) { super(x, y); }
    @Override
    public void onCollect(GameWorld world) {
        float extra = world.getCloud().getWater() * (GameConstants.Bonus.RAINBOW_WATER_MULT - 1f);
        world.getCloud().addWater(extra);
        world.getEffects().setRainbowTimer(GameConstants.Bonus.RAINBOW_DURATION);
    }
}
