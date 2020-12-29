package com.bubba

import ktx.app.KtxGame
import ktx.app.KtxScreen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

class MyGdxGame : KtxGame<KtxScreen>() {
    var batch: SpriteBatch? = null
    var img: Texture? = null
    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()
        batch!!.draw(img, 0f, 0f)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}