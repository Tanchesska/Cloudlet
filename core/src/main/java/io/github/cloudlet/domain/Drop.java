package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Drop {

    public enum DropType {
        NORMAL,
        SPECIAL,
        MINI_CLOUD
    }
    public Vector2  position;
    public float    radius   = 10f;
    public boolean  collected = false;
    public DropType type;
    public Drop(float x, float y) {
        this(x, y, DropType.NORMAL);
    }
    public Drop(float x, float y, DropType type) {
        this.position = new Vector2(x, y);
        this.type     = type;
    }
}
