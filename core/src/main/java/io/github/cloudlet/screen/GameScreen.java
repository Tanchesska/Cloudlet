package io.github.cloudlet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import io.github.cloudlet.Main;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.*;
import io.github.cloudlet.domain.bonus.Bonus;
import io.github.cloudlet.domain.enemy.Enemy;
import io.github.cloudlet.service.*;
import io.github.cloudlet.world.GameWorld;

public class GameScreen implements Screen {
    private final Main game;
    private final OrthographicCamera camera;
    private final GameWorld world;
    private final InputService inputService;
    private final CloudService cloudService;
    private final DropService dropService;
    private final RainService rainService;
    private final FlowerService flowerService;
    private final ScoreService scoreService;
    private final EnemyService enemyService;
    private final BonusService bonusService;
    private float gameTime = 0f;
    private float inputBlockTimer = 0f;
    private boolean autoPourTriggered = false;
    private final Rectangle pourButtonRect = new Rectangle(
        GameConstants.POUR_BTN_X, GameConstants.POUR_BTN_Y,
        GameConstants.POUR_BTN_W, GameConstants.POUR_BTN_H);

    public GameScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        Cloud cloud = new Cloud(GameConstants.CLOUD_INITIAL_X, GameConstants.CLOUD_INITIAL_Y);
        CloudEffects effects = new CloudEffects();
        this.world = new GameWorld(cloud, effects);

        this.inputService = new InputService();
        this.cloudService = new CloudService();
        this.dropService = new DropService(cloudService);
        this.rainService = new RainService();
        this.scoreService = new ScoreService();
        this.flowerService = new FlowerService(scoreService, world);
        this.enemyService = new EnemyService();
        this.bonusService = new BonusService();
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 0.05f);
        update(dt);
        draw();

        if (world.getCloud().isDead()) {
            game.setScreen(new GameOverScreen(game, scoreService.getScore(), scoreService.getBestScore()));
        }
    }
    private void update(float delta) {
        gameTime += delta;
        inputBlockTimer -= delta;
        world.setSpeedMultiplier(1f + gameTime / GameConstants.DIFFICULTY_SCALE_TIME);
        scoreService.update(delta, world.getSpeedMultiplier());
        enemyService.update(delta, world);
        bonusService.update(delta, world);

        if (inputBlockTimer <= 0f && !world.getEffects().isBlocked()) {
            inputService.update(world.getCloud(), camera, delta);
        }

        cloudService.update(world.getCloud(), delta);
        dropService.update(delta, world);
        rainService.update(delta, world);
        flowerService.update(delta, world);

        Cloud cloud = world.getCloud();
        cloud.getPosition().x = Math.max(0, Math.min(GameConstants.SCREEN_WIDTH, cloud.getPosition().x));
        cloud.getPosition().y = Math.max(GameConstants.FIELD_MIN_Y, Math.min(GameConstants.FIELD_MAX_Y, cloud.getPosition().y));

        handleAutoPour();
        handleManualPour();
    }

    private void handleAutoPour() {
        Cloud cloud = world.getCloud();
        if (cloud.getWater() >= GameConstants.AUTO_POUR_THRESHOLD
            && !rainService.isPouring(world)
            && !autoPourTriggered) {
            rainService.pour(cloud, world);
            cloudService.resetAfterRain(cloud);
            autoPourTriggered = true;
        }
        if (cloud.getWater() < GameConstants.AUTO_POUR_THRESHOLD * 0.5f) {
            autoPourTriggered = false;
        }
    }

    private void handleManualPour() {
        Cloud cloud = world.getCloud();
        boolean canRain = cloud.getWater() >= GameConstants.RAIN_THRESHOLD && !rainService.isPouring(world);
        if (!canRain) return;
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (pourButtonRect.contains(touch.x, touch.y)) {
                rainService.pour(cloud, world);
                cloudService.resetAfterRain(cloud);
                inputBlockTimer = GameConstants.INPUT_BLOCK_ON_POUR;
            }
        }
    }
    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        boolean canRain = world.getCloud().getWater() >= GameConstants.RAIN_THRESHOLD && !rainService.isPouring(world);

        game.assets.batch.setProjectionMatrix(camera.combined);
        game.assets.batch.begin();
        game.assets.batch.setColor(Color.WHITE);
        game.assets.batch.draw(game.assets.backgroundTexture, 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        game.assets.batch.end();

        game.assets.renderer.setProjectionMatrix(camera.combined);
        game.assets.renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (canRain) {
            game.assets.renderer.setColor(0.2f, 0.5f, 0.9f, 0.9f);
            drawRoundRect(pourButtonRect, 8);
        }
        drawWaterBar();
        game.assets.renderer.end();

        game.assets.batch.setProjectionMatrix(camera.combined);
        game.assets.batch.begin();
        drawCloud(world.getCloud(), world.getEffects());
        drawDrops();
        drawRainDrops();
        drawFlowers();
        drawEnemies();
        drawBonuses();
        drawHUD(canRain);
        game.assets.batch.end();
    }
    private void drawWaterBar() {
        Cloud cloud = world.getCloud();
        game.assets.renderer.setColor(0.25f, 0.25f, 0.25f, 0.7f);
        game.assets.renderer.rect(10, 28, 154, 16);
        float fill = (cloud.getWater() / GameConstants.WATER_MAX) * 150f;

        if (cloud.getWater() > 70f) {
            game.assets.renderer.setColor(0.1f, 0.5f, 0.95f, 1f);
        } else if (cloud.getWater() > 35f) {
            game.assets.renderer.setColor(0.3f, 0.75f, 1f, 1f);
        } else {
            game.assets.renderer.setColor(0.6f, 0.85f, 1f, 1f);
        }
        game.assets.renderer.rect(12, 30, fill, 12);
    }
    private void drawCloud(Cloud cloud, CloudEffects effects) {
        if (effects.hasShield()) {
            game.assets.batch.setColor(0.5f, 0.8f, 1f, 0.30f);
            float shieldSize = cloud.getRadius() * 2 + 44;
            game.assets.batch.draw(game.assets.cloudTexture,
                cloud.getPosition().x - cloud.getRadius() - 22,
                cloud.getPosition().y - cloud.getRadius() - 22,
                shieldSize, shieldSize);
            game.assets.batch.setColor(Color.WHITE);
        }
        float size = cloud.getRadius() * 2f;
        float originX = size / 2f;
        float originY = size / 2f;
        float drawX = cloud.getPosition().x - originX;
        float drawY = cloud.getPosition().y - originY;

        if (effects.getWindRotation() != 0f) {
            game.assets.batch.draw(game.assets.cloudTexture,
                drawX, drawY, originX, originY, size, size, 1f, 1f,
                effects.getWindRotation(), 0, 0,
                game.assets.cloudTexture.getWidth(), game.assets.cloudTexture.getHeight(),
                false, false);
        } else {
            game.assets.batch.draw(game.assets.cloudTexture, drawX, drawY, size, size);
        }
    }
    private void drawDrops() {
        for (Drop drop : world.getDrops()) {
            switch (drop.getType()) {
                case SPECIAL:
                    game.assets.batch.draw(game.assets.specialDropRegion, drop.getPosition().x - 9, drop.getPosition().y - 11, 12, 14);
                    break;
                case MINI_CLOUD:
                    game.assets.batch.draw(game.assets.miniCloudRegion, drop.getPosition().x - 16, drop.getPosition().y - 12, 32, 24);
                    break;
                default:
                    game.assets.batch.draw(game.assets.dropRegion, drop.getPosition().x - 6, drop.getPosition().y - 8, 12, 16);
                    break;
            }
        }
    }

    private void drawRainDrops() {
        for (RainDrop rd : world.getRainDrops()) {
            game.assets.batch.draw(game.assets.rainDropRegion, rd.getX() - 6, rd.getY() - 8, 12, 16);
        }
    }

    private void drawFlowers() {
        for (Flower flower : world.getFlowers()) {
            TextureRegion region = game.assets.flowerRegions[flower.getType()][flower.getStage()];
            game.assets.batch.draw(region, flower.getPosition().x - 24, 80, 48, 48);
        }
    }

    private void drawEnemies() {
        for (Enemy e : world.getEnemies()) {
            Texture tex = getEnemyTexture(e);
            if (tex == null) continue;
            game.assets.batch.setColor(1f, 1f, 1f, e.getAlpha());
            game.assets.batch.draw(tex, e.getPosition().x - 24, e.getPosition().y - 24, 64, 64);
        }
        game.assets.batch.setColor(Color.WHITE);
    }

    private Texture getEnemyTexture(Enemy e) {
        if (e instanceof io.github.cloudlet.domain.enemy.SunEnemy) return game.assets.sunTexture;
        if (e instanceof io.github.cloudlet.domain.enemy.WindEnemy) return game.assets.windTexture;
        if (e instanceof io.github.cloudlet.domain.enemy.BirdEnemy) return game.assets.birdTexture;
        if (e instanceof io.github.cloudlet.domain.enemy.StormEnemy) return game.assets.stormTexture;
        return null;
    }

    private void drawBonuses() {
        for (Bonus b : world.getBonuses()) {
            Texture tex = getBonusTexture(b);
            if (tex == null) continue;
            game.assets.batch.setColor(1f, 1f, 1f, b.getAlpha());
            game.assets.batch.draw(tex, b.getPosition().x - 18, b.getPosition().y - 18, 64, 64);
        }
        game.assets.batch.setColor(Color.WHITE);
    }

    private Texture getBonusTexture(Bonus b) {
        if (b instanceof io.github.cloudlet.domain.bonus.MagnetBonus) return game.assets.magnetTexture;
        if (b instanceof io.github.cloudlet.domain.bonus.ShieldBonus) return game.assets.shieldTexture;
        if (b instanceof io.github.cloudlet.domain.bonus.RainbowBonus) return game.assets.rainbowTexture;
        return null;
    }

    private void drawHUD(boolean canRain) {
        game.assets.font.getData().setScale(1f);
        game.assets.font.setColor(Color.YELLOW);
        game.assets.font.draw(game.assets.batch, "Score: " + scoreService.getScore(), 10, 470);
        game.assets.font.draw(game.assets.batch, "Best: " + scoreService.getBestScore(), 10, 452);

        if (canRain) {
            game.assets.font.setColor(Color.WHITE);
            game.assets.font.draw(game.assets.batch, "Pour!", GameConstants.POUR_BTN_X + 28, GameConstants.POUR_BTN_Y + 30);
        }

        Cloud cloud = world.getCloud();
        if (world.getEffects().isBlocked()) {
            game.assets.font.setColor(Color.RED);
            game.assets.font.draw(game.assets.batch, "STORM!", cloud.getPosition().x - 30, cloud.getPosition().y + 65);
        }
        if (world.getEffects().getWindRotation() != 0f) {
            game.assets.font.setColor(Color.ORANGE);
            game.assets.font.draw(game.assets.batch, "STORM!", cloud.getPosition().x - 22, cloud.getPosition().y + 65);
        }

        for (FloatingText t : scoreService.getTexts()) {
            game.assets.font.setColor(1f, 1f, 0f, Math.min(t.getLife(), 1f));
            game.assets.font.draw(game.assets.batch, t.getText(), t.getPos().x, t.getPos().y);
        }
        game.assets.font.setColor(Color.WHITE);
    }

    private void drawRoundRect(Rectangle r, float rad) {
        game.assets.renderer.rect(r.x + rad, r.y, r.width - rad * 2, r.height);
        game.assets.renderer.rect(r.x, r.y + rad, r.width, r.height - rad * 2);
        game.assets.renderer.circle(r.x + rad, r.y + rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + rad, rad);
        game.assets.renderer.circle(r.x + rad, r.y + r.height - rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + r.height - rad, rad);
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
