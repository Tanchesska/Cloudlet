package io.github.cloudlet.service;
import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Flower;
import io.github.cloudlet.domain.RainDrop;
import io.github.cloudlet.world.GameWorld;

public class FlowerService {
    private final ScoreService scoreService;
    public FlowerService(ScoreService scoreService, GameWorld world) {
        this.scoreService = scoreService;
        for (int i = 0; i < GameConstants.Flower.COUNT; i++) {
            float x    = 900f + i * 300f;
            int   type = MathUtils.random(0, 2);
            world.getFlowers().add(new Flower(x, GameConstants.Flower.POS_Y, type));
        }
    }
    public void update(float delta, GameWorld world) {
        for (Flower flower : world.getFlowers()) {
            moveFlower(flower, delta, world.getSpeedMultiplier(), world);
            handleRainCollision(flower, world);
        }
        trackRainMiss(world);
    }
    private void moveFlower(Flower flower, float delta, float speedMultiplier,
                            GameWorld world) {
        flower.getPosition().x -= GameConstants.Flower.SPEED * speedMultiplier * delta;
        flower.getHitbox().x    = flower.getPosition().x - 20f;
        flower.decrementHitCooldown(delta);
        if (flower.getPosition().x < -80f) {
            repositionFlower(flower, world, false);
        }
    }
    private void handleRainCollision(Flower flower, GameWorld world) {
        if (flower.getHitCooldown() > 0f) return;

        Iterator<RainDrop> it = world.getRainDrops().iterator();
        while (it.hasNext()) {
            RainDrop drop = it.next();
            if (!flower.getHitbox().contains(drop.getX(), drop.getY())) continue;

            it.remove();
            flower.setHitCooldown(GameConstants.Flower.HIT_COOLDOWN);
            flower.incrementWaterings();
            scoreService.addFlowerScore(
                flower.getWaterings(),
                flower.getPosition().x,
                flower.getPosition().y + 50f
            );
            growFlower(flower, world);
            break;
        }
    }
    private void trackRainMiss(GameWorld world) {
        boolean anyDropActive = !world.getRainDrops().isEmpty();
        if (!anyDropActive) return;

        boolean anyHit = false;
        for (Flower f : world.getFlowers()) {
            if (f.getHitCooldown() >= GameConstants.Flower.HIT_COOLDOWN * 0.9f) {
                anyHit = true;
                break;
            }
        }
        if (!anyHit) {
            world.getGameStats().currentFlowerNoMiss = false;
        }
    }

    private void growFlower(Flower flower, GameWorld world) {
        switch (flower.getWaterings()) {
            case 1: flower.setStage(1); break;
            case 2: flower.setStage(2); break;
            case 3: flower.setStage(3); break;
            case GameConstants.Flower.MAX_WATERINGS:
                if (world.getGameStats().currentFlowerNoMiss) {
                    world.getGameStats().pacifistAchieved = true;
                }
                repositionFlower(flower, world, true);
                break;
        }
    }

    private void repositionFlower(Flower flower, GameWorld world, boolean resetStage) {
        float farthestX = getFarthestX(world);
        float dist      = GameConstants.Flower.MIN_DIST
            + MathUtils.random(GameConstants.Flower.EXTRA_DIST);
        flower.reposition(farthestX + dist);
        if (resetStage) {
            flower.setStage(0);
            flower.resetWaterings();
            flower.setType(MathUtils.random(0, 2));
            world.getGameStats().currentFlowerNoMiss = true;
        }
    }

    private float getFarthestX(GameWorld world) {
        float max = 0f;
        for (Flower f : world.getFlowers()) {
            if (f.getPosition().x > max) max = f.getPosition().x;
        }
        return max;
    }
}
