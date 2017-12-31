package io.undertree.obstacleavoid

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Logger
import io.undertree.obstacleavoid.screen.loading.LoadingScreen

class ObstacleAvoidGame: Game() {

    val assetManager = AssetManager().apply {
        logger.level = Logger.DEBUG
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        setScreen(LoadingScreen(this))
    }

    override fun dispose() {
        super.dispose()
        assetManager.dispose()
    }
}