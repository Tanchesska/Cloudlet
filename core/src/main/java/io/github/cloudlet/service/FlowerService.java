package io.github.cloudlet.service;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.cloudlet.domain.Flower;
import io.github.cloudlet.domain.RainDrop;

public class FlowerService {

    private List<Flower> flowers = new ArrayList<>();

    private static final float SPEED = 60f;

    public FlowerService() {
        for (int i = 0; i < 5; i++) {
            float x = 800 + i * 200;
            int type = MathUtils.random(0, 2);

            flowers.add(new Flower(x, 100, type));
        }
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void update(float delta, RainService rainService, float speedMultiplier) {

        for (Flower flower : flowers) {

            flower.position.x -= SPEED * speedMultiplier * delta;
            flower.hitbox.x = flower.position.x - 20;

            if (flower.position.x < -50) {
                flower.reset(820);
            }

            flower.hitCooldown -= delta;

            Iterator<RainDrop> it = rainService.getDrops().iterator();

            while (it.hasNext()) {

                RainDrop drop = it.next();

                if (flower.hitCooldown <= 0f &&
                    flower.hitbox.contains(drop.x, drop.y)) {

                    flower.hitCooldown = 0.3f;

                    it.remove();


                    flower.waterings++;

                    if (flower.waterings == 1) flower.stage = 1;
                    else if (flower.waterings == 2) flower.stage = 2;
                    else if (flower.waterings == 3) flower.stage = 3;

                    else if (flower.waterings == 4) {
                        flower.waterings = 0;
                        flower.stage = 0;
                        flower.reset(820);
                    }

                    break;
                }
            }
        }
    }
}
