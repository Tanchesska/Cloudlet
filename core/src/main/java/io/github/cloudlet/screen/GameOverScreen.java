package io.github.cloudlet.screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.cloudlet.Main;
import io.github.cloudlet.constants.GameConstants;

public class GameOverScreen implements Screen {
    private final Main game;
    private final OrthographicCamera camera;
    private final int score;
    private final int bestScore;
    private final Rectangle restartButtonRect;
    private static final Color PINK_BTN = new Color(0.87f, 0.27f, 0.54f, 1f);

    public GameOverScreen(Main game, int score, int bestScore) {
        this.game = game;
        this.score = score;
        this.bestScore = bestScore;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        this.restartButtonRect = new Rectangle(300, 165, 200, 55);
    }
    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        float W = GameConstants.SCREEN_WIDTH;
        float H = GameConstants.SCREEN_HEIGHT;

        game.assets.batch.setProjectionMatrix(camera.combined);
        game.assets.batch.begin();
        game.assets.batch.setColor(Color.WHITE);
        game.assets.batch.draw(game.assets.backgroundTexture, 0, 0, W, H);
        game.assets.batch.end();

        game.assets.renderer.setProjectionMatrix(camera.combined);
        game.assets.renderer.begin(ShapeRenderer.ShapeType.Filled);
        game.assets.renderer.setColor(0f, 0f, 0f, 0.6f);
        game.assets.renderer.rect(0, 0, W, H);

        game.assets.renderer.setColor(PINK_BTN);
        drawRoundRect(restartButtonRect, 10);
        game.assets.renderer.end();

        game.assets.batch.begin();
        game.assets.font.getData().setScale(3.2f);
        game.assets.font.setColor(Color.RED);
        drawCentered("GAME OVER", W / 2f, 370);

        game.assets.font.getData().setScale(1.6f);
        game.assets.font.setColor(Color.WHITE);
        drawCentered("Score: " + score, W / 2f, 300);
        drawCentered("Best:  " + bestScore, W / 2f, 270);

        game.assets.font.getData().setScale(1.3f);
        drawCentered("Back to Menu", W / 2f, 200);

        game.assets.font.getData().setScale(0.9f);
        game.assets.font.setColor(Color.LIGHT_GRAY);
        drawCentered("Press R to return", W / 2f, 138);

        game.assets.font.getData().setScale(1f);
        game.assets.font.setColor(Color.WHITE);
        game.assets.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (restartButtonRect.contains(touch.x, touch.y)) {
                game.setScreen(new MenuScreen(game));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    private void drawCentered(String text, float centerX, float y) {
        game.assets.glyph.setText(game.assets.font, text);
        game.assets.font.draw(game.assets.batch, text, centerX - game.assets.glyph.width / 2f, y);
    }

    private void drawRoundRect(Rectangle r, float rad) {
        game.assets.renderer.rect(r.x + rad, r.y, r.width - rad * 2, r.height);
        game.assets.renderer.rect(r.x, r.y + rad, r.width, r.height - rad * 2);
        game.assets.renderer.circle(r.x + rad, r.y + rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + rad, rad);
        game.assets.renderer.circle(r.x + rad, r.y + r.height - rad, rad);
        game.assets.renderer.circle(r.x + r.width - rad, r.y + r.height - rad, rad);
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
