package io.github.cloudlet.service;
import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;
import java.util.Random;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.cloud.Cloud;
import io.github.cloudlet.domain.cloud.CloudEffects;
import io.github.cloudlet.domain.Drop;
import io.github.cloudlet.domain.bonus.Bonus;
import io.github.cloudlet.domain.bonus.MagnetBonus;
import io.github.cloudlet.domain.bonus.RainbowBonus;
import io.github.cloudlet.domain.bonus.ShieldBonus;
import io.github.cloudlet.world.GameWorld;

public class BonusService {
    private final Random random     = new Random();
    private float        spawnTimer = 0f;
    private float        nextSpawn  = GameConstants.Bonus.SPAWN_MIN;

    public void update(float delta, GameWorld world) {
        Cloud        cloud   = world.getCloud();
        CloudEffects effects = world.getEffects();

        spawnTimer += delta;
        if (spawnTimer >= nextSpawn) {
            world.getBonuses().add(spawnBonus());
            spawnTimer = 0f;
            nextSpawn  = GameConstants.Bonus.SPAWN_MIN
                + random.nextFloat() * GameConstants.Bonus.SPAWN_RANGE;
        }

        Iterator<Bonus> it = world.getBonuses().iterator();
        while (it.hasNext()) {
            Bonus b = it.next();
            b.update(delta);

            if (!b.isActive() || b.getPosition().x < -GameConstants.Bonus.OFFSCREEN_X) {
                it.remove();
                continue;
            }
            float touchDist = cloud.getRadius() + GameConstants.Bonus.HIT_RADIUS;
            if (!b.isDisappearing()
                && b.getPosition().dst(cloud.getPosition()) < touchDist) {
                b.onCollect(world);
                b.setDisappearing(true);
            }
        }

        tickMagnet(delta, world);
        tickShield(delta, effects);
        tickRainbow(delta, effects);
    }

    private void tickMagnet(float delta, GameWorld world) {
        CloudEffects effects = world.getEffects();
        if (effects.getMagnetTimer() > 0f) {
            effects.setMagnetTimer(effects.getMagnetTimer() - delta);
            if (effects.getMagnetTimer() <= 0f) {
                effects.setMagnetTimer(0f);
            } else {
                pullDrops(world, delta);
            }
        }
    }
    private void tickShield(float delta, CloudEffects effects) {
        if (effects.hasShield()) {
            effects.setShieldTimer(effects.getShieldTimer() - delta);
            if (effects.getShieldTimer() <= 0f) {
                effects.setShield(false);
                effects.setShieldTimer(0f);
            }
        }
    }

    private void tickRainbow(float delta, CloudEffects effects) {
        if (effects.getRainbowTimer() > 0f) {
            effects.setRainbowTimer(effects.getRainbowTimer() - delta);
            if (effects.getRainbowTimer() <= 0f) {
                effects.setRainbowTimer(0f);
            }
        }
    }

    private void pullDrops(GameWorld world, float delta) {
        Cloud cloud = world.getCloud();
        for (Drop drop : world.getDrops()) {
            Vector2 dir = new Vector2(
                cloud.getPosition().x - drop.getPosition().x,
                cloud.getPosition().y - drop.getPosition().y
            );
            float dist = dir.len();
            if (dist < GameConstants.Bonus.MAGNET_PULL_DIST && dist > 0f) {
                dir.nor();
                drop.getPosition().mulAdd(dir, GameConstants.Bonus.MAGNET_PULL_SPEED * delta);
            }
        }
    }

    private Bonus spawnBonus() {
        float x   = 900 + random.nextInt(300);
        float y   = 140 + random.nextInt(260);
        int   idx = random.nextInt(3);
        switch (idx) {
            case 0:  return new MagnetBonus(x, y);
            case 1:  return new ShieldBonus(x, y);
            default: return new RainbowBonus(x, y);
        }
    }
}
