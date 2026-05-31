package io.github.cloudlet.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.cloudlet.biome.Biome;
import io.github.cloudlet.domain.cloud.Cloud;
import io.github.cloudlet.domain.cloud.CloudEffects;

public class InputService {
    private final Vector3 touchPoint = new Vector3();

    public void update(Cloud cloud, CloudEffects effects,
                       OrthographicCamera camera, float delta, Biome biome) {

        float invert    = (effects != null && effects.isInverted()) ? -1f : 1f;
        float speedMult = (biome   != null) ? biome.getCloudSpeedMultiplier() : 1f;

        Vector2 direction = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1f;
        if (!direction.isZero()) {
            direction.nor().scl(invert);
            cloud.getPosition().mulAdd(direction, cloud.getSpeed() * speedMult * delta);
        }
        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            camera.unproject(touchPoint);
            Vector2 target = new Vector2(touchPoint.x, touchPoint.y);
            if (invert < 0f) {
                target.set(
                    2f * cloud.getPosition().x - touchPoint.x,
                    2f * cloud.getPosition().y - touchPoint.y
                );
            }
            cloud.getPosition().lerp(target, 0.08f * speedMult);
        }
    }
}
