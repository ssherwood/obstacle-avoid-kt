package io.undertree.obstacleavoid.config

object GameConfig {
    // pixels - desktop only
    const val WIDTH_PX = 480
    const val HEIGHT_PX = 800

    // world units
    const val HUD_WIDTH = WIDTH_PX.toFloat()
    const val HUD_HEIGHT = HEIGHT_PX.toFloat()
    const val WORLD_WIDTH = 6.0f
    const val WORLD_HEIGHT = 10.0f
    const val WORLD_CENTER_X = WORLD_WIDTH / 2f
    const val WORLD_CENTER_Y = WORLD_HEIGHT / 2f

    const val OBSTACLE_SPAWN_TIME = 0.35f

    const val LIVES_START = 3
    const val SCORE_MAX_TIME = 1.25f

    const val EASY_OBSTACLE_SPEED = 0.1f
    const val MEDIUM_OBSTACLE_SPEED = 0.15f
    const val HARD_OBSTACLE_SPEED = 0.18f
}