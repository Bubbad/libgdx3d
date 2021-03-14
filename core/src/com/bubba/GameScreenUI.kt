package com.bubba

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.bubba.ecs.components.PlayerComponent
import com.bubba.uiwidgets.CrosshairWidget
import com.bubba.uiwidgets.HealthWidget
import com.bubba.uiwidgets.PauseWidget
import com.bubba.uiwidgets.ScoreWidget

class GameScreenUI(assetManager: AssetManager, playerComponent: PlayerComponent) {
    private val stage = Stage() // The book uses a FitViewport instead of default ScalingViewport. Switch?
    private val crosshairWidget = CrosshairWidget(assetManager)
    private val scoreWidget = ScoreWidget(assetManager, playerComponent)
    private val healthWidget = HealthWidget(assetManager, playerComponent)
    private val pauseWidget = PauseWidget(assetManager, stage)

    init {
        crosshairWidget.setSize(CrosshairWidget.WIDTH.toFloat(), CrosshairWidget.HEIGHT.toFloat())
        crosshairWidget.setPosition(
            (DropGame.VIRTUAL_WIDTH / 2 - CrosshairWidget.WIDTH / 2).toFloat(),
            (DropGame.VIRTUAL_HEIGHT / 2 - CrosshairWidget.HEIGHT / 2).toFloat())
        stage.addActor(crosshairWidget)

        scoreWidget.setPosition(15f, DropGame.VIRTUAL_HEIGHT - 35f)
        scoreWidget.setSize(50f, 20f)
        stage.addActor(scoreWidget)

        healthWidget.setPosition((DropGame.VIRTUAL_WIDTH / 2).toFloat(), 35f)
        healthWidget.setSize(200f, 50f)
        stage.addActor(healthWidget)

        pauseWidget.setPosition(
            (DropGame.VIRTUAL_WIDTH / 2).toFloat(),
            (DropGame.VIRTUAL_HEIGHT / 2).toFloat())
        pauseWidget.setSize(200f, 200f)
    }

    fun update(deltaTime: Float) {
        stage.act(deltaTime)
    }

    fun showPauseMenu(shouldShow: Boolean) {
        if (shouldShow) {
            pauseWidget.showWindow()
        } else {
            pauseWidget.hideWindow()
        }
    }

    fun render() {
        stage.draw()
    }

    fun dispose() {
        stage.dispose()
    }
}