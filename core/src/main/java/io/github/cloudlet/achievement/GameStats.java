package io.github.cloudlet.achievement;
public class GameStats {
    public boolean bonusPickedUp = false;
    /** Сколько секунд подряд вода держалась <= 30 % */
    public float   continuousLowWaterTime = 0f;
    public boolean droughtAchieved        = false;
    /** Сколько StormEnemy пережито подряд без щита */
    public int consecutiveStormsNoShield = 0;
    public int miniCloudsCollected = 0;
    /** Ни разу не пролил мимо цветка во время его роста до MAX */
    public boolean pacifistAchieved = false;
    /** true = за текущий цикл роста цветка не было промахов */
    public boolean currentFlowerNoMiss = true;
}
