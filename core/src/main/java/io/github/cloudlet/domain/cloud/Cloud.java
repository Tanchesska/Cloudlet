package io.github.cloudlet.domain.cloud;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.interfaces.GameEntity;

public class Cloud implements GameEntity {

    private final Vector2 position;
    private float radius;
    private float water;
    private final float speed;
    private boolean dead = false;
    public Cloud(float x, float y) {
        this.position = new Vector2(x, y);
        this.radius   = GameConstants.Cloud.INITIAL_RADIUS;
        this.water    = GameConstants.Cloud.INITIAL_WATER;
        this.speed    = GameConstants.Cloud.SPEED;
    }
    public void setWater(float amount) {
        this.water = MathUtils.clamp(amount, 0f, GameConstants.Cloud.WATER_MAX);
    }
    public void addWater(float amount) {
        setWater(this.water + amount);
    }
    public void consumeWater(float amount) {
        setWater(this.water - amount);
    }
    public float   getWater()         { return water; }
    public float   getRadius()        { return radius; }
    public void    setRadius(float r) { this.radius = r; }
    public float   getSpeed()         { return speed; }
    public boolean isDead()           { return dead; }
    public void    setDead(boolean v) { this.dead = v; }

    @Override public Vector2  getPosition() { return position; }
    @Override public boolean  isActive()    { return !dead; }
}
