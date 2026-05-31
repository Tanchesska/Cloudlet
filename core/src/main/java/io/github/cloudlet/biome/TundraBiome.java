package io.github.cloudlet.biome;
public class TundraBiome implements Biome {
    @Override public BiomeType getType()                  { return BiomeType.TUNDRA; }
    @Override public String    getBackgroundTextureName() { return "background/tundra_background.png"; }
    @Override public float     getWaterDrainMultiplier()  { return 1.0f; }
    @Override public float     getCloudSpeedMultiplier()  { return 0.8f; }
    @Override public float     getAcidDropChance()        { return 0f; }
    @Override public int       getSunSpawnWeight()        { return 1; }
    @Override public String    getDisplayName()           { return "TUNDRA"; }
}
