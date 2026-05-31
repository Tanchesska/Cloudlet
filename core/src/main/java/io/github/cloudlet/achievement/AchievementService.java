package io.github.cloudlet.achievement;

import io.github.cloudlet.storage.AchievementStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class AchievementService {
    private final Set<AchievementType> unlocked;
    public AchievementService() {
        unlocked = AchievementStorage.loadUnlocked();
    }
    public List<AchievementType> checkOnGameOver(GameStats stats, int score) {
        List<AchievementType> newlyUnlocked = new ArrayList<>();

        tryUnlock(AchievementType.NATURALIST,
            score >= 100 && !stats.bonusPickedUp,
            newlyUnlocked);

        tryUnlock(AchievementType.DROUGHT,
            stats.droughtAchieved,
            newlyUnlocked);

        tryUnlock(AchievementType.LIGHTNING_ROD,
            stats.consecutiveStormsNoShield >= 3,
            newlyUnlocked);

        tryUnlock(AchievementType.OWN_ATMOSPHERE,
            stats.miniCloudsCollected >= 10,
            newlyUnlocked);

        tryUnlock(AchievementType.PACIFIST,
            stats.pacifistAchieved,
            newlyUnlocked);

        if (!newlyUnlocked.isEmpty()) {
            AchievementStorage.saveUnlocked(unlocked);
        }
        return newlyUnlocked;
    }

    private void tryUnlock(AchievementType type, boolean condition,
                           List<AchievementType> out) {
        if (condition && !unlocked.contains(type)) {
            unlocked.add(type);
            out.add(type);
        }
    }
}
