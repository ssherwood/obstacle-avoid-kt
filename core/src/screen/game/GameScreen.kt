package io.undertree.obstacleavoid.screen.game

import com.badlogic.gdx.Screen
import io.undertree.obstacleavoid.ObstacleAvoidGame
import io.undertree.obstacleavoid.utils.logger

/**
 *
 */
class GameScreen(private val game: ObstacleAvoidGame): Screen {

    companion object {
        @JvmStatic
        private val log = logger<GameScreen>()
    }

    private val assetManager = game.assetManager
    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        controller = GameController()
        renderer = GameRenderer(assetManager, controller)
    }

    override fun render(deltaTime: Float) {
        controller.updateWorld(deltaTime)
        renderer.render()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }
}