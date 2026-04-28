package io.github.cloudlet.service;

import io.github.cloudlet.domain.Cloud;

public class CloudService {

    private static final float MIN_SIZE = 25f;
    private static final float MAX_SIZE = 80f;

    public void update(Cloud cloud, float delta) {
        cloud.water -= 5f * delta;
        if (cloud.water < 0) cloud.water = 0;

        updateSize(cloud);
    }

    public void addWater(Cloud cloud, float amount) {
        cloud.water += amount;

        if (cloud.water > 80f) {
            cloud.water = 80f;
        }

        updateSize(cloud);
    }

    private void updateSize(Cloud cloud) {

        cloud.radius = MIN_SIZE + cloud.water * 0.6f;

        if (cloud.radius > MAX_SIZE) {
            cloud.radius = MAX_SIZE;
            cloud.water = (MAX_SIZE - MIN_SIZE) / 0.6f;
        }
    }
}
