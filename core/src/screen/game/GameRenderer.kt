package io.undertree.obstacleavoid.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import io.undertree.obstacleavoid.assets.AssetDescriptors.FONT
import io.undertree.obstacleavoid.assets.AssetDescriptors.GAMEPLAY
import io.undertree.obstacleavoid.assets.RegionNames.BACKGROUND_REGION
import io.undertree.obstacleavoid.assets.RegionNames.OBSTACLE_REGION
import io.undertree.obstacleavoid.assets.RegionNames.PLAYER_REGION
import io.undertree.obstacleavoid.config.GameConfig
import io.undertree.obstacleavoid.config.GameConfig.WORLD_WIDTH
import io.undertree.obstacleavoid.entity.Obstacle
import io.undertree.obstacleavoid.entity.Player
import io.undertree.obstacleavoid.utils.*
import io.undertree.obstacleavoid.utils.debug.DebugCameraController

/**
 *
 */
class GameRenderer(private val assetManager: AssetManager,
                   private val controller: GameController): Disposable {

    companion object {
        @JvmStatic
        private val log = logger<GameRenderer>()
    }

    private val debugCameraController = DebugCameraController(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val renderer = ShapeRenderer()

    private val hudCamera = OrthographicCamera()
    private val hudViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera)
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()
    private val padding = 20f

    private val uiFont = assetManager[FONT]
    private val gameplayAtlas = assetManager[GAMEPLAY]
    private val backgroundTexture = gameplayAtlas[BACKGROUND_REGION]
    private val obstacleTexture = gameplayAtlas[OBSTACLE_REGION]
    private val playerTexture = gameplayAtlas[PLAYER_REGION]

    fun render() {
        // batch.totalRenderCalls = 0

        with (debugCameraController) {
            handleDebugInput()
            applyTo(camera)
        }

        clearScreen()

        // check touch
        if (Gdx.input.isTouched && !controller.gameOver) {
            val screenTouch = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            log.debug("screenTouch=$screenTouch")
            val worldTouch = viewport.unproject(Vector2(screenTouch))
            log.debug("worldTouch=$worldTouch")

            val player = controller.player

            worldTouch.x = MathUtils.clamp(worldTouch.x, 0f, WORLD_WIDTH - Player.SIZE)
            player.x = worldTouch.x
        }

        renderGameplay()
        renderDebug()
        renderHUD()

        viewport.drawGrid(renderer)

        // log.debug("total render calls=${batch.totalRenderCalls}")
    }

    private fun renderGameplay() {
        viewport.apply()

        with (batch) {
            projectionMatrix = camera.combined

            use {
                draw(backgroundTexture, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

                val player = controller.player
                draw(playerTexture, player.x, player.y, Player.SIZE, Player.SIZE)

                controller.obstacles.forEach {
                    draw(obstacleTexture, it.x, it.y, Obstacle.SIZE, Obstacle.SIZE)
                }
            }
        }
    }

    private fun renderDebug() {
        viewport.apply()


        with (renderer) {
            val originalColor = color.cpy()
            color = Color.RED

            projectionMatrix = camera.combined
            use {
                with (controller) {
                    x(player.bounds.x, player.bounds.y, 0.1f)
                    circle(player.bounds)

                    obstacles.forEach {
                        x(it.bounds.x, it.bounds.y, 0.1f)
                        circle(it.bounds)
                    }
                }
            }

            color = originalColor
        }
    }

    private fun renderHUD() {
        hudViewport.apply()

        batch.apply {
            projectionMatrix = hudCamera.combined

            use {
                layout.setText(uiFont, "LIVES: ${controller.lives}")
                uiFont.draw(this, layout,
                        padding,
                        GameConfig.HUD_HEIGHT - layout.height)

                layout.setText(uiFont, "SCORE: ${controller.displayScore}")
                uiFont.draw(this, layout,
                        GameConfig.HUD_WIDTH - layout.width - padding,
                        GameConfig.HUD_HEIGHT - layout.height)
            }
        }
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        hudViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
    }
}