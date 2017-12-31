package io.undertree.obstacleavoid.utils.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import io.undertree.obstacleavoid.utils.logger

class DebugCameraController(x: Float, y: Float){
    companion object {
        @JvmStatic
        private val log = logger<DebugCameraController>()
    }

    private val position = Vector2(x, y)
    private val startPosition = Vector2(x, y)
    private val config = DebugCameraConfig()

    private var zoomLevel = 1f
        set(value) {
            field = MathUtils.clamp(value, config.getMaxZoomIn(), config.getMaxZoomOut())
        }

    init {
        log.debug("$config")
    }

    /*
    fun setStartPosition(x: Float, y: Float) {
        startPosition.set(x, y)
        position.set(startPosition)
    }
    */

    fun applyTo(camera: OrthographicCamera) {
        camera.position.set(position, 0f)
        camera.zoom = zoomLevel
        camera.update()
    }

    fun handleDebugInput() {
        val moveSpeed = config.getMoveSpeed() * Gdx.graphics.deltaTime
        val zoomSpeed = config.getZoomSpeed() * Gdx.graphics.deltaTime

        when {
            config.isLeftPressed() -> moveLeft(moveSpeed)
            config.isRightPressed() -> moveRight(moveSpeed)
            config.isUpPressed() -> moveUp(moveSpeed)
            config.isDownPressed() -> moveDown(moveSpeed)
            config.isZoomInPressed() -> zoomIn(zoomSpeed)
            config.isZoomOutPressed() -> zoomOut(zoomSpeed)
            config.isZoomResetPressed() -> zoomReset()
            config.isLogPressed() -> log.debug("position=${position}, zoomLevel=${zoomLevel}")
        }
    }

    private fun moveCamera(xSpeed: Float, ySpeed: Float) = setPosition(position.x + xSpeed, position.y + ySpeed)
    private fun setPosition(x:Float, y: Float) = position.set(x, y)
    private fun moveLeft(speed: Float) = moveCamera(-speed, 0f)
    private fun moveRight(speed: Float) = moveCamera(speed, 0f)
    private fun moveUp(speed: Float) = moveCamera(0f, speed)
    private fun moveDown(speed: Float) = moveCamera(0f, -speed)

    private fun zoomIn(speed: Float) {
        zoomLevel += speed
    }

    private fun zoomOut(speed: Float) {
        zoomLevel -= speed
    }

    private fun zoomReset() {
        zoomLevel = 1f
    }
}