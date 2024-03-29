package com.bubba

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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
import com.badlogic.gdx.physics.bullet.Bullet
import com.bubba.ecs.entities.EntityFactory
import com.bubba.ecs.systems.BulletCollisionSystem
import com.bubba.ecs.systems.RenderSystem
import ktx.app.KtxScreen
import ktx.log.info
import ktx.log.logger
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.PlayerComponent
import com.bubba.ecs.systems.EnemySpawnSystem
import com.bubba.ecs.systems.EnemySystem
import com.bubba.ecs.systems.KillEnemyOnContactSystem
import com.bubba.ecs.systems.PauseGameSystem
import com.bubba.ecs.systems.PlayerMoveSystem
import com.bubba.ecs.systems.ShootSystem


class GameScreen(private val dropGame: DropGame) : KtxScreen {

    private lateinit var gameScreenUI: GameScreenUI

    private val camera: PerspectiveCamera
    private val batch = ModelBatch()
    private val environment = Environment()
    private val font: BitmapFont = dropGame.font
    private val engine = Engine()
    private val logger = logger<GameScreen>()

    private val model: Model

    private val modelBuilder = ModelBuilder()
    private val wallHorizontal = modelBuilder.createBox(40f, 20f, 1f, Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.RED), FloatAttribute.createShininess(16f)), (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong())
    private val wallVertical = modelBuilder.createBox(1f, 20f, 40f, com.badlogic.gdx.graphics.g3d.Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(com.badlogic.gdx.graphics.Color.GREEN), com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createSpecular(com.badlogic.gdx.graphics.Color.WHITE), FloatAttribute.createShininess(16f)), (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong())
    private val groundModel = modelBuilder.createBox(40f, 1f, 40f, com.badlogic.gdx.graphics.g3d.Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(com.badlogic.gdx.graphics.Color.YELLOW), com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createSpecular(com.badlogic.gdx.graphics.Color.BLUE), FloatAttribute.createShininess(16f)), (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong())
    private val bulletCollisionSystem: BulletCollisionSystem

    private val playerComponent = PlayerComponent()

    init {
        Bullet.init()
        logger.info { "Starting gamescreen" }

        val fov = 67f
        camera = PerspectiveCamera(fov, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        val modelBuilder = ModelBuilder()
        val material = Material(ColorAttribute.createDiffuse(Color.BLUE))
        model = modelBuilder.createBox(5f, 5f, 5f, material,
                (VertexAttributes.Usage.Normal or VertexAttributes.Usage.Position).toLong())

        environment.set(ColorAttribute.createAmbientLight(0.4f, 0.4f, 0.4f, 1.0f))
        environment.add(DirectionalLight().set(Color.WHITE, Vector3(1.0f, -0.8f, -0.2f)))

        gameScreenUI = GameScreenUI(dropGame.assets, playerComponent, dropGame)
        Gdx.input.inputProcessor = gameScreenUI.stage

        bulletCollisionSystem = BulletCollisionSystem()
        engine.addSystem(bulletCollisionSystem)
        engine.addSystem(PlayerMoveSystem(camera))
        engine.addSystem(ShootSystem(camera, bulletCollisionSystem))
        engine.addSystem(EnemySystem())
        engine.addSystem(KillEnemyOnContactSystem(this))
        engine.addSystem(EnemySpawnSystem(model, 0f, 25f, 0f, bulletCollisionSystem))
        engine.addSystem(RenderSystem(camera, environment))
        engine.addSystem(PauseGameSystem(gameScreenUI))

        engine.addEntity(EntityFactory.createStaticEntity(model, 0f, 0f, 0f))
        engine.addEntity(EntityFactory.createCharacter(model, 5f, 25f, 5f, bulletCollisionSystem).add(playerComponent))
        createGround()

    }

    private fun createGround() {
        engine.addEntity(EntityFactory.createStaticEntity(groundModel,0f, 0f, 0f))
        engine.addEntity(EntityFactory.createStaticEntity(wallHorizontal, 0f, 10f, -20f))
        engine.addEntity(EntityFactory.createStaticEntity(wallHorizontal, 0f, 10f, 20f))
        engine.addEntity(EntityFactory.createStaticEntity(wallVertical, 20f, 10f, 0f))
        engine.addEntity(EntityFactory.createStaticEntity(wallVertical, -20f, 10f, 0f))
    }

    override fun render(delta: Float) {
        engine.update(delta)
        gameScreenUI.update(delta)
        gameScreenUI.render()
    }

    override fun show() {
        super.show()

        Gdx.input.isCursorCatched = true
    }

    override fun dispose() {
        super.dispose()
        model.dispose()
        bulletCollisionSystem.dispose()
        gameScreenUI.dispose()
    }

    fun remove(entity: Entity) {
        this.engine.removeEntity(entity)
        this.bulletCollisionSystem.entityRemoved(entity)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height) // TODO Resize UI
    }
}