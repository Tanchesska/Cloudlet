package io.github.cloudlet.service;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import io.github.cloudlet.storage.ScoreStorage;
public class ScoreService {
    private float score     = 0;
    private float bestScore = 0;
    private float timeAccumulator = 0f;
    public static class FloatingText {
        public Vector2 pos;
        public String  text;
        public float   life = 1f;
        public FloatingText(float x, float y, String text) {
            this.pos  = new Vector2(x, y);
            this.text = text;
        }
    }
    private final List<FloatingText> texts = new ArrayList<>();
    public ScoreService() {
        bestScore = ScoreStorage.loadBestScore();
    }
    public void addFlowerScore(int waterings, float x, float y) {
        int points = 0;
        switch (waterings) {
            case 1: points = 20;  break;
            case 2: points = 50;  break;
            case 3: points = 70;  break;
            case 4: points = 100; break;
        }
        score += points;
        updateBest();
        texts.add(new FloatingText(x, y, "+" + points));
    }
    public void update(float delta, float speedMultiplier) {
        timeAccumulator += delta * speedMultiplier;
        if (timeAccumulator >= 1f) {
            score += 1;
            timeAccumulator = 0f;
            updateBest();
        }
        Iterator<FloatingText> it = texts.iterator();
        while (it.hasNext()) {
            FloatingText t = it.next();
            t.life  -= delta;
            t.pos.y += 40 * delta;
            if (t.life <= 0) it.remove();
        }
    }
    private void updateBest() {
        if (score > bestScore) {
            bestScore = score;
            ScoreStorage.saveBestScore((int) bestScore);
        }
    }
    public int  getScore()     { return (int) score; }
    public int  getBestScore() { return (int) bestScore; }
    public List<FloatingText> getTexts() { return texts; }
}
