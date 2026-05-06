package io.github.cloudlet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.cloudlet.domain.*;
import io.github.cloudlet.service.*;

public class Main extends ApplicationAdapter {

    private static final float RAIN_WATER_THRESHOLD = 30f;
    private static final float CLOUD_AUTO_POUR = 80f;

    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private BitmapFont font;

    private Cloud cloud;

    private float gameTime = 0;

    private InputService inputService;
    private CloudService cloudService;
    private DropService dropService;
    private RainService rainService;
    private FlowerService flowerService;
    private ScoreService scoreService;
    private Texture flowersTexture;
    private TextureRegion[][] flowerRegions;
    private Texture cloudTexture;
    private Texture dropTexture;
    private TextureRegion dropRegion;
    private TextureRegion rainDropRegion;
    private float inputBlockTimer = 0f;
    private boolean autoPourTriggered = false;

    @Override
    public void create() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        cloud = new Cloud(400, 300);

        inputService = new InputService();
        cloudService = new CloudService();
        dropService = new DropService();
        rainService = new RainService();
        scoreService = new ScoreService();
        flowerService = new FlowerService(scoreService);

        cloudTexture = new Texture("blue_cloud.png");
        cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        dropTexture = new Texture("drops.png");
        dropTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        dropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);
        rainDropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);

        flowersTexture = new Texture("flowers.png");
        flowersTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        flowerRegions = new TextureRegion[3][4];

        flowerRegions[0][0] = new TextureRegion(flowersTexture, 160, 192, 32, 32);
        flowerRegions[0][1] = new TextureRegion(flowersTexture, 128, 192, 32, 32);
        flowerRegions[0][2] = new TextureRegion(flowersTexture, 96, 192, 32, 32);
        flowerRegions[0][3] = new TextureRegion(flowersTexture, 64, 192, 32, 32);

        flowerRegions[1][0] = new TextureRegion(flowersTexture, 96, 0, 32, 32);
        flowerRegions[1][1] = new TextureRegion(flowersTexture, 192, 160, 32, 32);
        flowerRegions[1][2] = new TextureRegion(flowersTexture, 0, 192, 32, 32);
        flowerRegions[1][3] = new TextureRegion(flowersTexture, 32, 192, 32, 32);

        flowerRegions[2][0] = new TextureRegion(flowersTexture, 32, 224, 32, 32);
        flowerRegions[2][1] = new TextureRegion(flowersTexture, 0, 224, 32, 32);
        flowerRegions[2][2] = new TextureRegion(flowersTexture, 224, 96, 32, 32);
        flowerRegions[2][3] = new TextureRegion(flowersTexture, 128, 96, 32, 32);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();
        inputBlockTimer -= delta;
        gameTime += delta;

        float speedMultiplier = 1f + gameTime / 90f;
        scoreService.update(delta, speedMultiplier);


        if (inputBlockTimer <= 0f) {
            inputService.update(cloud, camera, delta);
        }
        cloudService.update(cloud, delta);
        dropService.update(delta, cloud, cloudService, speedMultiplier);
        rainService.update(delta);
        flowerService.update(delta, rainService, speedMultiplier);

        cloud.position.x = Math.max(0, Math.min(800, cloud.position.x));
        cloud.position.y = Math.max(120, Math.min(480, cloud.position.y));

        boolean canRain = cloud.water >= RAIN_WATER_THRESHOLD && !rainService.isPouring();


        if (cloud.water >= CLOUD_AUTO_POUR && !rainService.isPouring() && !autoPourTriggered) {
            rainService.pour(cloud);
            cloud.water = 0;
            autoPourTriggered = true;
        }

        if (cloud.water < CLOUD_AUTO_POUR * 0.6f) {
            autoPourTriggered = false;
        }


        Rectangle buttonRect = new Rectangle(680, 20, 120, 50);

        if (Gdx.input.justTouched()) {

            Vector3 touch = camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (canRain && buttonRect.contains(touch.x, touch.y)) {
                rainService.pour(cloud);
                cloud.water = 0;
                inputBlockTimer = 0.85f;
            }
        }


        Gdx.gl.glClearColor(0.7f, 0.85f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);


        renderer.setColor(Color.FOREST);
        renderer.rect(0, 0, 800, 100);


        if (canRain) {
            renderer.setColor(0.2f, 0.5f, 0.9f, 0.85f);
            renderer.rect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
        }

        renderer.end();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawCloud(cloud);
        font.setColor(Color.YELLOW);
        font.draw(batch, "Score: " + scoreService.getScore(), 10, 470);
        font.draw(batch, "Best: " + scoreService.getBestScore(), 10, 440);
        for (ScoreService.FloatingText t : scoreService.getTexts()) {

            font.setColor(1f, 1f, 0f, 1f);

            font.draw(
                batch,
                t.text,
                t.pos.x,
                t.pos.y
            );
        }
        for (Drop drop : dropService.getDrops()) {
            batch.draw(
                dropRegion,
                drop.position.x - 6,
                drop.position.y - 8,
                12,
                16
            );
        }


        for (RainDrop rd : rainService.getDrops()) {
            batch.draw(
                rainDropRegion,
                rd.x - 6,
                rd.y - 8,
                12,
                16
            );
        }
        for (Flower flower : flowerService.getFlowers()) {

            TextureRegion region = flowerRegions[flower.type][flower.stage];

            batch.draw(region,
                flower.position.x - 24,
                80,
                48,
                48
            );
        }
        font.setColor(Color.WHITE);
        if (canRain) {
            font.draw(batch, "Pour!", 700, 50);
        }

        batch.end();
    }

private void drawCloud(Cloud cloud) {

    float size = cloud.radius * 2f;

    batch.draw(
        cloudTexture,
        cloud.position.x - size / 2,
        cloud.position.y - size / 2,
        size,
        size
    );
}

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
        flowersTexture.dispose();
        dropTexture.dispose();

    }
}
