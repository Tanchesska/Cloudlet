package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Enemy {

    public enum Type {
        SUN,
        WIND,
        BIRD,
        STORM
    }

    public Type type;

    public Vector2 position = new Vector2();

    public boolean active = true;

    public float alpha = 1f;
    public boolean dying = false;

    public float blockTimer = 0f;


    public float effectTimer = 0f;
    public boolean triggered = false;

    public Enemy(Type type, float x, float y) {

        this.type = type;
        this.position.set(x, y);
    }

    public void update(float delta, Cloud cloud) {
        if (!active) return;

        if (dying) {

            alpha -= delta * 1.5f;

            if (alpha <= 0f) {
                active = false;
            }
        }

        switch (type) {

            case SUN:

                if (!triggered &&
                    position.dst(cloud.position) < 120f) {

                    triggered = true;
                    effectTimer = 3f;
                }

                if (triggered) {

                    effectTimer -= delta;

                    cloud.water -= 12f * delta;

                    if (cloud.water < 0) {
                        cloud.water = 0;
                    }

                    if (effectTimer <= 0f) {
                        dying = true;
                    }
                }

                break;


            case WIND:

                if (!triggered &&
                    position.dst(cloud.position) < 150f) {

                    triggered = true;
                    effectTimer = 2f;
                }

                if (triggered) {

                    effectTimer -= delta;

                    cloud.position.x -= 120f * delta;

                    if (effectTimer <= 0f) {
                        dying = true;
                    }
                }

                break;

            case BIRD:

                if (!triggered &&
                    position.dst(cloud.position) < 40f) {

                    triggered = true;

                    cloud.water -= 25f;

                    if (cloud.water < 0) {
                        cloud.water = 0;
                    }

                    dying = true;
                }

                break;


            case STORM:

                if (!triggered &&
                    position.dst(cloud.position) < 50f) {

                    triggered = true;
                    blockTimer = 1.5f;
                }

                if (blockTimer > 0f) {

                    blockTimer -= delta;

                    cloud.blocked = true;

                } else {

                    cloud.blocked = false;

                    if (triggered) {
                        dying = true;
                    }
                }

                break;
        }
    }
}
