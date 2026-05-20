package io.github.cloudlet.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreStorage {
    private static final String FILE_NAME = "best_score.txt";
    public static int loadBestScore() {
        try {
            FileHandle file = Gdx.files.local(FILE_NAME);
            if (file.exists()) {
                String content = file.readString().trim();
                return Integer.parseInt(content);
            }
        } catch (Exception e) {
            Gdx.app.log("ScoreStorage", "Failed to load: " + e.getMessage());
        }
        return 0;
    }
    public static void saveBestScore(int score) {
        try {
            FileHandle file = Gdx.files.local(FILE_NAME);
            file.writeString(String.valueOf(score), false);
        } catch (Exception e) {
            Gdx.app.log("ScoreStorage", "Failed to save: " + e.getMessage());
        }
    }
}
