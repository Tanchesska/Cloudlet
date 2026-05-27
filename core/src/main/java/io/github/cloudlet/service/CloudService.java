package io.github.cloudlet.service;
import com.badlogic.gdx.math.MathUtils;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.cloud.Cloud;

public class CloudService {
    public void update(Cloud cloud, float delta) {
        cloud.consumeWater(GameConstants.Cloud.WATER_DRAIN_PER_SEC * delta);
        updateSize(cloud);
        if (cloud.getRadius() <= GameConstants.Cloud.MIN_RADIUS + GameConstants.Cloud.DEAD_RADIUS_THRESHOLD
            && cloud.getWater() <= 0f) {
            cloud.setDead(true);
        }
    }
    public void addWater(Cloud cloud, float amount) {
        cloud.addWater(amount);
        updateSize(cloud);
    }
    public void resetAfterRain(Cloud cloud) {
        cloud.setWater(GameConstants.Cloud.AUTO_POUR_RESET_WATER);
        updateSize(cloud);
    }
    public void updateSize(Cloud cloud) {
        float r = MathUtils.clamp(
            GameConstants.Cloud.RADIUS_BASE + cloud.getWater() * GameConstants.Cloud.RADIUS_FACTOR,
            GameConstants.Cloud.MIN_RADIUS,
            GameConstants.Cloud.MAX_RADIUS
        );
        cloud.setRadius(r);
    }
}
