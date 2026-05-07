package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Bonus {

    public enum Type {
        MAGNET,
        SHIELD,
        RAINBOW
    }

    public Type type;

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();

    public boolean active = true;

    public float alpha = 1f;
    public boolean disappearing = false;

    public Bonus(Type type, float x, float y) {

        this.type = type;

        position.set(x, y);

        velocity.set(
            -120f,
            0
        );
    }

    public void update(float delta) {

        position.mulAdd(velocity, delta);

        if (disappearing) {

            alpha -= delta * 2f;

            if (alpha <= 0f) {
                active = false;
            }
        }
    }
}
