package com.bubba.uiwidgets

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.bubba.SkinAsset
import com.bubba.ecs.components.PlayerComponent
import com.bubba.get

class ScoreWidget(assetManager: AssetManager, private val playerComponent: PlayerComponent): Actor() {

    private val label = Label("", assetManager.get(SkinAsset.UISkin))

    override fun act(delta: Float) {
        label.act(delta)
        label.setText("Score: ${playerComponent.getScore()}")
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        label.draw(batch, parentAlpha)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        label.setPosition(x, y)
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        label.setSize(width, height)
    }
}