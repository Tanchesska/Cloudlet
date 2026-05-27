package io.github.cloudlet.domain.enemy;
import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.interfaces.GameEntity;
import io.github.cloudlet.world.GameWorld;

public abstract class Enemy implements GameEntity {
    protected Vector2 position;
    protected boolean active    = true;
    protected float   alpha     = 1f;
    protected boolean dying     = false;
    protected boolean triggered = false;
    public Enemy(float x, float y) {
        this.position = new Vector2(x, y);
    }
    public abstract void update(float delta, GameWorld world);
    public void cancelEffect(GameWorld world) {}
    protected void startDying() {
        dying = true;
    }
    public float   getAlpha()            { return alpha; }
    public boolean isDying()             { return dying; }
    public boolean isTriggered()         { return triggered; }
    public void    setDying(boolean v)   { dying = v; }

    @Override public Vector2 getPosition() { return position; }
    @Override public boolean isActive()    { return active; }
}
