package io.undertree.obstacleavoid.entity

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector

/**
 *
 */
abstract class GameObjectBase {
    var x: Float = 0f
        set (value) {
            field = value
            updateBounds()
        }
    var y: Float = 0f
        set (value) {
            field = value
            updateBounds()
        }

    var width: Float = 1f
        set (value) {
            field = value
            updateBounds()
        }

    var height: Float = 1f
        set (value) {
            field = value
            updateBounds()
        }

    abstract val bounds: Circle

    open fun isCollidingWith(gameObject: GameObjectBase) = Intersector.overlaps(this.bounds, gameObject.bounds)

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    fun setLocation(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    private fun updateBounds() = bounds.setPosition(x + width / 2, y + height / 2)
}