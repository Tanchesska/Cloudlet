package io.github.cloudlet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class GameAssets implements Disposable {
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;
    public GlyphLayout glyph;

    public Texture backgroundTexture, menuBackgroundTexture, cloudTexture, dropTexture,
        specialDropTexture, miniCloudTexture, flowersTexture, sunTexture, windTexture,
        birdTexture, stormTexture, magnetTexture, shieldTexture, rainbowTexture;

    public TextureRegion dropRegion, rainDropRegion, specialDropRegion, miniCloudRegion;
    public TextureRegion[][] flowerRegions;

    public void load() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        glyph = new GlyphLayout();

        backgroundTexture = new Texture("background.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        menuBackgroundTexture = new Texture("menu_background.png");
        menuBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cloudTexture = new Texture("blue_cloud.png");
        cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        dropTexture = new Texture("drop.png");
        dropTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        dropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);
        rainDropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);

        specialDropTexture = new Texture("purple_drop.png");
        specialDropTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        specialDropRegion = new TextureRegion(specialDropTexture, 0, 0, specialDropTexture.getWidth(), specialDropTexture.getHeight());

        miniCloudTexture = new Texture("pink_cloud.png");
        miniCloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        miniCloudRegion = new TextureRegion(miniCloudTexture, 0, 0, miniCloudTexture.getWidth(), miniCloudTexture.getHeight());

        flowersTexture = new Texture("flowers.png");
        flowersTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        initFlowerRegions();

        sunTexture = new Texture("sun.png");
        windTexture = new Texture("wind2.png");
        birdTexture = new Texture("bird.png");
        stormTexture = new Texture("storm.png");

        magnetTexture = new Texture("magnit.png");
        shieldTexture = new Texture("shield.png");
        rainbowTexture = new Texture("rainbow.png");
    }

    private void initFlowerRegions() {
        flowerRegions = new TextureRegion[3][4];
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

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
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
