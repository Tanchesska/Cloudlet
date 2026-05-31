package io.github.cloudlet.biome;
public class ToxicBiome implements Biome {
    @Override public BiomeType getType()                  { return BiomeType.TOXIC; }
    @Override public String    getBackgroundTextureName() { return "background/toxic_background.png"; }
    @Override public float     getWaterDrainMultiplier()  { return 1.0f; }
    @Override public float     getCloudSpeedMultiplier()  { return 1.0f; }
    @Override public float     getAcidDropChance()        { return 0.12f; }
    @Override public int       getSunSpawnWeight()        { return 1; }
    @Override public String    getDisplayName()           { return "TOXIC"; }
}
