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
        dropTexture, specialDropTexture, miniCloudTexture, sunTexture,
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
    private com.badlogic.gdx.graphics.Texture[] flowerTextures;
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
        com.badlogic.gdx.utils.JsonReader reader = new com.badlogic.gdx.utils.JsonReader();
        com.badlogic.gdx.utils.JsonValue biomes = reader.parse(com.badlogic.gdx.Gdx.files.internal("config/flowers_config.json"));

        int biomeCount = biomes.size;
        flowerBiomeRegions = new com.badlogic.gdx.graphics.g2d.TextureRegion[biomeCount][][];
        flowerTextures = new com.badlogic.gdx.graphics.Texture[biomeCount];

        for (int b = 0; b < biomeCount; b++) {
            com.badlogic.gdx.utils.JsonValue biomeJson = biomes.get(b);

            String texturePath = biomeJson.getString("texture");
            com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(com.badlogic.gdx.Gdx.files.internal(texturePath));
            texture.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest, com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest);

            flowerTextures[b] = texture;

            int defaultW = biomeJson.getInt("tileWidth", 0);
            int defaultH = biomeJson.getInt("tileHeight", 0);

            com.badlogic.gdx.utils.JsonValue flowersJson = biomeJson.get("flowers");
            int typeCount = flowersJson.size;
            flowerBiomeRegions[b] = new com.badlogic.gdx.graphics.g2d.TextureRegion[typeCount][];

            for (int t = 0; t < typeCount; t++) {
                com.badlogic.gdx.utils.JsonValue flowerJson = flowersJson.get(t);
                com.badlogic.gdx.utils.JsonValue stagesJson = flowerJson.get("stages");
                int stageCount = stagesJson.size;
                flowerBiomeRegions[b][t] = new com.badlogic.gdx.graphics.g2d.TextureRegion[stageCount];

                for (int s = 0; s < stageCount; s++) {
                    com.badlogic.gdx.utils.JsonValue stage = stagesJson.get(s);
                    int x = stage.getInt("x");
                    int y = stage.getInt("y");
                    int w = stage.getInt("w", defaultW);
                    int h = stage.getInt("h", defaultH);

                    flowerBiomeRegions[b][t][s] = new com.badlogic.gdx.graphics.g2d.TextureRegion(texture, x, y, w, h);
                }
            }
        }
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
        sunTexture.dispose();
        magnetTexture.dispose();
        shieldTexture.dispose();
        rainbowTexture.dispose();

        birdMeadow.dispose();  windMeadow.dispose();  stormMeadow.dispose();
        birdDesert.dispose();  windDesert.dispose();  stormDesert.dispose();
        birdTundra.dispose();  windTundra.dispose();  stormTundra.dispose();
        birdToxic.dispose();   windToxic.dispose();   stormToxic.dispose();

        if (flowerTextures != null) {
            for (com.badlogic.gdx.graphics.Texture tex : flowerTextures) {
                if (tex != null) tex.dispose();
            }
        }

        if (specialDropDesert != null) specialDropDesert.dispose();
        if (specialDropTundra != null) specialDropTundra.dispose();
        if (miniCloudDesert   != null) miniCloudDesert.dispose();
        if (miniCloudTundra   != null) miniCloudTundra.dispose();
        if (miniCloudToxic    != null) miniCloudToxic.dispose();
    }
}
