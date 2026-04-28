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

    private Texture flowersTexture;
    private TextureRegion[][] flowerRegions;

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
        flowerService = new FlowerService();

        flowersTexture = new Texture("flowers.png");
        flowersTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        flowerRegions = new TextureRegion[3][4];

        flowerRegions[0][0] = new TextureRegion(flowersTexture, 20, 55, 9, 9);
        flowerRegions[0][1] = new TextureRegion(flowersTexture, 20, 40, 9, 9);
        flowerRegions[0][2] = new TextureRegion(flowersTexture, 17, 132, 14, 14);
        flowerRegions[0][3] = new TextureRegion(flowersTexture, 15, 145, 13, 16);

        flowerRegions[1][0] = new TextureRegion(flowersTexture, 36, 55, 9, 9);
        flowerRegions[1][1] = new TextureRegion(flowersTexture, 36, 40, 9, 9);
        flowerRegions[1][2] = new TextureRegion(flowersTexture, 33, 132, 14, 14);
        flowerRegions[1][3] = new TextureRegion(flowersTexture, 33, 145, 13, 16);

        flowerRegions[2][0] = new TextureRegion(flowersTexture, 52, 55, 9, 9);
        flowerRegions[2][1] = new TextureRegion(flowersTexture, 52, 40, 9, 10);
        flowerRegions[2][2] = new TextureRegion(flowersTexture, 50, 132, 14, 14);
        flowerRegions[2][3] = new TextureRegion(flowersTexture, 50, 145, 13, 16);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();
        gameTime += delta;

        float speedMultiplier = 1f + gameTime / 90f;


        inputService.update(cloud, camera, delta);
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
            }
        }


        Gdx.gl.glClearColor(0.5f, 0.7f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);


        renderer.setColor(Color.FOREST);
        renderer.rect(0, 0, 800, 100);


        renderer.setColor(Color.BLUE);
        for (Drop drop : dropService.getDrops()) {
            renderer.circle(drop.position.x, drop.position.y, drop.radius);
        }

        renderer.setColor(0.3f, 0.6f, 1f, 1f);
        for (RainDrop rd : rainService.getDrops()) {
            renderer.ellipse(rd.x - 3, rd.y - 6, 6, 12);
        }

        drawCloud(cloud);

        if (canRain) {
            renderer.setColor(0.2f, 0.5f, 0.9f, 0.85f);
            renderer.rect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
        }

        renderer.end();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Flower flower : flowerService.getFlowers()) {

            TextureRegion region = flowerRegions[flower.type][flower.stage];

            batch.draw(region,
                flower.position.x - 24,
                100,
                48,
                48
            );
        }

        if (canRain) {
            font.draw(batch, "Pour!", 700, 50);
        }

        batch.end();
    }

    private void drawCloud(Cloud cloud) {

        float x = cloud.position.x;
        float y = cloud.position.y;
        float r = cloud.radius;

        renderer.setColor(Color.WHITE);
        renderer.circle(x, y, r);
        renderer.circle(x - 0.55f * r, y + 0.25f * r, 0.55f * r);
        renderer.circle(x + 0.50f * r, y + 0.20f * r, 0.50f * r);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
        flowersTexture.dispose();
    }
}
