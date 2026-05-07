package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Drop {

    public Vector2 position;
    public float radius = 10f;
    public boolean collected = false;

    public Drop(float x, float y) {
        this.position = new Vector2(x, y);
    }
}
