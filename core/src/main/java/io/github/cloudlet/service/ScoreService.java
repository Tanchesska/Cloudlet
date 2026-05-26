package io.github.cloudlet.service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import io.github.cloudlet.constants.GameConstants;
import io.github.cloudlet.domain.FloatingText;
import io.github.cloudlet.storage.ScoreStorage;
public class ScoreService {
    private float score           = 0f;
    private float bestScore       = 0f;
    private float timeAccumulator = 0f;
    private final List<FloatingText> texts = new ArrayList<>();
    public ScoreService() {
        bestScore = ScoreStorage.loadBestScore();
    }
    public void update(float delta, float speedMultiplier) {
        timeAccumulator += delta * speedMultiplier;
        if (timeAccumulator >= 1f) {
            score += 1f;
            timeAccumulator = 0f;
            updateBest();
        }
        Iterator<FloatingText> it = texts.iterator();
        while (it.hasNext()) {
            FloatingText t = it.next();
            t.update(delta);
            if (t.isExpired()) it.remove();
        }
    }
    public void addFlowerScore(int waterings, float x, float y) {
        int points = scoreForWaterings(waterings);
        score += points;
        updateBest();
        texts.add(new FloatingText(x, y, "+" + points));
    }
    private int scoreForWaterings(int waterings) {
        switch (waterings) {
            case 1: return GameConstants.FLOWER_SCORE_1;
            case 2: return GameConstants.FLOWER_SCORE_2;
            case 3: return GameConstants.FLOWER_SCORE_3;
            case 4: return GameConstants.FLOWER_SCORE_4;
            default: return 0;
        }
    }
    private void updateBest() {
        if (score > bestScore) {
            bestScore = score;
            ScoreStorage.saveBestScore((int) bestScore);
        }
    }
    public int              getScore()     { return (int) score; }
    public int              getBestScore() { return (int) bestScore; }
    public List<FloatingText> getTexts()  { return texts; }
}
