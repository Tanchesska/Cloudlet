package io.github.cloudlet.service;
import java.util.List;
import java.util.Random;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.enemy.BirdEnemy;
import io.github.cloudlet.domain.enemy.Enemy;
import io.github.cloudlet.domain.enemy.StormEnemy;
import io.github.cloudlet.domain.enemy.SunEnemy;
import io.github.cloudlet.domain.enemy.WindEnemy;
import io.github.cloudlet.world.GameWorld;

public class EnemyService {
    private final Random random     = new Random();
    private float        spawnTimer = 0f;
    private float        nextSpawn  = GameConstants.ENEMY_SPAWN_MIN;
    public void update(float delta, GameWorld world) {
        List<Enemy> enemies = world.getEnemies();
        enemies.removeIf(e -> !e.isActive()
            || e.getPosition().x < -GameConstants.ENEMY_OFFSCREEN_X);

        for (Enemy e : enemies) {
            e.getPosition().x -= GameConstants.ENEMY_SPEED * delta;
            e.update(delta, world);
        }
        spawnTimer += delta;
        if (spawnTimer >= nextSpawn) {
            enemies.add(spawnEnemy());
            spawnTimer = 0f;
            nextSpawn  = GameConstants.ENEMY_SPAWN_MIN
                + random.nextFloat() * GameConstants.ENEMY_SPAWN_RANGE;
        }
    }
    private Enemy spawnEnemy() {
        float x   = 850f;
        float y   = 140 + random.nextInt(260);
        int   idx = random.nextInt(4);
        switch (idx) {
            case 0:  return new SunEnemy(x, y);
            case 1:  return new WindEnemy(x, y);
            case 2:  return new BirdEnemy(x, y);
            default: return new StormEnemy(x, y);
        }
    }
}
