package io.github.cloudlet.service;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Flower;
import io.github.cloudlet.domain.RainDrop;

public class FlowerService {
    private final List<Flower> flowers = new ArrayList<>();
    private final ScoreService scoreService;
    public FlowerService(ScoreService scoreService) {
        this.scoreService = scoreService;
        for (int i = 0; i < GameConstants.FLOWER_COUNT; i++) {
            float x    = 900 + i * 300;
            int   type = MathUtils.random(0, 2);
            flowers.add(new Flower(x, GameConstants.FLOWER_Y, type));
        }
    }
    public List<Flower> getFlowers() {
        return flowers;
    }
    public void update(float delta, RainService rainService, float speedMultiplier) {
        for (Flower flower : flowers) {
            moveFlower(flower, delta, speedMultiplier);
            handleRainCollision(flower, rainService);
        }
    }
    private void moveFlower(Flower flower, float delta, float speedMultiplier) {
        flower.position.x -= GameConstants.FLOWER_SPEED * speedMultiplier * delta;
        flower.hitbox.x    = flower.position.x - 20;
        flower.hitCooldown -= delta;

        if (flower.position.x < -80) {
            repositionFlower(flower, false);
        }
    }
    private void repositionFlower(Flower flower, boolean resetStage) {
        float farthestX = getFarthestX();
        float dist       = GameConstants.FLOWER_MIN_DIST
            + MathUtils.random(GameConstants.FLOWER_EXTRA_DIST);

        flower.position.set(farthestX + dist, GameConstants.FLOWER_Y);
        flower.hitbox.setPosition(flower.position.x - 20, GameConstants.FLOWER_Y);
        flower.hitCooldown = 0f;

        if (resetStage) {
            flower.stage    = 0;
            flower.waterings = 0;
            flower.type     = MathUtils.random(0, 2);
        }
    }
    private void handleRainCollision(Flower flower, RainService rainService) {
        if (flower.hitCooldown > 0f) return;

        Iterator<RainDrop> it = rainService.getDrops().iterator();
        while (it.hasNext()) {
            RainDrop drop = it.next();
            if (!flower.hitbox.contains(drop.x, drop.y)) continue;

            it.remove();
            flower.hitCooldown = 0.25f;
            flower.waterings++;

            scoreService.addFlowerScore(flower.waterings,
                flower.position.x, flower.position.y + 50);

            growFlower(flower);
            break;
        }
    }
    private void growFlower(Flower flower) {
        switch (flower.waterings) {
            case 1: flower.stage = 1; break;
            case 2: flower.stage = 2; break;
            case 3: flower.stage = 3; break;
            case 4:
                repositionFlower(flower, true);
                break;
        }
    }
    private float getFarthestX() {
        float max = 0;
        for (Flower f : flowers) {
            if (f.position.x > max) max = f.position.x;
        }
        return max;
    }
}
