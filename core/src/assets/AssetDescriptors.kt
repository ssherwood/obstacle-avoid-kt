package io.undertree.obstacleavoid.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.undertree.obstacleavoid.assets.AssetPaths.GAMEPLAY_ATLAS
import io.undertree.obstacleavoid.assets.AssetPaths.VARANDA_FONT
import io.undertree.obstacleavoid.utils.assetDescriptor

object AssetDescriptors {
    val FONT = assetDescriptor<BitmapFont>(VARANDA_FONT)
    val GAMEPLAY = assetDescriptor<TextureAtlas>(GAMEPLAY_ATLAS)
}