package com.bubba

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.bubba.uiwidgets.CrosshairWidget

class GameScreenUI(assetManager: AssetManager) {
    private val stage = Stage() // The book uses a FitViewport instead of default ScalingViewport. Switch?
    private val crosshairWidget = CrosshairWidget(assetManager)

    init {
        crosshairWidget.setSize(CrosshairWidget.WIDTH.toFloat(), CrosshairWidget.HEIGHT.toFloat())
        crosshairWidget.setPosition(
            (DropGame.VIRTUAL_WIDTH / 2 - CrosshairWidget.WIDTH / 2).toFloat(),
            (DropGame.VIRTUAL_HEIGHT / 2 - CrosshairWidget.HEIGHT / 2).toFloat())
        stage.addActor(crosshairWidget)
    }

    fun update(deltaTime: Float) {
        stage.act(deltaTime)
    }

    fun render() {
        stage.draw()
    }

    fun dispose() {
        stage.dispose()
    }
}