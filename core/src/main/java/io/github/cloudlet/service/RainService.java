package io.github.cloudlet.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.RainDrop;

public class RainService {
    private final List<RainDrop> drops = new ArrayList<>();
    private final Random random = new Random();
    public void pour(Cloud cloud) {
        float cx = cloud.position.x;
        float cy = cloud.position.y;
        float r = cloud.radius;
        for (int i = 0; i < 20; i++) {
            float x = cx + (random.nextFloat() * 2 - 1) * r;
            float y = cy - r * 0.2f;
            float velX = (random.nextFloat() * 2 - 1) * 30f;
            float velY = -(350f + random.nextFloat() * 150f);
            drops.add(new RainDrop(x, y, velX, velY));
        }
    }
    public void update(float delta) {
        Iterator<RainDrop> it = drops.iterator();
        while (it.hasNext()) {
            RainDrop rd = it.next();
            rd.x += rd.velX * delta;
            rd.y += rd.velY * delta;
            if (rd.y < 130f) {
                it.remove();
            }
        }
    }
    public boolean isPouring() {
        return !drops.isEmpty();
    }
    public List<RainDrop> getDrops() {
        return drops;
    }
}
