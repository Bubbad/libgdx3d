package com.bubba

import com.badlogic.gdx.scenes.scene2d.Stage

class GameScreenUI() {
    private val stage = Stage() // The book uses a FitViewport instead of default ScalingViewport. Switch?

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