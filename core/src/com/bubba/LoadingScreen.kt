package com.bubba

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(private val dropGame: DropGame) : KtxScreen {

    private var font: BitmapFont
    private var batch: SpriteBatch
    private val camera: OrthographicCamera = OrthographicCamera()

    init {
        camera.setToOrtho(false, 800f, 640f)
        batch = dropGame.batch
        font = dropGame.font
    }

    override fun render(delta: Float) {
        dropGame.assets.update()
        camera.update()

        batch.use {
            if (dropGame.assets.isFinished) {
                font.draw(it, "Welcome. Tap anywhere to start", 100f, 150f)
            } else {
                font.draw(it, "Loading", 100f, 150f)
            }
        }

        if (Gdx.input.isTouched && dropGame.assets.isFinished) {
            dropGame.setScreen<GameScreen>()
            dispose()
        }
    }

    override fun show() {
        MusicAssets.values().map { dropGame.assets.load(it) }
        SoundAssets.values().map { dropGame.assets.load(it) }
        TextureAssets.values().map { dropGame.assets.load(it) }
    }
}