package io.undertree.obstacleavoid.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import io.undertree.obstacleavoid.config.DifficultyLevel
import io.undertree.obstacleavoid.config.GameConfig
import io.undertree.obstacleavoid.entity.Obstacle
import io.undertree.obstacleavoid.entity.Player
import io.undertree.obstacleavoid.utils.GdxArray
import io.undertree.obstacleavoid.utils.isKeyPressed
import io.undertree.obstacleavoid.utils.logger

/**
 *
 */
class GameController {

    companion object {
        @JvmStatic
        private val log = logger<GameController>()
    }

    private val difficultyLevel = DifficultyLevel.MEDIUM
    private val obstaclePool = Pools.get(Obstacle::class.java, 20)

    private var obstacleTimer = 0f
    private var scoreTimer = 0f

    val gameOver
        get() = lives <= 0

    val obstacles = GdxArray<Obstacle>()

    var player: Player = Player().apply {
        setSize(Player.SIZE, Player.SIZE)
        setLocation(GameConfig.WORLD_WIDTH / 2f - Player.BOUNDS_RADIUS, 1f - Player.BOUNDS_RADIUS)
    }
        private set
    var lives = GameConfig.LIVES_START
        private set
    var score = 0
        private set
    var displayScore = 0
        private set

    fun updateWorld(deltaTime: Float) {
        if (gameOver) {
            return
        }

        // update game world
        when {
            Input.Keys.RIGHT.isKeyPressed() -> Player.MAX_X_SPEED
            Input.Keys.LEFT.isKeyPressed() -> -Player.MAX_X_SPEED
            else -> 0f
        }.also {
            player.x += it
        }

        blockPlayerFromLeavingWorld()

        createNewObstacle(deltaTime)
        updateObstacles()
        removePassedObstacles()

        updateScore(deltaTime)
        updateDisplayStore(deltaTime)

        if (isCollidingWithPlayer()) {
            log.debug("Collision detected")
            lives--

            if (gameOver) {
                println("Game Over!")
            } else {
                restart()
            }
        }
    }

    private fun restart() {
        obstaclePool.freeAll(obstacles)
        obstacles.clear()
        player.setLocation(GameConfig.WORLD_WIDTH / 2f - Player.BOUNDS_RADIUS, 1f - Player.BOUNDS_RADIUS)
    }

    private fun updateScore(deltaTime: Float) {
        scoreTimer += deltaTime

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            scoreTimer = 0f
            score += MathUtils.random(1, 5)
        }
    }

    private fun updateDisplayStore(deltaTime: Float) {
        if (displayScore < score) {
            // i'm not sure about this...
            displayScore = Math.min(score, displayScore + (60 * deltaTime).toInt())
        }
    }

    private fun updateObstacles() =
            obstacles.map { it.update() }

    // remove obstacles that have fallen below the screen (y axis)
    private fun removePassedObstacles() {
        obstacles.filter { it.y < -Obstacle.SIZE }
                .map {
                    obstaclePool.free(it)
                    obstacles.removeValue(it, true)
                }
    }

    private fun isCollidingWithPlayer() =
            obstacles.any { !it.hasHitPlayer && it.isCollidingWith(player) }

    private fun createNewObstacle(deltaTime: Float) {
        obstacleTimer += deltaTime

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            obstacleTimer = 0f

            obstacles.add(obstaclePool.obtain().apply {
                ySpeed = difficultyLevel.obstacleSpeed
                setSize(Obstacle.SIZE, Obstacle.SIZE)
                setLocation(MathUtils.random(0f, GameConfig.WORLD_WIDTH - Obstacle.SIZE), GameConfig.WORLD_HEIGHT)
            })
        }
    }

    private fun blockPlayerFromLeavingWorld() {
        player.x = MathUtils.clamp(player.x, 0f, GameConfig.WORLD_WIDTH - Player.SIZE)
    }
}