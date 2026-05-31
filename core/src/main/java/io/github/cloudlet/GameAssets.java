package io.github.cloudlet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import io.github.cloudlet.biome.BiomeType;

public class GameAssets implements Disposable {

    public SpriteBatch   batch;
    public ShapeRenderer renderer;
    public BitmapFont    font;
    public GlyphLayout   glyph;
    public Texture backgroundTexture, menuBackgroundTexture, cloudTexture,
        dropTexture, specialDropTexture, miniCloudTexture,
        flowersTexture, sunTexture,
        magnetTexture, shieldTexture, rainbowTexture;

    public Texture desertBackgroundTexture;
    public Texture tundraBackgroundTexture;
    public Texture toxicBackgroundTexture;
    public Texture       acidDropTexture;
    public TextureRegion acidDropRegion;
    public TextureRegion dropRegion, rainDropRegion;
    public Texture[][] enemyBiomeTextures;
    public TextureRegion[][][] flowerBiomeRegions;
    public TextureRegion[] specialDropBiomeRegion;
    public TextureRegion[] miniCloudBiomeRegion;
    private Texture flowerDesertTexture;
    private Texture flowerTundraTexture;
    private Texture flowerToxicTexture;
    private Texture birdMeadow, windMeadow, stormMeadow;
    private Texture birdDesert, windDesert, stormDesert;
    private Texture birdTundra, windTundra, stormTundra;
    private Texture birdToxic,  windToxic,  stormToxic;
    private Texture specialDropDesert, specialDropTundra;
    private Texture miniCloudDesert, miniCloudTundra, miniCloudToxic;

    public void load() {
        batch    = new SpriteBatch();
        renderer = new ShapeRenderer();
        font     = new BitmapFont();
        glyph    = new GlyphLayout();

        backgroundTexture = new Texture("background/background.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        menuBackgroundTexture = new Texture("background/menu_background.png");
        menuBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        desertBackgroundTexture = new Texture("background/desert_background.png");
        desertBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        tundraBackgroundTexture = new Texture("background/tundra_background.png");
        tundraBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        toxicBackgroundTexture = new Texture("background/toxic_background.png");
        toxicBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cloudTexture = new Texture("main_characters/blue_cloud.png");
        cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        dropTexture = new Texture("drop/drop.png");
        dropTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        dropRegion     = new TextureRegion(dropTexture, 0, 0, 6, 8);
        rainDropRegion = new TextureRegion(dropTexture, 0, 0, 6, 8);

        specialDropTexture = new Texture("bonus/purple_drop.png");
        specialDropTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        miniCloudTexture = new Texture("bonus/pink_cloud.png");
        miniCloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        acidDropTexture = new Texture("drop/acid_drop.png");
        acidDropTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        acidDropRegion  = new TextureRegion(acidDropTexture, 0, 0,
            acidDropTexture.getWidth(), acidDropTexture.getHeight());

        sunTexture = new Texture("enemy/sun.png");

        magnetTexture  = new Texture("bonus/magnit.png");
        shieldTexture  = new Texture("bonus/shield.png");
        rainbowTexture = new Texture("bonus/rainbow.png");

        flowersTexture = new Texture("main_characters/flowers.png");
        flowersTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        initEnemyTextures();
        initFlowerBiomeRegions();
        initDropBiomeRegions();
    }
    public Texture getBackgroundForBiome(BiomeType type) {
        switch (type) {
            case DESERT: return desertBackgroundTexture;
            case TUNDRA: return tundraBackgroundTexture;
            case TOXIC:  return toxicBackgroundTexture;
            default:     return backgroundTexture;
        }
    }

    private void initEnemyTextures() {

        birdMeadow  = new Texture("enemy/bird.png");
        windMeadow  = new Texture("enemy/wind2.png");
        stormMeadow = new Texture("enemy/storm.png");

        birdDesert  = new Texture("enemy/bird_desert.png");
        windDesert  = new Texture("enemy/wind_desert.png");
        stormDesert = new Texture("enemy/storm_desert.png");

        birdTundra  = new Texture("enemy/bird_tundra.png");
        windTundra  = new Texture("enemy/wind_tundra.png");
        stormTundra = new Texture("enemy/storm_tundra.png");

        birdToxic  = new Texture("enemy/bird_toxic.png");
        windToxic  = new Texture("enemy/wind_toxic.png");
        stormToxic = new Texture("enemy/storm_toxic.png");

        enemyBiomeTextures = new Texture[][]{
            { birdMeadow, windMeadow, stormMeadow },
            { birdDesert, windDesert, stormDesert },
            { birdTundra, windTundra, stormTundra },
            { birdToxic,  windToxic,  stormToxic  },
        };
    }
    private void initFlowerBiomeRegions() {
        flowerBiomeRegions = new TextureRegion[4][3][4];

        flowerBiomeRegions[0][0][0] = new TextureRegion(flowersTexture, 160, 192, 32, 32);
        flowerBiomeRegions[0][0][1] = new TextureRegion(flowersTexture, 128, 192, 32, 32);
        flowerBiomeRegions[0][0][2] = new TextureRegion(flowersTexture,  96, 192, 32, 32);
        flowerBiomeRegions[0][0][3] = new TextureRegion(flowersTexture,  64, 192, 32, 32);

        flowerBiomeRegions[0][1][0] = new TextureRegion(flowersTexture,  96,   0, 32, 32);
        flowerBiomeRegions[0][1][1] = new TextureRegion(flowersTexture, 192, 160, 32, 32);
        flowerBiomeRegions[0][1][2] = new TextureRegion(flowersTexture,   0, 192, 32, 32);
        flowerBiomeRegions[0][1][3] = new TextureRegion(flowersTexture,  32, 192, 32, 32);

        flowerBiomeRegions[0][2][0] = new TextureRegion(flowersTexture,  32, 224, 32, 32);
        flowerBiomeRegions[0][2][1] = new TextureRegion(flowersTexture,   0, 224, 32, 32);
        flowerBiomeRegions[0][2][2] = new TextureRegion(flowersTexture, 224,  96, 32, 32);
        flowerBiomeRegions[0][2][3] = new TextureRegion(flowersTexture, 128,  96, 32, 32);

        flowerDesertTexture = new Texture("main_characters/flowers_desert.png");
        flowerDesertTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        flowerBiomeRegions[1][0][0] = new TextureRegion(flowerDesertTexture, 0, 0, 24, 32);
        flowerBiomeRegions[1][0][1] = new TextureRegion(flowerDesertTexture, 24, 0, 24, 32);
        flowerBiomeRegions[1][0][2] = new TextureRegion(flowerDesertTexture,  48, 0, 24, 32);
        flowerBiomeRegions[1][0][3] = new TextureRegion(flowerDesertTexture,  72, 0, 24, 32);

        flowerBiomeRegions[1][1][0] = new TextureRegion(flowerDesertTexture,  0,   32, 24, 32);
        flowerBiomeRegions[1][1][1] = new TextureRegion(flowerDesertTexture, 24, 32, 24, 32);
        flowerBiomeRegions[1][1][2] = new TextureRegion(flowerDesertTexture,   48, 32, 24, 32);
        flowerBiomeRegions[1][1][3] = new TextureRegion(flowerDesertTexture,  72, 32, 24, 32);

        flowerBiomeRegions[1][2][0] = new TextureRegion(flowerDesertTexture,  0, 64, 24, 32);
        flowerBiomeRegions[1][2][1] = new TextureRegion(flowerDesertTexture,   24, 64, 24, 32);
        flowerBiomeRegions[1][2][2] = new TextureRegion(flowerDesertTexture, 48,  64, 24, 32);
        flowerBiomeRegions[1][2][3] = new TextureRegion(flowerDesertTexture, 72,  64, 24, 32);

        flowerTundraTexture = new Texture("main_characters/flowers_tundra.png");
        flowerTundraTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        flowerBiomeRegions[2][0][0] = new TextureRegion(flowerTundraTexture, 0, 0, 256, 341);
        flowerBiomeRegions[2][0][1] = new TextureRegion(flowerTundraTexture, 256, 0, 256, 341);
        flowerBiomeRegions[2][0][2] = new TextureRegion(flowerTundraTexture,  512, 0, 256, 341);
        flowerBiomeRegions[2][0][3] = new TextureRegion(flowerTundraTexture,  768, 0, 256, 341);

        flowerBiomeRegions[2][1][0] = new TextureRegion(flowerTundraTexture,  0,   341, 256, 341);
        flowerBiomeRegions[2][1][1] = new TextureRegion(flowerTundraTexture, 256, 341, 256, 341);
        flowerBiomeRegions[2][1][2] = new TextureRegion(flowerTundraTexture,   512, 341, 256, 341);
        flowerBiomeRegions[2][1][3] = new TextureRegion(flowerTundraTexture,  768, 341, 256, 341);

        flowerBiomeRegions[2][2][0] = new TextureRegion(flowerTundraTexture,  0, 682, 256, 341);
        flowerBiomeRegions[2][2][1] = new TextureRegion(flowerTundraTexture,   256, 682, 256, 341);
        flowerBiomeRegions[2][2][2] = new TextureRegion(flowerTundraTexture, 512,  682, 256, 341);
        flowerBiomeRegions[2][2][3] = new TextureRegion(flowerTundraTexture, 768,  682, 256, 341);

        flowerToxicTexture = new Texture("main_characters/flowers_toxic.png");
        flowerToxicTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        flowerBiomeRegions[3][0][0] = new TextureRegion(flowerToxicTexture, 0, 0, 310, 310);
        flowerBiomeRegions[3][0][1] = new TextureRegion(flowerToxicTexture, 310, 0, 310, 310);
        flowerBiomeRegions[3][0][2] = new TextureRegion(flowerToxicTexture,  620, 0, 310, 310);
        flowerBiomeRegions[3][0][3] = new TextureRegion(flowerToxicTexture,  930, 0, 310, 310);

        flowerBiomeRegions[3][1][0] = new TextureRegion(flowerToxicTexture,  0,   310, 310, 276);
        flowerBiomeRegions[3][1][1] = new TextureRegion(flowerToxicTexture, 310, 310, 310, 276);
        flowerBiomeRegions[3][1][2] = new TextureRegion(flowerToxicTexture,   620, 310, 310, 276);
        flowerBiomeRegions[3][1][3] = new TextureRegion(flowerToxicTexture,  930, 310, 310, 276);

        flowerBiomeRegions[3][2][0] = new TextureRegion(flowerToxicTexture,  0, 586, 290, 374);
        flowerBiomeRegions[3][2][1] = new TextureRegion(flowerToxicTexture,   290, 586, 330, 374);
        flowerBiomeRegions[3][2][2] = new TextureRegion(flowerToxicTexture, 620,  586, 310, 374);
        flowerBiomeRegions[3][2][3] = new TextureRegion(flowerToxicTexture, 930,  586, 310, 374);
    }
    private void initDropBiomeRegions() {
        specialDropDesert = new Texture("bonus/special_drop_desert.png");
        specialDropDesert.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        specialDropTundra = new Texture("bonus/special_drop_tundra.png");
        specialDropTundra.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        miniCloudDesert = new Texture("bonus/mini_cloud_desert.png");
        miniCloudDesert.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        miniCloudTundra = new Texture("bonus/mini_cloud_tundra.png");
        miniCloudTundra.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        miniCloudToxic  = new Texture("bonus/mini_cloud_toxic.png");
        miniCloudToxic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        specialDropBiomeRegion = new TextureRegion[]{
            new TextureRegion(specialDropTexture),
            new TextureRegion(specialDropDesert),
            new TextureRegion(specialDropTundra),
            new TextureRegion(specialDropTexture),
        };
        miniCloudBiomeRegion = new TextureRegion[]{
            new TextureRegion(miniCloudTexture),
            new TextureRegion(miniCloudDesert),
            new TextureRegion(miniCloudTundra),
            new TextureRegion(miniCloudToxic),
        };
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        font.dispose();

        backgroundTexture.dispose();
        menuBackgroundTexture.dispose();
        desertBackgroundTexture.dispose();
        tundraBackgroundTexture.dispose();
        toxicBackgroundTexture.dispose();

        cloudTexture.dispose();
        dropTexture.dispose();
        specialDropTexture.dispose();
        miniCloudTexture.dispose();
        acidDropTexture.dispose();
        flowersTexture.dispose();
        sunTexture.dispose();
        magnetTexture.dispose();
        shieldTexture.dispose();
        rainbowTexture.dispose();

        birdMeadow.dispose();  windMeadow.dispose();  stormMeadow.dispose();
        birdDesert.dispose();  windDesert.dispose();  stormDesert.dispose();
        birdTundra.dispose();  windTundra.dispose();  stormTundra.dispose();
        birdToxic.dispose();   windToxic.dispose();   stormToxic.dispose();

        if (flowerDesertTexture != null) flowerDesertTexture.dispose();
        if (flowerTundraTexture != null) flowerTundraTexture.dispose();
        if (flowerToxicTexture  != null) flowerToxicTexture.dispose();

        if (specialDropDesert != null) specialDropDesert.dispose();
        if (specialDropTundra != null) specialDropTundra.dispose();
        if (miniCloudDesert   != null) miniCloudDesert.dispose();
        if (miniCloudTundra   != null) miniCloudTundra.dispose();
        if (miniCloudToxic    != null) miniCloudToxic.dispose();
    }
}
