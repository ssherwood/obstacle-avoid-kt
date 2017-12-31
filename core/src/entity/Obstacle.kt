package io.undertree.obstacleavoid.entity

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.utils.Pool
import io.undertree.obstacleavoid.config.GameConfig.MEDIUM_OBSTACLE_SPEED

/**
 *
 */
class Obstacle: GameObjectBase(), Pool.Poolable {
    companion object {
        const val BOUNDS_RADIUS = 0.3f
        const val SIZE = BOUNDS_RADIUS * 2f
    }

    var hasHitPlayer = false
    var ySpeed = MEDIUM_OBSTACLE_SPEED

    override val bounds = Circle(x, y, BOUNDS_RADIUS)

    fun update() {
        y -= ySpeed // falling down at ySpeed
    }

    override fun isCollidingWith(gameObject: GameObjectBase): Boolean {
        if (gameObject is Player && super.isCollidingWith(gameObject)) {
            hasHitPlayer = true
        }

        return hasHitPlayer
    }

    override fun reset() {
        setSize(1f, 1f)
        setLocation(0f, 0f) // reset x and y
        hasHitPlayer = false
        ySpeed = MEDIUM_OBSTACLE_SPEED
    }
}