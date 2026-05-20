package io.github.cloudlet.service;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Bonus;
import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Drop;

public class BonusService {
    private final List<Bonus> bonuses = new ArrayList<>();
    private final Random random = new Random();
    private float spawnTimer = 0f;
    private float nextSpawn  = GameConstants.BONUS_SPAWN_MIN;
    private boolean magnetActive  = false;
    private float   magnetTimer   = 0f;
    private boolean shieldActive  = false;
    private float   shieldTimer   = 0f;
    private boolean rainbowActive = false;
    private float   rainbowTimer  = 0f;

    public void update(float delta, Cloud cloud, DropService dropService, CloudService cloudService) {
        spawnTimer += delta;
        if (spawnTimer >= nextSpawn) {
            spawnBonus();
            spawnTimer = 0f;
            nextSpawn = GameConstants.BONUS_SPAWN_MIN
                + random.nextFloat() * GameConstants.BONUS_SPAWN_RANGE;
        }

        Iterator<Bonus> iterator = bonuses.iterator();
        while (iterator.hasNext()) {
            Bonus b = iterator.next();
            b.update(delta);

            if (!b.active || b.position.x < -100) {
                iterator.remove();
                continue;
            }
            float touchDist = cloud.radius + GameConstants.BONUS_HIT_RADIUS;
            if (!b.disappearing && b.position.dst(cloud.position) < touchDist) {
                activateBonus(b, cloud, cloudService);
                b.disappearing = true;
            }
        }
        if (magnetActive) {
            magnetTimer -= delta;
            if (magnetTimer <= 0f) {
                magnetActive   = false;
                cloud.magnetTimer = 0f;
            } else {
                cloud.magnetTimer = magnetTimer;
                pullDrops(cloud, dropService, delta);
            }
        }
        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0f) {
                shieldActive   = false;
                cloud.hasShield = false;
            }
        }
        if (rainbowActive) {
            rainbowTimer -= delta;
            if (rainbowTimer <= 0f) {
                rainbowActive      = false;
                cloud.rainbowTimer = 0f;
            } else {
                cloud.rainbowTimer = rainbowTimer;
            }
        }
    }
    private void activateBonus(Bonus b, Cloud cloud, CloudService cloudService) {
        switch (b.type) {

            case MAGNET:
                magnetActive      = true;
                magnetTimer       = GameConstants.MAGNET_DURATION;
                cloud.magnetTimer = magnetTimer;
                break;

            case SHIELD:
                shieldActive   = true;
                shieldTimer    = GameConstants.SHIELD_DURATION;
                cloud.hasShield = true;
                break;

            case RAINBOW:
                cloud.water = Math.min(
                    cloud.water * GameConstants.RAINBOW_WATER_MULT,
                    GameConstants.WATER_MAX
                );
                cloudService.updateSize(cloud);
                rainbowActive      = true;
                rainbowTimer       = GameConstants.RAINBOW_DURATION;
                cloud.rainbowTimer = rainbowTimer;
                break;
        }
    }
    private void pullDrops(Cloud cloud, DropService dropService, float delta) {
        for (Drop drop : dropService.getDrops()) {
            Vector2 dir = new Vector2(
                cloud.position.x - drop.position.x,
                cloud.position.y - drop.position.y
            );
            float dist = dir.len();
            if (dist < GameConstants.MAGNET_PULL_DIST && dist > 0) {
                dir.nor();
                drop.position.mulAdd(dir, GameConstants.MAGNET_PULL_SPEED * delta);
            }
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
    public List<Bonus>  getBonuses()      { return bonuses; }
    public boolean      isShieldActive()  { return shieldActive; }
    public boolean      isMagnetActive()  { return magnetActive; }
    public boolean      isRainbowActive() { return rainbowActive; }
}
