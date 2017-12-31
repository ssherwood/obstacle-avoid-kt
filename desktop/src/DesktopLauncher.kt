package io.undertree.obstacleavoid.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.undertree.obstacleavoid.ObstacleAvoidGame
import io.undertree.obstacleavoid.config.GameConfig

fun main(args: Array<String>) {
    LwjglApplication(ObstacleAvoidGame(), LwjglApplicationConfiguration().apply {
        with (GameConfig) {
            width = WIDTH_PX
            height = HEIGHT_PX
        }
    })
}