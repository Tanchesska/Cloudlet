package io.github.cloudlet.service;
import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import java.util.Random;
import io.github.cloudlet.biome.Biome;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.cloud.Cloud;
import io.github.cloudlet.domain.Drop;
import io.github.cloudlet.world.GameWorld;
public class DropService {
    private final CloudService cloudService;
    private final Random       random     = new Random();
    private float              spawnTimer = 0f;
    public DropService(CloudService cloudService) {
        this.cloudService = cloudService;
    }
    public void update(float delta, GameWorld world) {
        Cloud cloud           = world.getCloud();
        float speedMultiplier = world.getSpeedMultiplier();
        Biome biome           = world.getBiomeManager().getCurrentBiome();

        spawnTimer += delta;
        float spawnInterval = Math.max(0.15f,
            GameConstants.Drop.SPAWN_INTERVAL / speedMultiplier);
        if (spawnTimer > spawnInterval) {
            spawnTimer = 0f;
            world.getDrops().add(spawnDrop(biome));
        }

        Iterator<Drop> it = world.getDrops().iterator();
        while (it.hasNext()) {
            Drop drop = it.next();
            drop.getPosition().x -= GameConstants.Drop.SPEED * speedMultiplier * delta;

            float dx   = drop.getPosition().x - cloud.getPosition().x;
            float dy   = drop.getPosition().y - cloud.getPosition().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < cloud.getRadius() + drop.getRadius()) {
                handleCollect(drop, world);
                it.remove();
                continue;
            }
            if (drop.getPosition().x < 0f) {
                it.remove();
            }
        }

        if (world.getEffects().isInverted()) {
            float t = world.getEffects().getInvertedTimer() - delta;
            world.getEffects().setInvertedTimer(Math.max(0f, t));
        }
    }
    private void handleCollect(Drop drop, GameWorld world) {
        switch (drop.getType()) {
            case ACID:
                world.getEffects().setInvertedTimer(GameConstants.Drop.ACID_INVERT_DURATION);
                break;
            case MINI_CLOUD:
                cloudService.addWater(world.getCloud(), GameConstants.Drop.WATER_CLOUD);
                world.getGameStats().miniCloudsCollected++;
                break;
            default:
                cloudService.addWater(world.getCloud(), waterForType(drop.getType()));
                break;
        }
    }

    private Drop spawnDrop(Biome biome) {
        float roll = random.nextFloat();
        Drop.DropType type;

        float acidChance = (biome != null) ? biome.getAcidDropChance() : 0f;
        if (roll < acidChance) {
            type = Drop.DropType.ACID;
        } else if (roll < acidChance + GameConstants.Drop.CLOUD_CHANCE) {
            type = Drop.DropType.MINI_CLOUD;
        } else if (roll < acidChance + GameConstants.Drop.CLOUD_CHANCE + GameConstants.Drop.SPECIAL_CHANCE) {
            type = Drop.DropType.SPECIAL;
        } else {
            type = Drop.DropType.NORMAL;
        }

        float y = MathUtils.random(200f, 480f);
        return new Drop(820f, y, type);
    }

    private float waterForType(Drop.DropType type) {
        switch (type) {
            case SPECIAL:    return GameConstants.Drop.WATER_SPECIAL;
            case MINI_CLOUD: return GameConstants.Drop.WATER_CLOUD;
            default:         return GameConstants.Drop.WATER_NORMAL;
        }
    }
}
