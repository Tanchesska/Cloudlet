package io.github.cloudlet.service;

import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Cloud;

public class CloudService {
    public void update(Cloud cloud, float delta) {
        cloud.water -= GameConstants.WATER_DRAIN_PER_SEC * delta;
        if (cloud.water < 0) {
            cloud.water = 0;
        }
        updateSize(cloud);
        if (cloud.radius <= cloud.minRadius + 0.5f && cloud.water <= 0) {
            cloud.dead = true;
        }
    }
    public void addWater(Cloud cloud, float amount) {
        cloud.water += amount;
        if (cloud.water > GameConstants.WATER_MAX) {
            cloud.water = GameConstants.WATER_MAX;
        }
        updateSize(cloud);
    }
    public void resetAfterRain(Cloud cloud) {
        cloud.water = GameConstants.AUTO_POUR_RESET_WATER;
        updateSize(cloud);
    }
    public void updateSize(Cloud cloud) {
        cloud.radius = GameConstants.CLOUD_RADIUS_BASE + cloud.water * GameConstants.CLOUD_RADIUS_FACTOR;
        if (cloud.radius < cloud.minRadius) {
            cloud.radius = cloud.minRadius;
        }
        if (cloud.radius > cloud.maxRadius) {
            cloud.radius = cloud.maxRadius;
        }
    }
}
