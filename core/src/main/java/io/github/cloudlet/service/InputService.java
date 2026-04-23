package io.github.cloudlet.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.cloudlet.domain.Cloud;

public class InputService {

    private Vector3 touchPoint = new Vector3();

    public void update(Cloud cloud, OrthographicCamera camera, float delta) {

        Vector2 direction = new Vector2();

        // управление WASD
        if (Gdx.input.isKeyPressed(Input.Keys.W)) direction.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) direction.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) direction.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) direction.x += 1;

        if (!direction.isZero()) {
            direction.nor();
            cloud.position.mulAdd(direction, cloud.speed * delta);
        }

        // управление мышкой
        if (Gdx.input.isTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPoint);

            Vector2 target = new Vector2(touchPoint.x, touchPoint.y);
            cloud.position.lerp(target, 0.08f);
        }
    }
}
