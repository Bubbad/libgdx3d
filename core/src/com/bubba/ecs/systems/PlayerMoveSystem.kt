package com.bubba.ecs.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.ModelComponent
import ktx.ashley.get

class PlayerMoveSystem(private val camera: PerspectiveCamera): EntitySystem(), EntityListener {

    private var playerEntity: Entity? = null
    private var characterMoveComponent: CharacterMoveComponent? = null
    private var modelComponent: ModelComponent? = null
    private val cameraYRotationVector = Vector3()
    private val playerTranslation = Vector3()
    private val ghostMatrix4 = Matrix4()

    private var totalTime = 0f

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(CharacterMoveComponent::class.java, ModelComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        val deltaX = -Gdx.input.deltaX * 0.5f
        val deltaY = -Gdx.input.deltaY * 0.5f
        totalTime += deltaTime
        cameraYRotationVector.set(0f,0f,0f)
        camera.rotate(camera.up, deltaX)
        cameraYRotationVector.set(camera.direction).crs(camera.up).nor()
        camera.direction.rotate(cameraYRotationVector, deltaY)

        if (playerEntity == null) {
            return
        }

        characterMoveComponent!!.characterDirection.set(-1f, 0f, 0f).rot(modelComponent!!.modelInstance.transform).nor()
        characterMoveComponent!!.movingDirection.set(0f, 0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterMoveComponent!!.movingDirection.add(camera.direction)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterMoveComponent!!.movingDirection.sub(camera.direction)
        }

        characterMoveComponent!!.characterController.setWalkDirection(characterMoveComponent!!.movingDirection.scl(0.5f))

        characterMoveComponent!!.ghostObject.getWorldTransform(ghostMatrix4)
        ghostMatrix4.getTranslation(playerTranslation)
        //modelComponent!!.modelInstance.transform.setTranslation(playerTranslation.x, playerTranslation.y, playerTranslation.z)

        modelComponent!!.modelInstance.transform.set(
                playerTranslation.x,
                playerTranslation.y,
                playerTranslation.z,
                camera.direction.x,
                camera.direction.y,
                camera.direction.z,
                0f)

        camera.position.set(
                playerTranslation.x,
                playerTranslation.y,
                playerTranslation.z,
        )

        camera.update(true)

    }

    override fun entityAdded(entity: Entity) {
        playerEntity = entity
        characterMoveComponent = entity[CharacterMoveComponent.mapper]!!
        modelComponent = entity[ModelComponent.mapper]!!
    }

    override fun entityRemoved(entity: Entity) {
        playerEntity = null
        characterMoveComponent = null
        modelComponent = null
    }
}