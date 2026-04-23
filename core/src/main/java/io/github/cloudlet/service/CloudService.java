package io.github.cloudlet.service;

import io.github.cloudlet.domain.Cloud;

public class CloudService {

    public void update(Cloud cloud, float delta) {
        cloud.water -= 5f * delta;
        if (cloud.water < 0) cloud.water = 0;

        updateSize(cloud);
    }

    public void addWater(Cloud cloud, float amount) {
        cloud.water += amount;
        updateSize(cloud);
    }

    private void updateSize(Cloud cloud) {
        cloud.radius = 20 + cloud.water;
    }
}
