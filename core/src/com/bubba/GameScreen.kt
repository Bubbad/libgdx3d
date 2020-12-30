package com.bubba

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import com.bubba.ecs.components.ModelComponent
import com.bubba.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.log.info
import ktx.log.logger

class GameScreen(private val dropGame: DropGame) : KtxScreen {

    private val camera: PerspectiveCamera
    private val batch = ModelBatch()
    private val environment = Environment()
    private val font: BitmapFont = dropGame.font
    private val engine = dropGame.engine
    private val logger = logger<GameScreen>()

    private val model: Model
    private val position = Vector3()


    init {
        logger.info { "Starting gamescreen" }

        val fov = 67f
        camera = PerspectiveCamera(fov, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(10f, 10f, 10f)
        camera.lookAt(0f,0f,0f)
        camera.near = 1f
        camera.far = 300f
        camera.update()

        val modelBuilder = ModelBuilder()
        val material = Material(ColorAttribute.createDiffuse(Color.BLUE))
        model = modelBuilder.createBox(5f, 5f, 5f, material,
                (VertexAttributes.Usage.Normal or VertexAttributes.Usage.Position).toLong())

        environment.set(ColorAttribute.createAmbientLight(0.4f, 0.4f, 0.4f, 1.0f))
        environment.add(DirectionalLight().set(Color.WHITE, Vector3(1.0f, -0.8f, -0.2f)))

        engine.entity {
            this.entity.add(ModelComponent(model, 5f, 5f, 5f))
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun show() {
        super.show()
        engine.addSystem(RenderSystem(camera, environment))

    }

    override fun dispose() {
        super.dispose()
        model.dispose()
    }

}