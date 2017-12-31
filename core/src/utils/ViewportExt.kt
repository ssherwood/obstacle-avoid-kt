package io.undertree.obstacleavoid.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport

@JvmOverloads
fun Viewport.drawGrid(renderer: ShapeRenderer, cellSize: Int = 1, asColor: Color = Color.WHITE) {
    val doubleWorldWidth = worldWidth * 2
    val doubleWorldHeight = worldHeight * 2

    apply()

    renderer.apply {
        val oldColor = color.cpy()

        use {
            color = asColor

            // draw vertical lines
            var x = -doubleWorldWidth
            while (x < doubleWorldWidth) {
                line(x, -doubleWorldHeight, x, doubleWorldHeight)
                x += cellSize
            }

            // draw horizontal lines
            var y = -doubleWorldHeight
            while (y < doubleWorldHeight) {
                line(-doubleWorldWidth, y, doubleWorldWidth, y)
                y += cellSize
            }

            // draw 0,0 lines
            color = Color.RED
            line(0f, -doubleWorldHeight, 0f, doubleWorldHeight)
            line(-doubleWorldWidth, 0f, doubleWorldWidth, 0f)

            // draw world bounds
            color = Color.GREEN
            line(0f, worldHeight, worldWidth, worldHeight)
            line(worldWidth, 0f, worldWidth, worldHeight)
        }

        color = oldColor
    }
}