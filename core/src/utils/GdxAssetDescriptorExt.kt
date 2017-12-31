package io.undertree.obstacleavoid.utils

import com.badlogic.gdx.assets.AssetDescriptor

inline fun <reified T: Any> assetDescriptor(filename: String) = AssetDescriptor<T>(filename, T::class.java)