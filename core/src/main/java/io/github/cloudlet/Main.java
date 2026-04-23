package io.github.cloudlet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.cloudlet.domain.Cloud;
import io.github.cloudlet.domain.Drop;
import io.github.cloudlet.service.InputService;
import io.github.cloudlet.service.CloudService;
import io.github.cloudlet.service.DropService;

public class Main extends ApplicationAdapter {

    private OrthographicCamera camera;
    private ShapeRenderer renderer;

    private Cloud cloud;
    private float backgroundOffset = 0;
    private InputService inputService;
    private CloudService cloudService;
    private DropService dropService;

    private float worldOffset = 0;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        renderer = new ShapeRenderer();

        cloud = new Cloud(400, 300);

        inputService = new InputService();
        cloudService = new CloudService();
        dropService = new DropService();
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        inputService.update(cloud, camera, delta);
        cloudService.update(cloud, delta);
        dropService.update(delta, cloud, cloudService);

        worldOffset += 50 * delta;
        backgroundOffset += 200 * delta;

        cloud.position.x = Math.max(0, Math.min(800, cloud.position.x));
        cloud.position.y = Math.max(120, Math.min(480, cloud.position.y));

        Gdx.gl.glClearColor(0.5f, 0.7f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Color.FOREST);
        renderer.rect(-backgroundOffset % 800, 0, 800, 100);
        renderer.rect(800 - (backgroundOffset % 800), 0, 800, 100);


        renderer.rect(0, (worldOffset % 100) - 100, 800, 100);

        renderer.setColor(Color.BLUE);
        for (Drop drop : dropService.getDrops()) {
            renderer.circle(drop.position.x, drop.position.y, drop.radius);
        }

        renderer.setColor(Color.WHITE);
        renderer.circle(cloud.position.x, cloud.position.y, cloud.radius);

        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
