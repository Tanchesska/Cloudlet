package io.github.cloudlet.service;
import com.badlogic.gdx.math.MathUtils;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Cloud;

public class CloudService {
    public void update(Cloud cloud, float delta) {
        cloud.consumeWater(GameConstants.WATER_DRAIN_PER_SEC * delta);
        updateSize(cloud);
        if (cloud.getRadius() <= GameConstants.CLOUD_MIN_RADIUS + GameConstants.CLOUD_DEAD_RADIUS_THRESHOLD
            && cloud.getWater() <= 0f) {
            cloud.setDead(true);
        }
    }
    public void addWater(Cloud cloud, float amount) {
        cloud.addWater(amount);
        updateSize(cloud);
    }
    public void resetAfterRain(Cloud cloud) {
        cloud.setWater(GameConstants.AUTO_POUR_RESET_WATER);
        updateSize(cloud);
    }
    public void updateSize(Cloud cloud) {
        float r = MathUtils.clamp(
            GameConstants.CLOUD_RADIUS_BASE + cloud.getWater() * GameConstants.CLOUD_RADIUS_FACTOR,
            GameConstants.CLOUD_MIN_RADIUS,
            GameConstants.CLOUD_MAX_RADIUS
        );
        cloud.setRadius(r);
    }
}
