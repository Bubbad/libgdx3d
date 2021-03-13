package com.bubba.uiwidgets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.bubba.SkinAsset
import com.bubba.ecs.components.PlayerComponent
import com.bubba.get

class HealthWidget(assetManager: AssetManager, private val playerComponent: PlayerComponent): Actor() {

    private val healthBar: ProgressBar
    private val healthBarStyle : ProgressBar.ProgressBarStyle

    init {
        healthBarStyle = ProgressBar.ProgressBarStyle(
            assetManager.get(SkinAsset.UISkin).newDrawable("white", Color.GRAY),
            assetManager.get(SkinAsset.UISkin).newDrawable("white", Color.RED)
        )


        healthBarStyle.knobBefore = healthBarStyle.knob;
        healthBar = ProgressBar(0f, 100f, 1f, false, healthBarStyle)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        healthBar.draw(batch, parentAlpha)
    }

    override fun act(delta: Float) {
        healthBar.act(delta)
        healthBar.value = playerComponent.getHealth().toFloat()
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        healthBar.setPosition(x, y)
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        healthBar.setSize(width, height)
        healthBarStyle.background.minWidth = width
        healthBarStyle.background.minHeight = height
        healthBarStyle.knob.minWidth = healthBar.value
        healthBarStyle.knob.minHeight = height
    }
}