package io.github.cloudlet;

import com.badlogic.gdx.Game;
import io.github.cloudlet.screen.MenuScreen;

public class Main extends Game {
    public GameAssets assets;

    @Override
    public void create() {
        assets = new GameAssets();
        assets.load();
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) {
            assets.dispose();
        }
    }
}
