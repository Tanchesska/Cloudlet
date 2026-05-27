package io.github.cloudlet.constants;

public final class GameConstants {

    private GameConstants() {}

    //размеры окна, границы мира и кнопки
    public static final class Screen {
        public static final float WIDTH = 800f;
        public static final float HEIGHT = 480f;
        public static final float FIELD_MIN_Y = 120f;
        public static final float FIELD_MAX_Y = 480f;
        public static final float DIFFICULTY_SCALE_TIME = 90f;

        public static final float POUR_BTN_X = 680f;
        public static final float POUR_BTN_Y = 20f;
        public static final float POUR_BTN_W = 110f;
        public static final float POUR_BTN_H = 46f;
        public static final float INPUT_BLOCK_ON_POUR = 0.5f;
        public static final int MAX_SCORE = 999_999_999;
    }

    // Начальные координаты, лимиты воды и радиуса
    public static final class Cloud {
        public static final float INITIAL_X = 400f;
        public static final float INITIAL_Y = 300f;
        public static final float INITIAL_RADIUS = 40f;
        public static final float INITIAL_WATER = 20f;
        public static final float MIN_RADIUS = 20f;
        public static final float MAX_RADIUS = 90f;
        public static final float SPEED = 300f;

        public static final float RADIUS_BASE = 20f;
        public static final float RADIUS_FACTOR = 0.7f;
        public static final float DEAD_RADIUS_THRESHOLD = 0.5f;

        public static final float WATER_MAX = 100f;
        public static final float WATER_DRAIN_PER_SEC = 4f;
        public static final float AUTO_POUR_THRESHOLD = 80f;
        public static final float AUTO_POUR_RESET_WATER = 20f;
        public static final float RAIN_THRESHOLD = 30f;
    }

    // Скорости, радиусы урона
    public static final class Enemy {
        public static final float SPEED = 120f;
        public static final float SPAWN_MIN = 6f;
        public static final float SPAWN_RANGE = 6f;
        public static final float HIT_RADIUS = 24f;
        public static final float OFFSCREEN_X = 120f;
        public static final float DYING_FADE_SPEED = 1.5f;

        // Эффекты конкретных врагов
        public static final float SUN_WATER_DRAIN_PER_SEC = 12f;
        public static final float SUN_EFFECT_DURATION = 3f;
        public static final float BIRD_WATER_STEAL = 25f;
        public static final float STORM_BLOCK_DURATION = 1.5f;
        public static final float WIND_PUSH_SPEED = 300f;
        public static final float WIND_DURATION = 2f;
        public static final float WIND_ROTATION_SPEED = 360f;
    }

    // Таймеры, вероятности и логика магнита/щита
    public static final class Bonus {
        public static final float SPEED = -120f;
        public static final float SPAWN_MIN = 15f;
        public static final float SPAWN_RANGE = 10f;
        public static final float HIT_RADIUS = 18f;
        public static final float OFFSCREEN_X = 100f;
        public static final float FADE_SPEED = 2f;

        // Эффекты конкретных бонусов
        public static final float MAGNET_DURATION = 3f;
        public static final float MAGNET_PULL_DIST = 250f;
        public static final float MAGNET_PULL_SPEED = 350f;
        public static final float SHIELD_DURATION = 5f;
        public static final float RAINBOW_WATER_MULT = 1.5f;
        public static final float RAINBOW_DURATION = 3f;
    }

    // Обычные капли, фиолетовые капли и мини-тучки
    public static final class Drop {
        public static final float SPEED = 200f;
        public static final float SPAWN_INTERVAL = 0.5f;

        public static final float SPECIAL_CHANCE = 0.08f;
        public static final float CLOUD_CHANCE = 0.03f;

        public static final float WATER_NORMAL = 5f;
        public static final float WATER_SPECIAL = 10f;
        public static final float WATER_CLOUD = 12f;
    }

    // Дождь
    public static final class Rain {
        public static final float FLOOR_Y = 130f;
        public static final int DROP_COUNT = 20;
        public static final float VEL_X_RANGE = 30f;
        public static final float VEL_Y_BASE = 350f;
        public static final float VEL_Y_RANGE = 150f;
    }

    // Цветы
    public static final class Flower {
        public static final float SPEED = 60f;
        public static final float MIN_DIST = 350f;
        public static final float EXTRA_DIST = 250f;
        public static final float POS_Y = 100f;
        public static final int COUNT = 5;

        public static final int MAX_WATERINGS = 4;
        public static final float HIT_COOLDOWN = 0.25f;

        public static final int SCORE_1 = 20;
        public static final int SCORE_2 = 50;
        public static final int SCORE_3 = 70;
        public static final int SCORE_4 = 100;
    }
}
