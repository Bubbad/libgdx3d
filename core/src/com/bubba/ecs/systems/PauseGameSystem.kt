package com.bubba.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.bubba.GameScreenUI

class PauseGameSystem(private val gameScreenUI: GameScreenUI): EntitySystem() {

    private var isShown = false

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isShown = !isShown
            gameScreenUI.toggleShowPauseMenu()
        }
    }
}