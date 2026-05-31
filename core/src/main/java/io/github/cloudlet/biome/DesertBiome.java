package io.github.cloudlet.biome;
public class DesertBiome implements Biome {
    @Override public BiomeType getType()                  { return BiomeType.DESERT; }
    @Override public String    getBackgroundTextureName() { return "background/desert_background.png"; }
    @Override public float     getWaterDrainMultiplier()  { return 1.5f; }
    @Override public float     getCloudSpeedMultiplier()  { return 1.0f; }
    @Override public float     getAcidDropChance()        { return 0f; }
    @Override public int       getSunSpawnWeight()        { return 3; }
    @Override public String    getDisplayName()           { return "DESERT"; }
}
