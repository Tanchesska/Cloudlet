package io.github.cloudlet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import io.github.cloudlet.Main;
import io.github.cloudlet.achievement.AchievementType;
import io.github.cloudlet.constants.GameConstants;

import java.util.List;

public class GameOverScreen implements Screen {

    private final Main                  game;
    private final OrthographicCamera    camera;
    private final int                   score;
    private final int                   bestScore;
    private final List<AchievementType> newAchievements;
    private final Rectangle             restartButtonRect;

    private static final Color PINK_BTN = new Color(0.87f, 0.27f, 0.54f, 1f);
    private static final Color GOLD     = new Color(1f, 0.84f, 0f, 1f);
    private static final float Y_GAME_OVER  = 460f;
    private static final float Y_SCORE      = 408f;
    private static final float Y_BEST       = 383f;
    private static final float Y_ACH_HEADER = 350f;
    private static final float Y_ACH_START  = 326f;
    private static final float ACH_CARD_H   = 44f;
    private static final float Y_BUTTON     = 75f;
    private static final float Y_HINT       = 32f;
    private static final float ACH_PANEL_WIDTH        = 500f;
    private static final float ACH_PANEL_PADDING_SIDE = 10f;
    private static final float ACH_PANEL_EXTRA_H      = 6f;
    private static final float ACH_TOP_BORDER_H       = 3f;
    private static final float ACH_DIVIDER_H          = 1f;
    private static final float ACH_TITLE_OFFSET_X     = 12f;
    private static final float ACH_TITLE_OFFSET_Y     = 6f;
    private static final float ACH_DESC_OFFSET_X      = 20f;
    private static final float ACH_DESC_OFFSET_Y      = 22f;
    private static final float ACH_PANEL_BG_ALPHA     = 0.90f;
    private static final float ACH_DIVIDER_COLOR      = 0.28f;

    public GameOverScreen(Main game, int score, int bestScore) {
        this(game, score, bestScore, null);
    }

    public GameOverScreen(Main game, int score, int bestScore,
                          List<AchievementType> newAchievements) {
        this.game            = game;
        this.score           = score;
        this.bestScore       = bestScore;
        this.newAchievements = newAchievements;
        this.camera          = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.Screen.WIDTH, GameConstants.Screen.HEIGHT);
        this.restartButtonRect = new Rectangle(300f, Y_BUTTON - 10f, 200f, 50f);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        float W = GameConstants.Screen.WIDTH;
        float H = GameConstants.Screen.HEIGHT;

        game.assets.batch.setProjectionMatrix(camera.combined);
        game.assets.batch.begin();
        game.assets.batch.setColor(Color.WHITE);
        game.assets.batch.draw(game.assets.backgroundTexture, 0, 0, W, H);
        game.assets.batch.end();
        game.assets.renderer.setProjectionMatrix(camera.combined);
        game.assets.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.assets.renderer.setColor(0f, 0f, 0f, 0.68f);
        game.assets.renderer.rect(0, 0, W, H);
        game.assets.renderer.setColor(PINK_BTN);
        drawRoundRect(restartButtonRect, 10f);

        if (newAchievements != null && !newAchievements.isEmpty()) {
            int   count  = newAchievements.size();
            float cardW  =  ACH_PANEL_WIDTH;
            float cardX  = (W - cardW) / 2f;
            float totalH = count * ACH_CARD_H + ACH_PANEL_EXTRA_H;
            float panelY = Y_ACH_START - totalH;

            game.assets.renderer.setColor(0.07f, 0.07f, 0.07f, ACH_PANEL_BG_ALPHA);
            game.assets.renderer.rect(cardX, panelY, cardW, totalH);

            game.assets.renderer.setColor(GOLD);
            game.assets.renderer.rect(cardX, panelY + totalH - ACH_TOP_BORDER_H, cardW, ACH_TOP_BORDER_H);

            game.assets.renderer.setColor(ACH_DIVIDER_COLOR, ACH_DIVIDER_COLOR, ACH_DIVIDER_H, 1f);
            for (int i = 1; i < count; i++) {
                float lineY = panelY + totalH - ACH_TOP_BORDER_H - i * ACH_CARD_H;
                game.assets.renderer.rect(cardX + ACH_PANEL_PADDING_SIDE, lineY, cardW - ACH_PANEL_PADDING_SIDE*2, ACH_DIVIDER_H);
            }
        }

        game.assets.renderer.end();
        game.assets.batch.begin();
        game.assets.font.getData().setScale(2.8f);
        game.assets.font.setColor(Color.RED);
        drawCentered("GAME OVER", W / 2f, Y_GAME_OVER);

        game.assets.font.getData().setScale(1.5f);
        game.assets.font.setColor(Color.WHITE);
        drawCentered("Score: " + score,     W / 2f, Y_SCORE);

        game.assets.font.getData().setScale(1.3f);
        game.assets.font.setColor(new Color(0.85f, 0.85f, 0.85f, 1f));
        drawCentered("Best:  " + bestScore, W / 2f, Y_BEST);

        if (newAchievements != null && !newAchievements.isEmpty()) {
            int   count  = newAchievements.size();
            float cardW  = ACH_PANEL_WIDTH;
            float cardX  = (W - cardW) / 2f;
            float totalH = count * ACH_CARD_H + ACH_PANEL_EXTRA_H;
            float panelY = Y_ACH_START - totalH;

            game.assets.font.getData().setScale(0.95f);
            game.assets.font.setColor(GOLD);
            drawCentered("NEW ACHIEVEMENTS:", W / 2f, Y_ACH_HEADER);

            float itemTop = panelY + totalH - 6f;
            for (AchievementType a : newAchievements) {
                game.assets.font.getData().setScale(0.95f);
                game.assets.font.setColor(GOLD);
                game.assets.font.draw(game.assets.batch,
                    "[+] " + a.title,
                    cardX + ACH_TITLE_OFFSET_X,
                    itemTop - ACH_TITLE_OFFSET_Y);

                game.assets.font.getData().setScale(0.78f);
                game.assets.font.setColor(0.72f, 0.72f, 0.72f, 1f);
                game.assets.font.draw(game.assets.batch,
                    a.description,
                    cardX + ACH_DESC_OFFSET_X,
                    itemTop - ACH_DESC_OFFSET_Y);

                itemTop -= ACH_CARD_H;
            }
        }

        game.assets.font.getData().setScale(1.2f);
        game.assets.font.setColor(Color.WHITE);
        drawCentered("Back to Menu", W / 2f, Y_BUTTON + 28f);

        game.assets.font.getData().setScale(0.8f);
        game.assets.font.setColor(Color.LIGHT_GRAY);
        drawCentered("Press R to return", W / 2f, Y_HINT);

        game.assets.font.getData().setScale(1f);
        game.assets.font.setColor(Color.WHITE);
        game.assets.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (restartButtonRect.contains(touch.x, touch.y))
                game.setScreen(new MenuScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            game.setScreen(new MenuScreen(game));
    }

    private void drawCentered(String text, float centerX, float y) {
        game.assets.glyph.setText(game.assets.font, text);
        game.assets.font.draw(game.assets.batch, text,
            centerX - game.assets.glyph.width / 2f, y);
    }

    private void drawRoundRect(Rectangle r, float rad) {
        game.assets.renderer.rect(r.x + rad, r.y, r.width - rad * 2, r.height);
        game.assets.renderer.rect(r.x, r.y + rad, r.width, r.height - rad * 2);
        game.assets.renderer.circle(r.x + rad,           r.y + rad,            rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + rad,            rad);
        game.assets.renderer.circle(r.x + rad,           r.y + r.height - rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + r.height - rad, rad);
    }

    @Override public void show()   {}
    @Override public void resize(int w, int h) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose(){}
}
