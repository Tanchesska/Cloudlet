package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Cloud {

    public Vector2 position;

    public float radius = 40f;
    public float speed = 300f;

    public float water = 50f;

    public Cloud(float x, float y) {
        this.position = new Vector2(x, y);
    }
}
