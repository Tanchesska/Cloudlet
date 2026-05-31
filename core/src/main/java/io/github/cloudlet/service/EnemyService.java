package io.github.cloudlet.service;
import java.util.List;
import java.util.Random;
import io.github.cloudlet.biome.Biome;
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
    private float        nextSpawn  = GameConstants.Enemy.SPAWN_MIN;
    public void update(float delta, GameWorld world) {
        List<Enemy> enemies = world.getEnemies();
        enemies.removeIf(e -> !e.isActive()
            || e.getPosition().x < -GameConstants.Enemy.OFFSCREEN_X);

        for (Enemy e : enemies) {
            e.getPosition().x -= GameConstants.Enemy.SPEED * delta;
            e.update(delta, world);
        }
        spawnTimer += delta;
        if (spawnTimer >= nextSpawn) {
            Biome biome = world.getBiomeManager().getCurrentBiome();
            enemies.add(spawnEnemy(biome));
            spawnTimer = 0f;
            nextSpawn  = GameConstants.Enemy.SPAWN_MIN
                + random.nextFloat() * GameConstants.Enemy.SPAWN_RANGE;
        }
    }
    private Enemy spawnEnemy(Biome biome) {
        float x = 850f;
        float y = 140 + random.nextInt(260);

        int sunWeight = (biome != null) ? biome.getSunSpawnWeight() : 1;

        int total = sunWeight + 3;
        int roll  = random.nextInt(total);

        if (roll < sunWeight)            return new SunEnemy(x, y);
        if (roll == sunWeight)           return new WindEnemy(x, y);
        if (roll == sunWeight + 1)       return new BirdEnemy(x, y);
        return new StormEnemy(x, y);
    }
}
