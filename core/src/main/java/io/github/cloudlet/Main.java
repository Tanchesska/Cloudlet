package io.github.cloudlet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.*;
import io.github.cloudlet.service.*;
import io.github.cloudlet.storage.ScoreStorage;

public class Main extends ApplicationAdapter {
    private GameState state = GameState.MENU;
    private OrthographicCamera camera;
    private ShapeRenderer      renderer;
    private SpriteBatch        batch;
    private BitmapFont         font;
    private GlyphLayout        glyph;
    private Texture backgroundTexture;
    private Texture menuBackgroundTexture;
    private Texture       cloudTexture;
    private Texture       dropTexture;
    private Texture       specialDropTexture;
    private Texture       miniCloudTexture;
    private TextureRegion dropRegion;
    private TextureRegion rainDropRegion;
    private TextureRegion specialDropRegion;
    private TextureRegion miniCloudRegion;
    private Texture       flowersTexture;
    private TextureRegion[][] flowerRegions;
    private Texture sunTexture, windTexture, birdTexture, stormTexture;
    private Texture magnetTexture, shieldTexture, rainbowTexture;
    private Cloud cloud;
    private InputService  inputService;
    private CloudService  cloudService;
    private DropService   dropService;
    private RainService   rainService;
    private FlowerService flowerService;
    private ScoreService  scoreService;
    private EnemyService  enemyService;
    private BonusService  bonusService;
    private float   gameTime          = 0f;
    private float   inputBlockTimer   = 0f;
    private boolean autoPourTriggered = false;
    private final Rectangle pourButtonRect = new Rectangle(
        GameConstants.POUR_BTN_X, GameConstants.POUR_BTN_Y,
        GameConstants.POUR_BTN_W, GameConstants.POUR_BTN_H);
    private final Rectangle startButtonRect   = new Rectangle(300, 185, 200, 55);
    private final Rectangle restartButtonRect = new Rectangle(300, 165, 200, 55);
    private static final Color PINK_BTN      = new Color(0.87f, 0.27f, 0.54f, 1f);
    private static final Color PINK_TITLE    = new Color(0.62f, 0.05f, 0.32f, 1f);
    private static final Color PINK_SUBTITLE = new Color(0.75f, 0.25f, 0.50f, 1f);
    private final Vector3 tmpTouch = new Vector3();

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        renderer = new ShapeRenderer();
        batch    = new SpriteBatch();
        font     = new BitmapFont();
        glyph    = new GlyphLayout();
        loadTextures();
        initGame();
    }
    private void loadTextures() {
       backgroundTexture = new Texture("background.png");
       backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        menuBackgroundTexture = new Texture("menu_background.png");
        menuBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cloudTexture = new Texture("blue_cloud.png");
        cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        dropTexture = new Texture("drops.png");
        dropTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        dropRegion     = new TextureRegion(dropTexture, 0, 0, 6, 8);
        rainDropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);

        specialDropTexture = new Texture("purple_drop.png");
        specialDropTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        specialDropRegion  = new TextureRegion(specialDropTexture,
            0, 0, specialDropTexture.getWidth(), specialDropTexture.getHeight());

        miniCloudTexture = new Texture("pink_cloud.png");
        miniCloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        miniCloudRegion  = new TextureRegion(miniCloudTexture,
            0, 0, miniCloudTexture.getWidth(), miniCloudTexture.getHeight());

        flowersTexture = new Texture("flowers.png");
        flowersTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        initFlowerRegions();

        sunTexture   = new Texture("sun.png");
        windTexture  = new Texture("wind2.png");
        birdTexture  = new Texture("eagles.png");
        stormTexture = new Texture("storm.png");

        magnetTexture  = new Texture("magnit.png");
        shieldTexture  = new Texture("shield.png");
        rainbowTexture = new Texture("rainbow.png");
    }

    private void initFlowerRegions() {
        flowerRegions    = new TextureRegion[3][4];
        flowerRegions[0][0] = new TextureRegion(flowersTexture, 160, 192, 32, 32);
        flowerRegions[0][1] = new TextureRegion(flowersTexture, 128, 192, 32, 32);
        flowerRegions[0][2] = new TextureRegion(flowersTexture, 96,  192, 32, 32);
        flowerRegions[0][3] = new TextureRegion(flowersTexture, 64,  192, 32, 32);
        flowerRegions[1][0] = new TextureRegion(flowersTexture, 96,  0,   32, 32);
        flowerRegions[1][1] = new TextureRegion(flowersTexture, 192, 160, 32, 32);
        flowerRegions[1][2] = new TextureRegion(flowersTexture, 0,   192, 32, 32);
        flowerRegions[1][3] = new TextureRegion(flowersTexture, 32,  192, 32, 32);
        flowerRegions[2][0] = new TextureRegion(flowersTexture, 32,  224, 32, 32);
        flowerRegions[2][1] = new TextureRegion(flowersTexture, 0,   224, 32, 32);
        flowerRegions[2][2] = new TextureRegion(flowersTexture, 224, 96,  32, 32);
        flowerRegions[2][3] = new TextureRegion(flowersTexture, 128, 96,  32, 32);
    }
    private void initGame() {
        cloud         = new Cloud(GameConstants.CLOUD_INITIAL_X, GameConstants.CLOUD_INITIAL_Y);
        inputService  = new InputService();
        cloudService  = new CloudService();
        dropService   = new DropService();
        rainService   = new RainService();
        scoreService  = new ScoreService();
        flowerService = new FlowerService(scoreService);
        enemyService  = new EnemyService();
        bonusService  = new BonusService();
        gameTime          = 0f;
        inputBlockTimer   = 0f;
        autoPourTriggered = false;
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        switch (state) {
            case MENU:      updateMenu(delta);    renderMenu();    break;
            case PLAYING:   updatePlaying(delta); renderPlaying(); break;
            case GAME_OVER: updateGameOver();     renderGameOver();break;
        }
    }
    private void updateMenu(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = unprojectTouch();
            if (startButtonRect.contains(touch.x, touch.y)) {
                startGame();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }
    }
    private void renderMenu() {
        float W = GameConstants.SCREEN_WIDTH;
        float H = GameConstants.SCREEN_HEIGHT;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(menuBackgroundTexture, 0, 0, W, H);
        batch.end();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(PINK_BTN);
        drawRoundRect(startButtonRect, 12);
        renderer.end();
        batch.begin();
        font.getData().setScale(3.8f);
        font.setColor(0.5f, 0.03f, 0.25f, 0.4f);
        drawCentered("CAUDLET", W / 2f + 3, 398);
        font.setColor(PINK_TITLE);
        drawCentered("CAUDLET", W / 2f, 402);
        font.getData().setScale(1.1f);
        font.setColor(PINK_SUBTITLE);
        font.getData().setScale(1.6f);
        font.setColor(Color.WHITE);
        drawCentered("Start Game", W / 2f, 220);
        int saved = ScoreStorage.loadBestScore();
        if (saved > 0) {
            font.getData().setScale(1.15f);
            font.setColor(PINK_BTN);
            drawCentered("Best Score: " + saved, W / 2f, 148);
        }
        font.getData().setScale(0.9f);
        font.setColor(PINK_SUBTITLE);
        drawCentered("ENTER to start", W / 2f, 110);

        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        batch.end();
    }
    private void startGame() {
        initGame();
        state = GameState.PLAYING;
    }
    private void updatePlaying(float delta) {
        gameTime += delta;
        inputBlockTimer -= delta;
        float speedMultiplier = 1f + gameTime / GameConstants.DIFFICULTY_SCALE_TIME;
        scoreService.update(delta, speedMultiplier);
        enemyService.update(delta, cloud, bonusService);
        bonusService.update(delta, cloud, dropService, cloudService);

        if (inputBlockTimer <= 0f && !cloud.blocked) {
            inputService.update(cloud, camera, delta);
        }

        cloudService.update(cloud, delta);
        dropService.update(delta, cloud, cloudService, speedMultiplier);
        rainService.update(delta);
        flowerService.update(delta, rainService, speedMultiplier);

        cloud.position.x = Math.max(0, Math.min(GameConstants.SCREEN_WIDTH,  cloud.position.x));
        cloud.position.y = Math.max(GameConstants.FIELD_MIN_Y,
            Math.min(GameConstants.FIELD_MAX_Y, cloud.position.y));

        handleAutoPour();
        handleManualPour();
        if (cloud.dead) {
            state = GameState.GAME_OVER;
        }
    }
    private void handleAutoPour() {
        if (cloud.water >= GameConstants.AUTO_POUR_THRESHOLD
            && !rainService.isPouring()
            && !autoPourTriggered) {
            rainService.pour(cloud);
            cloudService.resetAfterRain(cloud);
            autoPourTriggered = true;
        }
        if (cloud.water < GameConstants.AUTO_POUR_THRESHOLD * 0.5f) {
            autoPourTriggered = false;
        }
    }
    private void handleManualPour() {
        boolean canRain = cloud.water >= GameConstants.RAIN_THRESHOLD
            && !rainService.isPouring();
        if (!canRain) return;
        if (Gdx.input.justTouched()) {
            Vector3 touch = unprojectTouch();
            if (pourButtonRect.contains(touch.x, touch.y)) {
                rainService.pour(cloud);
                cloudService.resetAfterRain(cloud);
                inputBlockTimer = 0.5f;
            }
        }
    }
    private void renderPlaying() {
        boolean canRain = cloud.water >= GameConstants.RAIN_THRESHOLD
            && !rainService.isPouring();
        float W = GameConstants.SCREEN_WIDTH;
        float H = GameConstants.SCREEN_HEIGHT;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(backgroundTexture, 0, 0, W, H);
        batch.end();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (canRain) {
            renderer.setColor(0.2f, 0.5f, 0.9f, 0.9f);
            drawRoundRect(pourButtonRect, 8);
        }
        drawWaterBar();
        renderer.end();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawCloud(cloud);
        drawDrops();
        drawRainDrops();
        drawFlowers();
        drawEnemies();
        drawBonuses();
        drawHUD(canRain);
        batch.end();
    }
    private void drawWaterBar() {
        renderer.setColor(0.25f, 0.25f, 0.25f, 0.7f);
        renderer.rect(10, 28, 154, 16);
        float fill = (cloud.water / GameConstants.WATER_MAX) * 150f;
        if (cloud.water > 70f) {
            renderer.setColor(0.1f, 0.5f, 0.95f, 1f);
        } else if (cloud.water > 35f) {
            renderer.setColor(0.3f, 0.75f, 1f, 1f);
        } else {
            renderer.setColor(0.6f, 0.85f, 1f, 1f);
        }
        renderer.rect(12, 30, fill, 12);
    }
    private void drawCloud(Cloud cloud) {
        if (cloud.hasShield) {
            batch.setColor(0.5f, 0.8f, 1f, 0.30f);
            float shieldSize = cloud.radius * 2 + 44;
            batch.draw(cloudTexture,
                cloud.position.x - cloud.radius - 22,
                cloud.position.y - cloud.radius - 22,
                shieldSize, shieldSize);
            batch.setColor(Color.WHITE);
        }
        float size    = cloud.radius * 2f;
        float originX = size / 2f;
        float originY = size / 2f;
        float drawX   = cloud.position.x - originX;
        float drawY   = cloud.position.y - originY;
        if (cloud.windRotation != 0f) {
            batch.draw(cloudTexture,
                drawX, drawY,
                originX, originY,
                size, size,
                1f, 1f,
                cloud.windRotation,
                0, 0,
                cloudTexture.getWidth(), cloudTexture.getHeight(),
                false, false);
        } else {
            batch.draw(cloudTexture, drawX, drawY, size, size);
        }
    }
    private void drawDrops() {
        for (Drop drop : dropService.getDrops()) {
            switch (drop.type) {
                case SPECIAL:
                    batch.setColor(Color.WHITE);
                    batch.draw(specialDropRegion,
                        drop.position.x - 9, drop.position.y - 11, 12, 14);
                    break;
                case MINI_CLOUD:
                    batch.setColor(Color.WHITE);
                    batch.draw(miniCloudRegion,
                        drop.position.x - 16, drop.position.y - 12, 32, 24);
                    break;
                default:
                    batch.draw(dropRegion,
                        drop.position.x - 6, drop.position.y - 8, 12, 16);
                    break;
            }
        }
        batch.setColor(Color.WHITE);
    }
    private void drawRainDrops() {
        for (RainDrop rd : rainService.getDrops()) {
            batch.draw(rainDropRegion, rd.x - 6, rd.y - 8, 12, 16);
        }
    }
    private void drawFlowers() {
        for (Flower flower : flowerService.getFlowers()) {
            TextureRegion region = flowerRegions[flower.type][flower.stage];
            batch.draw(region, flower.position.x - 24, 80, 48, 48);
        }
    }
    private void drawEnemies() {
        for (Enemy e : enemyService.getEnemies()) {
            Texture tex = getEnemyTexture(e);
            if (tex == null) continue;
            batch.setColor(1f, 1f, 1f, e.alpha);
            batch.draw(tex, e.position.x - 24, e.position.y - 24, 55, 55);
        }
        batch.setColor(Color.WHITE);
    }
    private Texture getEnemyTexture(Enemy e) {
        switch (e.type) {
            case SUN:   return sunTexture;
            case WIND:  return windTexture;
            case BIRD:  return birdTexture;
            case STORM: return stormTexture;
            default:    return null;
        }
    }
    private void drawBonuses() {
        for (Bonus b : bonusService.getBonuses()) {
            Texture tex = getBonusTexture(b);
            if (tex == null) continue;
            batch.setColor(1f, 1f, 1f, b.alpha);
            batch.draw(tex, b.position.x - 18, b.position.y - 18, 36, 36);
        }
        batch.setColor(Color.WHITE);
    }
    private Texture getBonusTexture(Bonus b) {
        switch (b.type) {
            case MAGNET:  return magnetTexture;
            case SHIELD:  return shieldTexture;
            case RAINBOW: return rainbowTexture;
            default:      return null;
        }
    }
    private void drawHUD(boolean canRain) {
        font.getData().setScale(1f);
        font.setColor(Color.YELLOW);
        font.draw(batch, "Score: " + scoreService.getScore(), 10, 470);
        font.draw(batch, "Best: "  + scoreService.getBestScore(), 10, 452);
        if (canRain) {
            font.setColor(Color.WHITE);
            font.draw(batch, "Pour!",
                GameConstants.POUR_BTN_X + 28, GameConstants.POUR_BTN_Y + 30);
        }
        if (cloud.blocked) {
            font.setColor(Color.RED);
            font.draw(batch, "STORM!", cloud.position.x - 30, cloud.position.y + 65);
        }
        if (cloud.windRotation != 0f) {
            font.setColor(Color.ORANGE);
            font.draw(batch, "WIND!", cloud.position.x - 22, cloud.position.y + 65);
        }
        for (ScoreService.FloatingText t : scoreService.getTexts()) {
            font.setColor(1f, 1f, 0f, Math.min(t.life, 1f));
            font.draw(batch, t.text, t.pos.x, t.pos.y);
        }
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
    }
    private void updateGameOver() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = unprojectTouch();
            if (restartButtonRect.contains(touch.x, touch.y)) {
                state = GameState.MENU;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            state = GameState.MENU;
        }
    }
    private void renderGameOver() {
        float W = GameConstants.SCREEN_WIDTH;
        float H = GameConstants.SCREEN_HEIGHT;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(backgroundTexture, 0, 0, W, H);
        batch.end();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(0f, 0f, 0f, 0.6f);
        renderer.rect(0, 0, W, H);

        renderer.setColor(PINK_BTN);
        drawRoundRect(restartButtonRect, 10);

        renderer.end();
        batch.begin();

        font.getData().setScale(3.2f);
        font.setColor(Color.RED);
        drawCentered("GAME OVER", W / 2f, 370);

        font.getData().setScale(1.6f);
        font.setColor(Color.WHITE);
        drawCentered("Score: " + scoreService.getScore(), W / 2f, 300);
        drawCentered("Best:  " + scoreService.getBestScore(), W / 2f, 270);

        font.getData().setScale(1.3f);
        font.setColor(Color.WHITE);
        drawCentered("Back to Menu", W / 2f, 200);

        font.getData().setScale(0.9f);
        font.setColor(Color.LIGHT_GRAY);
        drawCentered("Press R to return", W / 2f, 138);

        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        batch.end();
    }
    private void drawCentered(String text, float centerX, float y) {
        glyph.setText(font, text);
        font.draw(batch, text, centerX - glyph.width / 2f, y);
    }
    private void drawRoundRect(Rectangle r, float rad) {
        renderer.rect(r.x + rad, r.y,       r.width - rad * 2, r.height);
        renderer.rect(r.x,       r.y + rad, r.width,           r.height - rad * 2);
        renderer.circle(r.x + rad,           r.y + rad,           rad);
        renderer.circle(r.x + r.width - rad, r.y + rad,           rad);
        renderer.circle(r.x + rad,           r.y + r.height - rad, rad);
        renderer.circle(r.x + r.width - rad, r.y + r.height - rad, rad);
    }
    private Vector3 unprojectTouch() {
        tmpTouch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(tmpTouch);
        return tmpTouch;
    }
    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        menuBackgroundTexture.dispose();
        cloudTexture.dispose();
        dropTexture.dispose();
        specialDropTexture.dispose();
        miniCloudTexture.dispose();
        flowersTexture.dispose();
        sunTexture.dispose();
        windTexture.dispose();
        birdTexture.dispose();
        stormTexture.dispose();
        magnetTexture.dispose();
        shieldTexture.dispose();
        rainbowTexture.dispose();
    }
}
