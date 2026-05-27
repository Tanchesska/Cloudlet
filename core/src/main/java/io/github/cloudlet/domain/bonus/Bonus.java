package io.github.cloudlet.domain.bonus;

import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.interfaces.GameEntity;
import io.github.cloudlet.interfaces.Updatable;
import io.github.cloudlet.world.GameWorld;

public abstract class Bonus implements GameEntity, Updatable {

    protected Vector2 position;
    protected Vector2 velocity;
    protected boolean active       = true;
    protected float   alpha        = 1f;
    protected boolean disappearing = false;

    public Bonus(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(GameConstants.Bonus.SPEED, 0f);
    }

    @Override
    public void update(float delta) {
        position.mulAdd(velocity, delta);
        if (disappearing) {
            alpha -= delta * GameConstants.Bonus.FADE_SPEED;
            if (alpha <= 0f) active = false;
        }
    }
    public abstract void onCollect(GameWorld world);
    public float   getAlpha()               { return alpha; }
    public boolean isDisappearing()          { return disappearing; }
    public void    setDisappearing(boolean v){ disappearing = v; }
    @Override public Vector2 getPosition()  { return position; }
    @Override public boolean isActive()     { return active; }
}
