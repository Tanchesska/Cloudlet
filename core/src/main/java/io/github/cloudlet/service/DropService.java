package io.github.cloudlet.service;
import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import java.util.Random;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Cloud;
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

        spawnTimer += delta;
        float spawnInterval = Math.max(0.15f,
            GameConstants.DROP_SPAWN_INTERVAL / speedMultiplier);
        if (spawnTimer > spawnInterval) {
            spawnTimer = 0f;
            world.getDrops().add(spawnDrop());
        }
        Iterator<Drop> it = world.getDrops().iterator();
        while (it.hasNext()) {
            Drop drop = it.next();
            drop.getPosition().x -= GameConstants.DROP_SPEED * speedMultiplier * delta;

            float dx   = drop.getPosition().x - cloud.getPosition().x;
            float dy   = drop.getPosition().y - cloud.getPosition().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < cloud.getRadius() + drop.getRadius()) {
                cloudService.addWater(cloud, waterForType(drop.getType()));
                it.remove();
                continue;
            }
            if (drop.getPosition().x < 0f) {
                it.remove();
            }
        }
    }
    private Drop spawnDrop() {
        float roll = random.nextFloat();
        Drop.DropType type;
        if (roll < GameConstants.CLOUD_DROP_CHANCE) {
            type = Drop.DropType.MINI_CLOUD;
        } else if (roll < GameConstants.CLOUD_DROP_CHANCE + GameConstants.SPECIAL_DROP_CHANCE) {
            type = Drop.DropType.SPECIAL;
        } else {
            type = Drop.DropType.NORMAL;
        }
        float y = MathUtils.random(200f, 480f);
        return new Drop(820f, y, type);
    }
    private float waterForType(Drop.DropType type) {
        switch (type) {
            case SPECIAL:    return GameConstants.WATER_PER_SPECIAL_DROP;
            case MINI_CLOUD: return GameConstants.WATER_PER_CLOUD_DROP;
            default:         return GameConstants.WATER_PER_DROP;
        }
    }
}
