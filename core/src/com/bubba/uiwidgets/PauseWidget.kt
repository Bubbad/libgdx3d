package com.bubba.uiwidgets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.bubba.DropGame
import com.bubba.GameScreen
import com.bubba.LoadingScreen
import com.bubba.SkinAsset
import com.bubba.get

class PauseWidget(assetManager: AssetManager, stage: Stage, game: DropGame): Actor() {

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

        closeButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                toggleShowWindow()
            }
        })

        restartButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                game.removeScreen(GameScreen::class.java)
                game.setScreen<LoadingScreen>()
            }
        })

        quitButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })
    }

    fun toggleShowWindow() {
        if (isShown) {
            hideWindow()
        } else {
            showWindow()
        }
        isShown = !isShown
    }

    private fun hideWindow() {
        Gdx.input.isCursorCatched = true
        window.remove()
    }

    private fun showWindow() {
        Gdx.input.isCursorCatched = false
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