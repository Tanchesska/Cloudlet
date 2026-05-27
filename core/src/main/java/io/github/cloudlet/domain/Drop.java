package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.interfaces.GameEntity;
public class Drop implements GameEntity {
    public enum DropType { NORMAL, SPECIAL, MINI_CLOUD }
    private final Vector2  position;
    private final float    radius   = 10f;
    private final DropType type;
    private boolean collected = false;
    public Drop(float x, float y) {
        this(x, y, DropType.NORMAL);
    }
    public Drop(float x, float y, DropType type){
        this.position = new Vector2(x, y);
        this.type     = type;
    }
    public DropType getType()  {
        return type;
    }
    public float    getRadius()  {
        return radius;
    }
    public boolean  isCollected() {
        return collected;
    }
    public void     setCollected(boolean v) {
        collected = v;
    }

    @Override public Vector2 getPosition() {
        return position;
    }
    @Override public boolean isActive() {
        return !collected;
    }
}
