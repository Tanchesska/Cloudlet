package io.github.cloudlet.service;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;

import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Drop;

public class DropService {

    private ArrayList<Drop> drops = new ArrayList<>();

    private float spawnTimer = 0;

    public ArrayList<Drop> getDrops() {
        return drops;
    }

    public void update(float delta, Cloud cloud, CloudService cloudService) {

        spawnTimer += delta;

        if (spawnTimer > 0.5f) {
            spawnTimer = 0;

            float x = 820;
            float y = MathUtils.random(200, 480);

            drops.add(new Drop(x, y));
        }

        Iterator<Drop> iterator = drops.iterator();

        while (iterator.hasNext()) {
            Drop drop = iterator.next();

            drop.position.x -= 200 * delta;

            float dx = drop.position.x - cloud.position.x;
            float dy = drop.position.y - cloud.position.y;

            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < cloud.radius) {
                cloudService.addWater(cloud, 5f);
                iterator.remove();
                continue;
            }

            if (drop.position.x < 0) {
                iterator.remove();
            }
        }
    }
}
