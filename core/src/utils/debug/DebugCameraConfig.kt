package io.undertree.obstacleavoid.utils.debug

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.JsonReader
import io.undertree.obstacleavoid.utils.isKeyPressed
import io.undertree.obstacleavoid.utils.logger
import io.undertree.obstacleavoid.utils.toInternalFile

/**
 *
 */
class DebugCameraConfig {
    companion object {
        @JvmStatic
        private val log = logger<DebugCameraConfig>()

        enum class CameraSettings(val key: String, val defaultValue: Float, var mappedValue: Float = defaultValue) {
            MAX_ZOOM_IN("maxZoomIn", 0.2f),
            MAX_ZOOM_OUT("maxZoomOut", 30f),
            ZOOM_SPEED("zoomSpeed", 2f),
            MOVE_SPEED("moveSpeed", 20f);

            override fun toString(): String {
                return "$key=$mappedValue"
            }
        }

        enum class CameraKeys(val key: String, val defaultKey: Int, var mappedKey: Int = defaultKey) {
            LEFT_KEY("leftKey", Input.Keys.A),
            RIGHT_KEY("rightKey", Input.Keys.D),
            UP_KEY("upKey", Input.Keys.W),
            DOWN_KEY("downKey", Input.Keys.S),
            ZOOM_IN_KEY("zoomInKey", Input.Keys.EQUALS),
            ZOOM_OUT_KEY("zoomOutKey", Input.Keys.MINUS),
            ZOOM_RESET_KEY("zoomResetKey", Input.Keys.BACKSPACE),
            LOG_KEY("logKey", Input.Keys.ENTER);

            fun isKeyPressed() = mappedKey.isKeyPressed()

            override fun toString(): String {
                return "$key=${Input.Keys.toString(mappedKey)}"
            }
        }

        private const val FILE_PATH = "debug/debug-camera.json"
        private val fileHandle = FILE_PATH.toInternalFile()
    }

    init {
        if (fileHandle.exists()) {
            loadConfig()
        } else {
            log.info("Using defaults file does not exist on path ${FILE_PATH}")
        }
    }

    fun isLeftPressed() = CameraKeys.LEFT_KEY.isKeyPressed()
    fun isRightPressed() = CameraKeys.RIGHT_KEY.isKeyPressed()
    fun isDownPressed() = CameraKeys.DOWN_KEY.isKeyPressed()
    fun isUpPressed() = CameraKeys.UP_KEY.isKeyPressed()
    fun isZoomInPressed() = CameraKeys.ZOOM_IN_KEY.isKeyPressed()
    fun isZoomOutPressed() = CameraKeys.ZOOM_OUT_KEY.isKeyPressed()
    fun isZoomResetPressed() = CameraKeys.ZOOM_RESET_KEY.isKeyPressed()
    fun isLogPressed() = CameraKeys.LOG_KEY.isKeyPressed()

    fun getZoomSpeed() = CameraSettings.ZOOM_SPEED.mappedValue
    fun getMoveSpeed() = CameraSettings.MOVE_SPEED.mappedValue
    fun getMaxZoomIn() = CameraSettings.MAX_ZOOM_IN.mappedValue
    fun getMaxZoomOut() = CameraSettings.MAX_ZOOM_OUT.mappedValue

    private fun loadConfig() {
        try {
            val jsonRoot = JsonReader().parse(fileHandle)
            log.debug("Camera config loaded from ${FILE_PATH}")

            for (cameraSetting in CameraSettings.values()) {
                val keyValue = jsonRoot.getFloat(cameraSetting.key, cameraSetting.defaultValue)
                cameraSetting.mappedValue = keyValue
            }

            for (cameraKey in CameraKeys.values()) {
                val keyString = jsonRoot.getString(cameraKey.key, cameraKey.defaultKey.toString())
                cameraKey.mappedKey = Input.Keys.valueOf(keyString)
            }
        } catch (e: Exception) {
            log.error("Error loading ${FILE_PATH} using defaults", e)
        }
    }

    override fun toString(): String {
        return """
            | ${DebugCameraConfig::class.java.simpleName} {
            |   cameraSettings {
            |      ${CameraSettings.values().joinToString("\n|      ")}
            |   },
            |   cameraKeys={
            |      ${CameraKeys.values().joinToString("\n|      ")}
            |   }
            | }
        """.trimMargin()
    }
}