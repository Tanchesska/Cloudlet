package io.github.cloudlet.service;

import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyService {

    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();

    private float spawnTimer = 0f;
    private float nextSpawn = 6f;

    public void update(
        float delta,
        Cloud cloud,
        BonusService bonusService
    ) {


        enemies.removeIf(enemy ->
            !enemy.active || enemy.position.x < -120
        );


        for (Enemy e : enemies) {

            e.position.x -= 120f * delta;

            if (!bonusService.isShieldActive()) {

                e.update(delta, cloud);
            }
        }


        spawnTimer += delta;

        if (spawnTimer >= nextSpawn) {

            spawnEnemy();

            spawnTimer = 0f;

            nextSpawn = 6f + random.nextFloat() * 6f;
        }
    }

    private void spawnEnemy() {

        Enemy.Type type = Enemy.Type.values()[
            random.nextInt(
                Enemy.Type.values().length
            )
            ];

        float x = 850f;

        float y = 140 + random.nextInt(260);

        enemies.add(
            new Enemy(type, x, y)
        );
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
