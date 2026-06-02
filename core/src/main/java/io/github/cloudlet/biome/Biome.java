package io.github.cloudlet.biome;

public interface Biome {
    BiomeType   getType();
    String      getBackgroundTextureName();
    float       getWaterDrainMultiplier();
    float       getCloudSpeedMultiplier();
    float       getAcidDropChance();
    int         getSunSpawnWeight();
    String      getDisplayName();
}
