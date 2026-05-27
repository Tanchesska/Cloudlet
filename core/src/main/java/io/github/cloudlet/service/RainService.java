package io.github.cloudlet.service;

import java.util.Iterator;
import java.util.Random;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.cloud.Cloud;
import io.github.cloudlet.domain.RainDrop;
import io.github.cloudlet.world.GameWorld;
public class RainService {
    private final Random random = new Random();
    public void pour(Cloud cloud, GameWorld world) {
        float cx = cloud.getPosition().x;
        float cy = cloud.getPosition().y;
        float r  = cloud.getRadius();
        for (int i = 0; i < GameConstants.Rain.DROP_COUNT; i++) {
            float x    = cx + (random.nextFloat() * 2f - 1f) * r;
            float y    = cy - r * 0.2f;
            float velX = (random.nextFloat() * 2f - 1f) * GameConstants.Rain.VEL_X_RANGE;
            float velY = -(GameConstants.Rain.VEL_Y_BASE
                + random.nextFloat() * GameConstants.Rain.VEL_Y_RANGE);
            world.getRainDrops().add(new RainDrop(x, y, velX, velY));
        }
    }
    public void update(float delta, GameWorld world) {
        Iterator<RainDrop> it = world.getRainDrops().iterator();
        while (it.hasNext()) {
            RainDrop rd = it.next();
            rd.update(delta);
            if (rd.getY() < GameConstants.Rain.FLOOR_Y) {
                it.remove();
            }
        }
    }
    public boolean isPouring(GameWorld world) {
        return !world.getRainDrops().isEmpty();
    }
}
