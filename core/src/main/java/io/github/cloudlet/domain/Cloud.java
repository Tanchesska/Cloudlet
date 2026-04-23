package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class Cloud {
    public Vector2 position = new Vector2();
    public boolean hasShield     = false;
    public float   magnetTimer   = 0f;
    public float   rainbowTimer  = 0f;
    public float   windRotation  = 0f;
    public float radius    = 40f;
    public float speed     = 300f;
    public float water     = 20f;
    public float minRadius = 20f;
    public float maxRadius = 90f;
    public boolean dead    = false;
    public boolean blocked = false;
    public Cloud(float x, float y) {
        this.position.set(x, y);
    }
}
