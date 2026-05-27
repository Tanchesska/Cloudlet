package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Vector2;

public class FloatingText {
    private final Vector2 pos;
    private final String  text;
    private float life = 1f;
    public FloatingText(float x, float y, String text) {
        this.pos  = new Vector2(x, y);
        this.text = text;
    }
    public void update(float delta) {
        life  -= delta;
        pos.y += 40f * delta;
    }
    public Vector2 getPos()    { return pos; }
    public String  getText()   { return text; }
    public float   getLife()   { return life; }
    public boolean isExpired() { return life <= 0f; }
}
