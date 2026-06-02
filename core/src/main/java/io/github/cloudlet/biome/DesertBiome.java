package io.github.cloudlet.biome;

import io.github.cloudlet.constants.GameConstants;
public class DesertBiome implements Biome {
    @Override public BiomeType getType()                  { return BiomeType.DESERT; }
    @Override public String    getBackgroundTextureName() { return GameConstants.Textures.BACKGROUND_DESERT; }
    @Override public float     getWaterDrainMultiplier()  { return 1.5f; }
    @Override public float     getCloudSpeedMultiplier()  { return 1.0f; }
    @Override public float     getAcidDropChance()        { return 0f; }
    @Override public int       getSunSpawnWeight()        { return 3; }
    @Override public String    getDisplayName()           { return "DESERT"; }
}
