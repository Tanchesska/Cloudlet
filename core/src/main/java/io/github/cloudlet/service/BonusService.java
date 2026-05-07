package io.github.cloudlet.service;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.cloudlet.domain.Bonus;
import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Drop;

public class BonusService {

    private final List<Bonus> bonuses = new ArrayList<>();

    private final Random random = new Random();

    private float spawnTimer = 0f;
    private float nextSpawn = 18f;

    private boolean magnetActive = false;
    private float magnetTimer = 0f;

    private boolean shieldActive = false;
    private float shieldTimer = 0f;

    private float rainbowTimer = 0f;

    public void update(
        float delta,
        Cloud cloud,
        DropService dropService
    ) {


        spawnTimer += delta;

        if (spawnTimer >= nextSpawn) {

            spawnBonus();

            spawnTimer = 0f;

            nextSpawn = 15f + random.nextFloat() * 10f;
        }


        Iterator<Bonus> iterator = bonuses.iterator();

        while (iterator.hasNext()) {

            Bonus b = iterator.next();

            b.update(delta);

            if (!b.active || b.position.x < -100) {
                iterator.remove();
                continue;
            }

            if (b.position.dst(cloud.position) < cloud.radius) {

                activateBonus(b, cloud);

                b.disappearing = true;
            }
        }

        if (magnetActive) {

            magnetTimer -= delta;

            if (magnetTimer <= 0f) {
                magnetActive = false;
            }

            for (Drop drop : dropService.getDrops()) {

                Vector2 dir = new Vector2(
                    cloud.position.x - drop.position.x,
                    cloud.position.y - drop.position.y
                );

                float dist = dir.len();

                if (dist < 250f) {

                    dir.nor();

                    drop.position.mulAdd(dir, 350f * delta);
                }
            }

            cloud.radius = 90f;
        }
        else {

            if (rainbowTimer <= 0f) {
                cloud.radius = 45f;
            }
        }

        if (shieldActive) {

            shieldTimer -= delta;

            if (shieldTimer <= 0f) {
                shieldActive = false;
            }
        }


        if (rainbowTimer > 0f) {

            rainbowTimer -= delta;

            if (rainbowTimer <= 0f && !magnetActive) {
                cloud.radius = 45f;
            }
        }
    }

    private void activateBonus(Bonus b, Cloud cloud) {

        switch (b.type) {

            case MAGNET:

                magnetActive = true;

                magnetTimer = 5f;

                cloud.radius = 90f;

                break;


            case SHIELD:

                shieldActive = true;

                shieldTimer = 5f;

                break;


            case RAINBOW:

                cloud.radius *= 2f;

                rainbowTimer = 1f;

                if (cloud.radius >= 90f) {
                    cloud.water = 100f;
                }

                break;
        }
    }

    private void spawnBonus() {

        Bonus.Type type = Bonus.Type.values()[
            random.nextInt(Bonus.Type.values().length)
            ];

        float x = 900 + random.nextInt(300);

        float y = 140 + random.nextInt(260);

        bonuses.add(new Bonus(type, x, y));
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public boolean isMagnetActive() {
        return magnetActive;
    }
}
