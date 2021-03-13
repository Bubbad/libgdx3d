package com.bubba.uiwidgets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.bubba.TextureAssets
import com.bubba.get

class CrosshairWidget(assetManager: AssetManager): Actor() {
    companion object {
        const val WIDTH = 32
        const val HEIGHT = 32
    }

    private val crosshairDot: Image = Image(assetManager.get(TextureAssets.CrosshairDot))

    override fun draw(batch: Batch?, parentAlpha: Float) {
        crosshairDot.draw(batch, parentAlpha)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        crosshairDot.setPosition(x - WIDTH/2, y - HEIGHT/2)
    }
}