package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

import io.github.cloudlet.constants.GameConstants;

public class Enemy {
    public enum Type { SUN, WIND, BIRD, STORM }
    public Type    type;
    public Vector2 position   = new Vector2();
    public boolean active     = true;
    public float   alpha      = 1f;
    public boolean dying      = false;
    public float   blockTimer  = 0f;
    public float   effectTimer = 0f;
    public boolean triggered   = false;
    public float rotation = 0f;
    public Enemy(Type type, float x, float y) {
        this.type = type;
        this.position.set(x, y);
    }
    public void update(float delta, Cloud cloud) {
        if (!active) return;

        if (dying) {
            alpha -= delta * 1.5f;
            if (alpha <= 0f) active = false;
            return;
        }
        float touchDist = cloud.radius + GameConstants.ENEMY_HIT_RADIUS;

        switch (type) {
            case SUN:
                if (!triggered && position.dst(cloud.position) < touchDist) {
                    triggered   = true;
                    effectTimer = 3f;
                }
                if (triggered) {
                    effectTimer -= delta;
                    cloud.water -= 12f * delta;
                    if (cloud.water < 0) cloud.water = 0;
                    if (effectTimer <= 0f) dying = true;
                }
                break;

            case WIND:
                if (!triggered && position.dst(cloud.position) < touchDist) {
                    triggered    = true;
                    effectTimer  = GameConstants.WIND_DURATION;
                    cloud.blocked = true;
                }
                if (triggered) {
                    effectTimer -= delta;
                    cloud.position.x -= GameConstants.WIND_PUSH_SPEED * delta;
                    cloud.windRotation -= GameConstants.WIND_ROTATION_SPEED * delta;

                    if (effectTimer <= 0f) {
                        cloud.blocked      = false;
                        cloud.windRotation = 0f;
                        dying = true;
                    }
                }
                break;

            case BIRD:
                if (!triggered && position.dst(cloud.position) < touchDist) {
                    triggered   = true;
                    cloud.water -= 25f;
                    if (cloud.water < 0) cloud.water = 0;
                    dying = true;
                }
                break;

            case STORM:
                if (!triggered && position.dst(cloud.position) < touchDist) {
                    triggered  = true;
                    blockTimer = 1.5f;
                }
                if (blockTimer > 0f) {
                    blockTimer -= delta;
                    cloud.blocked = true;
                } else {
                    cloud.blocked = false;
                    if (triggered) dying = true;
                }
                break;
        }
    }
}
