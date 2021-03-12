package com.bubba.ecs.systems

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

private const val TIME_BETWEEN_PLAYER_JUMPS_MS = 1000

class PlayerMoveSystem(private val camera: PerspectiveCamera): EntitySystem(), EntityListener {

    private var playerEntity: Entity? = null
    private var characterMoveComponent: CharacterMoveComponent? = null
    private var modelComponent: ModelComponent? = null
    private val cameraYRotationVector = Vector3()
    private val playerTranslation = Vector3()
    private val playerSidewaysTmpVector = Vector3()
    private val playerJumpTmpVector = Vector3()
    private var lastPlayerJump = 0L
    private val ghostMatrix4 = Matrix4()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(CharacterMoveComponent::class.java, ModelComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        if (playerEntity == null) {
            return
        }

        rotateCameraFromMouseMovement()
        moveCharacterIfKeysPressed(deltaTime)
        moveCameraToPlayerPosition()
    }

    private fun moveCameraToPlayerPosition() {
        camera.position.set(
            playerTranslation.x,
            playerTranslation.y,
            playerTranslation.z,
        )

        camera.update(true)
    }

    private fun moveCharacterIfKeysPressed(deltaTime: Float) {
        characterMoveComponent!!.movingDirection.set(0f, 0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterMoveComponent!!.movingDirection.add(camera.direction)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterMoveComponent!!.movingDirection.sub(camera.direction)
        }

        playerSidewaysTmpVector.set(0f, 0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerSidewaysTmpVector.set(camera.direction).crs(camera.up).scl(-1f)
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerSidewaysTmpVector.set(camera.direction).crs(camera.up)
        }
        characterMoveComponent!!.movingDirection.add(playerSidewaysTmpVector)

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && System.currentTimeMillis() - lastPlayerJump > TIME_BETWEEN_PLAYER_JUMPS_MS) {
            lastPlayerJump = System.currentTimeMillis()
            playerJumpTmpVector.set(camera.up).scl(10f)
            characterMoveComponent!!.characterController.jump(playerJumpTmpVector)
        }

        characterMoveComponent!!.characterController.setWalkDirection(characterMoveComponent!!.movingDirection.scl(10f * deltaTime))

        updateCharacterPositionAndRotationAccordingToBullet()
    }

    private fun updateCharacterPositionAndRotationAccordingToBullet() {
        characterMoveComponent!!.ghostObject.getWorldTransform(ghostMatrix4)
        ghostMatrix4.getTranslation(playerTranslation)

        // So far this has been enough - the camera.direction.x/y/z below likely is wrong(?)
        //modelComponent!!.modelInstance.transform.setTranslation(playerTranslation.x, playerTranslation.y, playerTranslation.z)

        modelComponent!!.modelInstance.transform.set(
            playerTranslation.x,
            playerTranslation.y,
            playerTranslation.z,
            camera.direction.x,
            camera.direction.y,
            camera.direction.z,
            0f
        )
    }

    private fun rotateCameraFromMouseMovement() {
        val deltaX = -Gdx.input.deltaX * 0.5f
        val deltaY = -Gdx.input.deltaY * 0.5f
        cameraYRotationVector.set(0f, 0f, 0f)
        camera.rotate(camera.up, deltaX)
        cameraYRotationVector.set(camera.direction).crs(camera.up).nor()
        camera.direction.rotate(cameraYRotationVector, deltaY)
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