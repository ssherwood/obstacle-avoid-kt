package io.undertree.obstacleavoid.screen.loading

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.undertree.obstacleavoid.ObstacleAvoidGame
import io.undertree.obstacleavoid.assets.AssetDescriptors
import io.undertree.obstacleavoid.config.GameConfig
import io.undertree.obstacleavoid.screen.game.GameScreen
import io.undertree.obstacleavoid.utils.GdxShapeType
import io.undertree.obstacleavoid.utils.clearScreen
import io.undertree.obstacleavoid.utils.logger
import io.undertree.obstacleavoid.utils.use

/**
 *
 */
class LoadingScreen(private val game: ObstacleAvoidGame) : ScreenAdapter() {
    companion object {
        @JvmStatic
        private val log = logger<LoadingScreen>()

        private const val PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f
        private const val PROGRESS_BAR_HEIGHT = 60f
    }

    private val assetManager = game.assetManager
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: ShapeRenderer

    private var progressPercent = 0f
    private var finishedLoadingDelay = 0.8f

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
        renderer = ShapeRenderer()

        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.GAMEPLAY)
    }

    override fun render(deltaTime: Float) {
        update(deltaTime)

        clearScreen()
        viewport.apply()

        with(renderer) {
            projectionMatrix = camera.combined

            use(GdxShapeType.Filled) {
                draw()
            }
        }

        if (finishedLoadingDelay <= 0) {
            log.debug("assetManager diagnostics=${assetManager.diagnostics}")
            game.screen = GameScreen(game)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        log.debug("dispose")
        renderer.dispose()
    }

    private fun update(deltaTime: Float) {
        // progress is between 0f and 1f
        progressPercent = assetManager.progress

        // update returns true when all assets are loaded
        if (assetManager.update()) {
            finishedLoadingDelay -= deltaTime
        }
    }

    private fun draw() {
        val progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f
        val progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f

        renderer.rect(progressBarX, progressBarY,
                progressPercent * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT)
    }
}