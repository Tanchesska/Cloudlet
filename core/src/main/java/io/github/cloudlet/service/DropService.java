package io.github.cloudlet.service;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Drop;

public class DropService {
    private final ArrayList<Drop> drops  = new ArrayList<>();
    private final Random          random = new Random();
    private float spawnTimer = 0f;
    public ArrayList<Drop> getDrops() {
        return drops;
    }
    public void update(float delta, Cloud cloud, CloudService cloudService, float speedMultiplier) {
        spawnTimer += delta;
        float spawnInterval = Math.max(0.15f,
            GameConstants.DROP_SPAWN_INTERVAL / speedMultiplier);
        if (spawnTimer > spawnInterval) {
            spawnTimer = 0;
            spawnDrop();
        }
        Iterator<Drop> iterator = drops.iterator();
        while (iterator.hasNext()) {
            Drop drop = iterator.next();
            drop.position.x -= GameConstants.DROP_SPEED * speedMultiplier * delta;
            float dx   = drop.position.x - cloud.position.x;
            float dy   = drop.position.y - cloud.position.y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist < cloud.radius + drop.radius) {
                float water = waterForType(drop.type);
                cloudService.addWater(cloud, water);
                iterator.remove();
                continue;
            }
            if (drop.position.x < 0) {
                iterator.remove();
            }
        }
    }
    private void spawnDrop() {
        float roll = random.nextFloat();
        Drop.DropType type;
        if (roll < GameConstants.CLOUD_DROP_CHANCE) {
            type = Drop.DropType.MINI_CLOUD;
        } else if (roll < GameConstants.CLOUD_DROP_CHANCE + GameConstants.SPECIAL_DROP_CHANCE) {
            type = Drop.DropType.SPECIAL;
        } else {
            type = Drop.DropType.NORMAL;
        }
        float y = MathUtils.random(200, 480);
        drops.add(new Drop(820, y, type));
    }
    private float waterForType(Drop.DropType type) {
        switch (type) {
            case SPECIAL:   return GameConstants.WATER_PER_SPECIAL_DROP;
            case MINI_CLOUD: return GameConstants.WATER_PER_CLOUD_DROP;
            default:        return GameConstants.WATER_PER_DROP;
        }
    }
}
