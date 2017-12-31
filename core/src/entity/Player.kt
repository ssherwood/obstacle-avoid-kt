package io.undertree.obstacleavoid.entity

import com.badlogic.gdx.math.Circle

/**
 *
 */
class Player: GameObjectBase() {
    companion object {
        const val BOUNDS_RADIUS = 0.4f
        const val SIZE = BOUNDS_RADIUS * 2f
        const val MAX_X_SPEED = 0.25f
    }

    override val bounds = Circle(x, y, BOUNDS_RADIUS)
}