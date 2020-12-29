package com.bubba

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.bubba.ecs.components.MoveComponent
import com.bubba.ecs.components.RainDropCounterComponent
import com.bubba.ecs.components.RenderComponent
import com.bubba.ecs.components.TransformComponent
import com.bubba.ecs.systems.CollisionSystem
import com.bubba.ecs.systems.InputSystem
import com.bubba.ecs.systems.MoveSystem
import com.bubba.ecs.systems.RaindropSpawnSystem
import com.bubba.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.info
import ktx.log.logger

class GameScreen(private val dropGame: DropGame) : KtxScreen {

    private val activeRaindrops: Array<Rectangle>
    private val camera: OrthographicCamera
    private lateinit var rainMusic: Music
    private lateinit var dropSound: Sound
    private lateinit var bucketTexture: Texture
    private lateinit var dropTexture: Texture
    private val batch: SpriteBatch = dropGame.batch
    private val font: BitmapFont = dropGame.font
    private val engine = dropGame.engine
    private val logger = logger<GameScreen>()

    private lateinit var bucketEntity: Entity

    init {
        logger.info { "Starting gamescreen" }

        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 640f)

        activeRaindrops = Array()
    }

    override fun show() {
        rainMusic = dropGame.assets.get(MusicAssets.Rain).apply {
            isLooping = true
            play()
        }

        bucketTexture = dropGame.assets.get(TextureAssets.Bucket)
        bucketEntity = engine.entity {
            with<RainDropCounterComponent>()
            this.entity.add(TransformComponent(Rectangle(800f / 2, 20f, 64f, 64f)))
            this.entity.add(RenderComponent(bucketTexture))
            this.entity.add(MoveComponent(Vector2()))
        }

        engine.addSystem(RenderSystem(batch, font, camera, bucketEntity))

        dropSound = dropGame.assets.get(SoundAssets.Drop)
        engine.addSystem(CollisionSystem(bucketEntity, dropSound))
        engine.addSystem(MoveSystem())
        engine.addSystem(InputSystem(bucketEntity, camera))

        dropTexture = dropGame.assets.get(TextureAssets.Drop)
        engine.addSystem(RaindropSpawnSystem(dropTexture))

    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}