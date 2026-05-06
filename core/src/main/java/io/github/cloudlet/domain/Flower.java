package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Flower {

    public Vector2 position;

    public int type;

    public int stage = 0;

    public int waterings = 0;

    public float hitCooldown = 0f;

    public Rectangle hitbox;

    public Flower(float x, float y, int type) {
        this.position = new Vector2(x, y);
        this.type = type;

        this.hitbox = new Rectangle(x - 20, y, 40, 40);
    }

    public void reset(float x) {
        position.set(x, 100);

        hitCooldown = 0f;

        hitbox.setPosition(x - 20, 100);
    }
}
