package com.bubba.uiwidgets

import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.bubba.DropGame
import com.bubba.SkinAsset
import com.bubba.get

class PauseWidget(assetManager: AssetManager, stage: Stage): Actor() {

    private val window = Window("Pause", assetManager.get(SkinAsset.UISkin))
    private val closeButton = TextButton("X", assetManager.get(SkinAsset.UISkin))
    private val restartButton = TextButton("Restart", assetManager.get(SkinAsset.UISkin))
    private val quitButton = TextButton("Exit", assetManager.get(SkinAsset.UISkin))
    private var isShown = false

    init {
        this.stage = stage

        window.titleTable.add(closeButton).height(window.padTop)
        window.add(restartButton)
        window.add(quitButton)
    }

    fun hideWindow() {
        window.remove()
    }

    fun showWindow() {
        stage.addActor(window)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        window.setPosition(
            (DropGame.VIRTUAL_WIDTH - this.width)/ 2 - (window.width / 2),
            (DropGame.VIRTUAL_HEIGHT - this.height) / 2 - (window.height / 2))
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        window.setSize(width, height)
    }
}