package io.github.cloudlet.achievement;

public enum AchievementType {

    NATURALIST(
        "Naturalist",
        "Reach 100 points without using any bonus"
    ),
    DROUGHT(
        "Drought",
        "Survive 60 seconds with water below 30%"
    ),
    LIGHTNING_ROD(
        "Lightning Rod",
        "Survive 3 StormEnemy attacks in a row without a shield"
    ),
    OWN_ATMOSPHERE(
        "Own Atmosphere",
        "Collect 10 mini-clouds in one game"
    ),
    PACIFIST(
        "Pacifist-Rescuer",
        "Grow a flower to maximum without missing a drop"
    );

    public final String title;
    public final String description;
    public final String reason;

    AchievementType(String title, String description) {
        this.title       = title;
        this.description = description;
        this.reason      = description;
    }
}
