package io.github.cloudlet.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.cloudlet.domain.cloud.Cloud;

public class InputService {
    private final Vector3 touchPoint = new Vector3();
    public void update(Cloud cloud, OrthographicCamera camera, float delta) {
        Vector2 direction = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1f;
        if (!direction.isZero()) {
            direction.nor();
            cloud.getPosition().mulAdd(direction, cloud.getSpeed() * delta);
        }
        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            camera.unproject(touchPoint);
            cloud.getPosition().lerp(new Vector2(touchPoint.x, touchPoint.y), 0.08f);
        }
    }
}
