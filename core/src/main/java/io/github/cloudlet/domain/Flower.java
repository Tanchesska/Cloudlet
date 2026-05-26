package io.github.cloudlet.domain;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.interfaces.GameEntity;

public class Flower implements GameEntity {

    private final Vector2   position;
    private int             type;
    private int             stage       = 0;
    private int             waterings   = 0;
    private float           hitCooldown = 0f;
    private final Rectangle hitbox;

    public Flower(float x, float y, int type) {
        this.position = new Vector2(x, y);
        this.type     = type;
        this.hitbox   = new Rectangle(x - 20, y, 40, 40);
    }
    public void reposition(float x) {
        position.set(x, GameConstants.FLOWER_Y);
        hitbox.setPosition(x - 20, GameConstants.FLOWER_Y);
        hitCooldown = 0f;
    }
    public int     getType()       { return type; }
    public void    setType(int t)  { type = t; }
    public int     getStage()      { return stage; }
    public void    setStage(int s) { stage = s; }
    public int     getWaterings()  { return waterings; }
    public void    incrementWaterings() { waterings++; }
    public void    resetWaterings()     { waterings = 0; }
    public float   getHitCooldown()     { return hitCooldown; }
    public void    setHitCooldown(float v)   { hitCooldown = v; }
    public void    decrementHitCooldown(float delta) { hitCooldown -= delta; }
    public Rectangle getHitbox()  { return hitbox; }

    @Override public Vector2 getPosition() { return position; }
    @Override public boolean isActive()    { return true; }
}
