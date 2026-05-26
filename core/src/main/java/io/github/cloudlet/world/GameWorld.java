package io.github.cloudlet.world;
import java.util.ArrayList;
import java.util.List;
import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.CloudEffects;
import io.github.cloudlet.domain.Drop;
import io.github.cloudlet.domain.Flower;
import io.github.cloudlet.domain.RainDrop;
import io.github.cloudlet.domain.bonus.Bonus;
import io.github.cloudlet.domain.enemy.Enemy;

public class GameWorld {
    private final Cloud          cloud;
    private final CloudEffects   effects;
    private final List<Enemy>    enemies   = new ArrayList<>();
    private final List<Bonus>    bonuses   = new ArrayList<>();
    private final List<Drop>     drops     = new ArrayList<>();
    private final List<RainDrop> rainDrops = new ArrayList<>();
    private final List<Flower>   flowers   = new ArrayList<>();
    private float   speedMultiplier = 1f;
    private boolean gameOver        = false;

    public GameWorld(Cloud cloud, CloudEffects effects) {
        this.cloud   = cloud;
        this.effects = effects;
    }
    public void cancelEnemyEffects() {
        for (Enemy enemy : enemies) {
            if (enemy.isTriggered() && enemy.isActive() && !enemy.isDying()) {
                enemy.cancelEffect(this);
                enemy.setDying(true);
            }
        }
    }

    public Cloud          getCloud()           { return cloud; }
    public CloudEffects   getEffects()         { return effects; }
    public List<Enemy>    getEnemies()         { return enemies; }
    public List<Bonus>    getBonuses()         { return bonuses; }
    public List<Drop>     getDrops()           { return drops; }
    public List<RainDrop> getRainDrops()       { return rainDrops; }
    public List<Flower>   getFlowers()         { return flowers; }
    public float          getSpeedMultiplier() { return speedMultiplier; }
    public void           setSpeedMultiplier(float v) { speedMultiplier = v; }
    public boolean        isGameOver()         { return gameOver; }
    public void           setGameOver(boolean v)      { gameOver = v; }
}
