package com.bubba

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen

class DropGame : KtxGame<KtxScreen>() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    val assets = AssetManager()
    val engine = PooledEngine()

    private lateinit var loadingScreen: LoadingScreen
    private lateinit var gameScreen: GameScreen

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.data.setScale(1.5f)
        gameScreen = GameScreen(this)
        loadingScreen = LoadingScreen(this)
        addScreen(gameScreen)
        addScreen(loadingScreen)
        setScreen<LoadingScreen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        assets.dispose()
        super.dispose()
    }
}