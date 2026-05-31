package io.github.cloudlet.biome;

import io.github.cloudlet.constants.GameConstants;

public class BiomeManager {

    private Biome   currentBiome      = new MeadowBiome();
    private boolean transitioning     = false;
    private float   transitionTimer   = 0f;
    private String  transitionMessage = "";
    public boolean update(int score, float delta) {
        if (transitioning) {
            transitionTimer -= delta;
            if (transitionTimer <= 0f) transitioning = false;
        }

        BiomeType desired = biomeForScore(score);
        if (desired != currentBiome.getType()) {
            currentBiome      = createBiome(desired);
            transitioning     = true;
            transitionTimer   = GameConstants.Biome.TRANSITION_DISPLAY_DURATION;
            transitionMessage = currentBiome.getDisplayName() + "!";
            return true;
        }
        return false;
    }

    public Biome   getCurrentBiome()       { return currentBiome; }
    public boolean isTransitioning()       { return transitioning; }
    public String  getTransitionMessage()  { return transitionMessage; }

    private static BiomeType biomeForScore(int score) {
        int pos = score % GameConstants.Biome.CYCLE_LENGTH;
        if (pos >= GameConstants.Biome.SCORE_THRESHOLD_TOXIC)  return BiomeType.TOXIC;
        if (pos >= GameConstants.Biome.SCORE_THRESHOLD_TUNDRA) return BiomeType.TUNDRA;
        if (pos >= GameConstants.Biome.SCORE_THRESHOLD_DESERT) return BiomeType.DESERT;
        return BiomeType.MEADOW;
    }

    private static Biome createBiome(BiomeType type) {
        switch (type) {
            case DESERT: return new DesertBiome();
            case TUNDRA: return new TundraBiome();
            case TOXIC:  return new ToxicBiome();
            default:     return new MeadowBiome();
        }
    }
}
