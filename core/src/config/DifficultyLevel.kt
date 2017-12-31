package io.undertree.obstacleavoid.config

enum class DifficultyLevel(val obstacleSpeed: Float) {
    EAZY(GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(GameConfig.MEDIUM_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBSTACLE_SPEED)
}