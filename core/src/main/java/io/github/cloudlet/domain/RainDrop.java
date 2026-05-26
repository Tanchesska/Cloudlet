package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.interfaces.GameEntity;
import io.github.cloudlet.interfaces.Updatable;

public class RainDrop implements GameEntity, Updatable {
    private float x, y;
    private final float velX, velY;
    public RainDrop(float x, float y, float velX, float velY) {
        this.x = x; this.y = y;
        this.velX = velX; this.velY = velY;
    }

    @Override
    public void update(float delta) {
        x += velX * delta;
        y += velY * delta;
    }

    public float getX()    { return x; }
    public float getY()    { return y; }

    @Override public Vector2 getPosition() { return new Vector2(x, y); }
    @Override public boolean isActive()    { return true; }
}
