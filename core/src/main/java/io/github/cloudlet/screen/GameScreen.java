package io.github.cloudlet.screen;

import java.util.ArrayList;
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
import io.github.cloudlet.achievement.AchievementService;
import io.github.cloudlet.achievement.AchievementType;
import io.github.cloudlet.biome.Biome;
import io.github.cloudlet.biome.BiomeType;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.Drop;
import io.github.cloudlet.domain.FloatingText;
import io.github.cloudlet.domain.Flower;
import io.github.cloudlet.domain.RainDrop;
import io.github.cloudlet.domain.bonus.Bonus;
import io.github.cloudlet.domain.cloud.Cloud;
import io.github.cloudlet.domain.cloud.CloudEffects;
import io.github.cloudlet.domain.enemy.BirdEnemy;
import io.github.cloudlet.domain.enemy.Enemy;
import io.github.cloudlet.domain.enemy.StormEnemy;
import io.github.cloudlet.domain.enemy.SunEnemy;
import io.github.cloudlet.domain.enemy.WindEnemy;
import io.github.cloudlet.domain.bonus.MagnetBonus;
import io.github.cloudlet.domain.bonus.ShieldBonus;
import io.github.cloudlet.domain.bonus.RainbowBonus;
import io.github.cloudlet.service.BonusService;
import io.github.cloudlet.service.CloudService;
import io.github.cloudlet.service.DropService;
import io.github.cloudlet.service.EnemyService;
import io.github.cloudlet.service.FlowerService;
import io.github.cloudlet.service.InputService;
import io.github.cloudlet.service.RainService;
import io.github.cloudlet.service.ScoreService;
import io.github.cloudlet.world.GameWorld;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class GameScreen implements Screen {

    private final Main               game;
    private final OrthographicCamera camera;
    private final GameWorld          world;
    private final InputService       inputService;
    private final CloudService       cloudService;
    private final DropService        dropService;
    private final RainService        rainService;
    private final FlowerService      flowerService;
    private final ScoreService       scoreService;
    private final EnemyService       enemyService;
    private final BonusService       bonusService;
    private final AchievementService achievementService;

    private float   gameTime          = 0f;
    private float   inputBlockTimer   = 0f;
    private boolean autoPourTriggered = false;

    private final Rectangle pourButtonRect = new Rectangle(
        GameConstants.Screen.POUR_BTN_X, GameConstants.Screen.POUR_BTN_Y,
        GameConstants.Screen.POUR_BTN_W, GameConstants.Screen.POUR_BTN_H);

    private static final class AchievementNotif {
        final String  text;
        final String  title;
        final String  description;
        final boolean isAchievement;
        float timer;

        AchievementNotif(String biomeMessage) {
            this.text          = biomeMessage;
            this.title         = "";
            this.description   = "";
            this.isAchievement = false;
            this.timer         = GameConstants.Biome.TRANSITION_DISPLAY_DURATION;
        }

        AchievementNotif(AchievementType type) {
            this.title         = type.title;
            this.text          = type.reason;
            this.description   = type.description;
            this.isAchievement = true;
            this.timer         = GameConstants.Achievement.NOTIFICATION_DURATION;
        }
    }
    private final Deque<AchievementNotif> notifQueue = new ArrayDeque<>();
    private final List<AchievementType> sessionAchievements = new ArrayList<>();
    public GameScreen(Main game) {
        this.game   = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.Screen.WIDTH, GameConstants.Screen.HEIGHT);

        Cloud        cloud   = new Cloud(GameConstants.Cloud.INITIAL_X, GameConstants.Cloud.INITIAL_Y);
        CloudEffects effects = new CloudEffects();
        this.world  = new GameWorld(cloud, effects);

        this.inputService       = new InputService();
        this.cloudService       = new CloudService();
        this.dropService        = new DropService(cloudService);
        this.rainService        = new RainService();
        this.scoreService       = new ScoreService();
        this.flowerService      = new FlowerService(scoreService, world);
        this.enemyService       = new EnemyService();
        this.bonusService       = new BonusService();
        this.achievementService = new AchievementService();
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 0.05f);
        update(dt);
        draw();

        if (world.getCloud().isDead()) {
            List<AchievementType> finalAch = achievementService.checkOnGameOver(
                world.getGameStats(), scoreService.getScore());
            sessionAchievements.addAll(finalAch);
            game.setScreen(new GameOverScreen(
                game, scoreService.getScore(), scoreService.getBestScore(), sessionAchievements));
        }
    }

    private void update(float delta) {
        gameTime        += delta;
        inputBlockTimer -= delta;

        world.setSpeedMultiplier(1f + gameTime / GameConstants.Screen.DIFFICULTY_SCALE_TIME);
        scoreService.update(delta, world.getSpeedMultiplier());

        boolean biomeChanged = world.getBiomeManager().update(scoreService.getScore(), delta);
        if (biomeChanged) {
            notifQueue.addFirst(
                new AchievementNotif(world.getBiomeManager().getTransitionMessage()));
        }

        enemyService.update(delta, world);
        bonusService.update(delta, world);

        Biome currentBiome = world.getBiomeManager().getCurrentBiome();

        CloudEffects effects = world.getEffects();
        if (inputBlockTimer <= 0f && !effects.isBlocked()) {
            inputService.update(world.getCloud(), effects, camera, delta, currentBiome);
        }

        cloudService.update(world.getCloud(), delta, currentBiome);

        dropService.update(delta, world);
        rainService.update(delta, world);
        flowerService.update(delta, world);

        Cloud cloud = world.getCloud();
        cloud.getPosition().x = Math.max(0,
            Math.min(GameConstants.Screen.WIDTH, cloud.getPosition().x));
        cloud.getPosition().y = Math.max(GameConstants.Screen.FIELD_MIN_Y,
            Math.min(GameConstants.Screen.FIELD_MAX_Y, cloud.getPosition().y));

        handleAutoPour();
        handleManualPour();
        updateGameStats(delta);
        List<AchievementType> unlockedNow = achievementService.checkOnGameOver(world.getGameStats(), scoreService.getScore());
        for (AchievementType ach : unlockedNow) {
            notifQueue.addLast(new AchievementNotif(ach));
            sessionAchievements.add(ach);
        }
        if (!notifQueue.isEmpty()) {
            AchievementNotif top = notifQueue.peek();
            top.timer -= delta;
            if (top.timer <= 0f) notifQueue.poll();
        }
    }

    private void updateGameStats(float delta) {
        float waterPct = world.getCloud().getWater() / GameConstants.Cloud.WATER_MAX * 100f;
        if (waterPct <= GameConstants.Achievement.DROUGHT_WATER_THRESHOLD) {
            world.getGameStats().continuousLowWaterTime += delta;
            if (world.getGameStats().continuousLowWaterTime
                >= GameConstants.Achievement.DROUGHT_LOW_WATER_TIME) {
                world.getGameStats().droughtAchieved = true;
            }
        } else {
            world.getGameStats().continuousLowWaterTime = 0f;
        }
    }

    private void handleAutoPour() {
        Cloud cloud = world.getCloud();
        if (cloud.getWater() >= GameConstants.Cloud.AUTO_POUR_THRESHOLD
            && !rainService.isPouring(world) && !autoPourTriggered) {
            rainService.pour(cloud, world);
            cloudService.resetAfterRain(cloud);
            autoPourTriggered = true;
        }
        if (cloud.getWater() < GameConstants.Cloud.AUTO_POUR_THRESHOLD * 0.5f) {
            autoPourTriggered = false;
        }
    }

    private void handleManualPour() {
        Cloud cloud   = world.getCloud();
        boolean canRain = cloud.getWater() >= GameConstants.Cloud.RAIN_THRESHOLD
            && !rainService.isPouring(world);
        if (!canRain) return;
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (pourButtonRect.contains(touch.x, touch.y)) {
                rainService.pour(cloud, world);
                cloudService.resetAfterRain(cloud);
                inputBlockTimer = GameConstants.Screen.INPUT_BLOCK_ON_POUR;
            }
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        boolean canRain = world.getCloud().getWater() >= GameConstants.Cloud.RAIN_THRESHOLD
            && !rainService.isPouring(world);

        Biome    currentBiome = world.getBiomeManager().getCurrentBiome();
        BiomeType biomeType   = currentBiome.getType();
        int       biomeIdx    = biomeType.ordinal();

        game.assets.batch.setProjectionMatrix(camera.combined);
        game.assets.batch.begin();
        game.assets.batch.setColor(Color.WHITE);
        game.assets.batch.draw(game.assets.getBackgroundForBiome(biomeType),
            0, 0, GameConstants.Screen.WIDTH, GameConstants.Screen.HEIGHT);
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
        drawDrops(biomeIdx, biomeType);
        drawRainDrops();
        drawFlowers(biomeIdx);
        drawEnemies(biomeIdx);
        drawBonuses();
        drawHUD(canRain, currentBiome);
        drawNotification();
        game.assets.batch.end();
    }

    private void drawWaterBar() {
        Cloud cloud = world.getCloud();
        game.assets.renderer.setColor(0.25f, 0.25f, 0.25f, 0.7f);
        game.assets.renderer.rect(10, 28, 154, 16);
        float fill = (cloud.getWater() / GameConstants.Cloud.WATER_MAX) * 150f;
        if      (cloud.getWater() > 70f) game.assets.renderer.setColor(0.1f, 0.5f,  0.95f, 1f);
        else if (cloud.getWater() > 35f) game.assets.renderer.setColor(0.3f, 0.75f, 1f,    1f);
        else                             game.assets.renderer.setColor(0.6f, 0.85f, 1f,    1f);
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
        float size    = cloud.getRadius() * 2f;
        float originX = size / 2f;
        float originY = size / 2f;
        float drawX   = cloud.getPosition().x - originX;
        float drawY   = cloud.getPosition().y - originY;
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

    private void drawDrops(int biomeIdx, BiomeType biomeType) {
        boolean isToxic = biomeType == BiomeType.TOXIC;
        for (Drop drop : world.getDrops()) {
            switch (drop.getType()) {
                case SPECIAL:
                    if (isToxic) {
                        game.assets.batch.setColor(0.4f, 1f, 0.4f,
                            0.9f + 0.1f * (float) Math.sin(System.currentTimeMillis() / 300.0));
                    }
                    game.assets.batch.draw(game.assets.specialDropBiomeRegion[biomeIdx],
                        drop.getPosition().x - 9, drop.getPosition().y - 11, 12, 14);
                    game.assets.batch.setColor(Color.WHITE);
                    break;
                case MINI_CLOUD:
                    game.assets.batch.draw(game.assets.miniCloudBiomeRegion[biomeIdx],
                        drop.getPosition().x - 16, drop.getPosition().y - 12, 32, 24);
                    break;
                case ACID:
                    game.assets.batch.setColor(0.3f, 1f, 0.3f, 0.85f);
                    game.assets.batch.draw(game.assets.acidDropRegion,
                        drop.getPosition().x - 9, drop.getPosition().y - 11, 12, 14);
                    game.assets.batch.setColor(Color.WHITE);
                    break;
                default:
                    game.assets.batch.draw(game.assets.dropRegion,
                        drop.getPosition().x - 6, drop.getPosition().y - 8, 12, 16);
                    break;
            }
        }
    }

    private void drawRainDrops() {
        for (RainDrop rd : world.getRainDrops()) {
            game.assets.batch.draw(game.assets.rainDropRegion,
                rd.getX() - 6, rd.getY() - 8, 12, 16);
        }
    }

    private void drawFlowers(int biomeIdx) {
        for (Flower flower : world.getFlowers()) {
            TextureRegion region =
                game.assets.flowerBiomeRegions[biomeIdx][flower.getType()][flower.getStage()];
            game.assets.batch.draw(region, flower.getPosition().x - 24, 80, 48, 48);
        }
    }

    private void drawEnemies(int biomeIdx) {
        for (Enemy e : world.getEnemies()) {
            if (e instanceof SunEnemy) {
                game.assets.batch.setColor(1f, 1f, 1f, e.getAlpha());
                game.assets.batch.draw(game.assets.sunTexture,
                    e.getPosition().x - 32, e.getPosition().y - 32, 64, 64);
            } else {
                Texture tex = getEnemyTexture(e, biomeIdx);
                if (tex == null) continue;
                game.assets.batch.setColor(1f, 1f, 1f, e.getAlpha());
                game.assets.batch.draw(tex,
                    e.getPosition().x - 32, e.getPosition().y - 32, 64, 64);
            }
        }
        game.assets.batch.setColor(Color.WHITE);
    }

    private Texture getEnemyTexture(Enemy e, int biomeIdx) {
        if (e instanceof BirdEnemy)  return game.assets.enemyBiomeTextures[biomeIdx][0];
        if (e instanceof WindEnemy)  return game.assets.enemyBiomeTextures[biomeIdx][1];
        if (e instanceof StormEnemy) return game.assets.enemyBiomeTextures[biomeIdx][2];
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
        if (b instanceof MagnetBonus)  return game.assets.magnetTexture;
        if (b instanceof ShieldBonus)  return game.assets.shieldTexture;
        if (b instanceof RainbowBonus) return game.assets.rainbowTexture;
        return null;
    }

    private void drawHUD(boolean canRain, Biome currentBiome) {
        game.assets.font.getData().setScale(1f);
        game.assets.font.setColor(Color.YELLOW);
        game.assets.font.draw(game.assets.batch, "Score: " + scoreService.getScore(), 10, 470);
        game.assets.font.draw(game.assets.batch, "Best: "  + scoreService.getBestScore(), 10, 452);

        game.assets.font.setColor(0.9f, 0.9f, 0.3f, 1f);
        game.assets.font.draw(game.assets.batch, currentBiome.getDisplayName(), 10, 434);

        if (canRain) {
            game.assets.font.setColor(Color.WHITE);
            game.assets.font.draw(game.assets.batch, "Pour!",
                GameConstants.Screen.POUR_BTN_X + 28, GameConstants.Screen.POUR_BTN_Y + 30);
        }

        Cloud cloud = world.getCloud();
        if (world.getEffects().isBlocked()) {
            game.assets.font.setColor(Color.RED);
            game.assets.font.draw(game.assets.batch, "STORM!",
                cloud.getPosition().x - 30, cloud.getPosition().y + 65);
        }
        if (world.getEffects().getWindRotation() != 0f) {
            game.assets.font.setColor(Color.ORANGE);
            game.assets.font.draw(game.assets.batch, "STORM!",
                cloud.getPosition().x - 22, cloud.getPosition().y + 65);
        }

        for (FloatingText t : scoreService.getTexts()) {
            game.assets.font.setColor(1f, 1f, 0f, Math.min(t.getLife(), 1f));
            game.assets.font.draw(game.assets.batch, t.getText(), t.getPos().x, t.getPos().y);
        }
        game.assets.font.setColor(Color.WHITE);
    }

    private void drawNotification() {
        if (notifQueue.isEmpty()) return;
        AchievementNotif notif = notifQueue.peek();
        float alpha = Math.min(1f, notif.timer);

        if (notif.isAchievement) {
            float bw = 340f, bh = 72f;
            float bx = (GameConstants.Screen.WIDTH  - bw) / 2f;
            float by =  GameConstants.Screen.HEIGHT - 120f;

            game.assets.batch.end();
            game.assets.renderer.begin(ShapeRenderer.ShapeType.Filled);
            game.assets.renderer.setColor(0.1f, 0.1f, 0.1f, 0.85f * alpha);
            game.assets.renderer.rect(bx, by, bw, bh);
            game.assets.renderer.setColor(0.85f, 0.65f, 0f, alpha);
            game.assets.renderer.rect(bx, by + bh - 4, bw, 4);
            game.assets.renderer.end();
            game.assets.batch.begin();

            game.assets.font.getData().setScale(1.1f);
            game.assets.font.setColor(1f, 0.85f, 0f, alpha);
            game.assets.font.draw(game.assets.batch, "[+] " + notif.title, bx + 14, by + bh - 10);

            game.assets.font.getData().setScale(0.85f);
            game.assets.font.setColor(0.9f, 0.9f, 0.9f, alpha);
            game.assets.font.draw(game.assets.batch, notif.text, bx + 14, by + bh - 32);

            game.assets.font.getData().setScale(0.75f);
            game.assets.font.setColor(0.65f, 0.65f, 0.65f, alpha);
            game.assets.font.draw(game.assets.batch, notif.description, bx + 14, by + 18);
        } else {
            game.assets.font.getData().setScale(1.1f);
            game.assets.font.setColor(0.9f, 0.9f, 0.3f, alpha);
            drawCentered(notif.text, GameConstants.Screen.WIDTH / 2f,
                GameConstants.Screen.HEIGHT - 30);
        }

        game.assets.font.getData().setScale(1f);
        game.assets.font.setColor(Color.WHITE);
    }

    private void drawCentered(String text, float centerX, float y) {
        game.assets.glyph.setText(game.assets.font, text);
        game.assets.font.draw(game.assets.batch, text,
            centerX - game.assets.glyph.width / 2f, y);
    }

    private void drawRoundRect(Rectangle r, float rad) {
        game.assets.renderer.rect(r.x + rad, r.y, r.width - rad * 2, r.height);
        game.assets.renderer.rect(r.x, r.y + rad, r.width, r.height - rad * 2);
        game.assets.renderer.circle(r.x + rad,           r.y + rad,            rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + rad,            rad);
        game.assets.renderer.circle(r.x + rad,           r.y + r.height - rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + r.height - rad, rad);
    }

    @Override public void show()   {}
    @Override public void resize(int w, int h) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose(){}
}
