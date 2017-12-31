package io.undertree.obstacleavoid.utils

import com.badlogic.gdx.utils.Logger

// helps with initializing loggers
inline fun <reified T: Any> logger(): Logger = Logger(T::class.java.simpleName, Logger.DEBUG)