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
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.ModelComponent
import com.bubba.ecs.components.PlayerComponent
import ktx.ashley.get
import ktx.ashley.has

private const val TIME_BETWEEN_PLAYER_JUMPS_MS = 1000
private const val PLAYER_SHOOTING_RANGE_LIMIT = 50f

class PlayerMoveSystem(private val camera: PerspectiveCamera,
                       private val bulletCollisionSystem: BulletCollisionSystem)
    : EntitySystem(), EntityListener {

    private var playerEntity: Entity? = null
    private var characterMoveComponent: CharacterMoveComponent? = null
    private var modelComponent: ModelComponent? = null
    private val cameraYRotationVector = Vector3()
    private val playerTranslation = Vector3()
    private val playerSidewaysTmpVector = Vector3()
    private val playerJumpTmpVector = Vector3()
    private var lastPlayerJump = 0L
    private val ghostMatrix4 = Matrix4()

    private val rayTestCallBack = ClosestRayResultCallback(Vector3.Zero, Vector3.Z)
    private val rayFrom = Vector3()
    private val rayTo = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(
            CharacterMoveComponent::class.java,
            ModelComponent::class.java,
            PlayerComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        if (playerEntity == null) {
            return
        }

        rotateCameraFromMouseMovement()
        moveCharacterIfKeysPressed(deltaTime)
        moveCameraToPlayerPosition()
        fireIfMouseClicked()
    }

    private fun fireIfMouseClicked() {
        if (Gdx.input.justTouched()) {
            fire()
        }
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

        updateModelOfCharacterPositionAndRotationAccordingToBullet()
    }

    private fun updateModelOfCharacterPositionAndRotationAccordingToBullet() {
        characterMoveComponent!!.ghostObject.getWorldTransform(ghostMatrix4)
        ghostMatrix4.getTranslation(playerTranslation)

        // Should we fetch the quaternion of the ghostObject and put that in the modelInstance transform instead?
        modelComponent!!.modelInstance.transform.set(
            playerTranslation.x,
            playerTranslation.y,
            playerTranslation.z,
            camera.direction.x,
            camera.direction.y,
            camera.direction.z,
            0f)
    }

    private fun rotateCameraFromMouseMovement() {
        val deltaX = -Gdx.input.deltaX * 0.5f
        val deltaY = -Gdx.input.deltaY * 0.5f
        cameraYRotationVector.set(0f, 0f, 0f)
        camera.rotate(camera.up, deltaX)
        cameraYRotationVector.set(camera.direction).crs(camera.up).nor()
        camera.direction.rotate(cameraYRotationVector, deltaY)
    }

    private fun fire() {
        // The press is always in the middle of screen
        val ray = camera.getPickRay((Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        rayFrom.set(ray.origin)
        rayTo.set(ray.direction).scl(PLAYER_SHOOTING_RANGE_LIMIT).add(rayFrom)

        rayTestCallBack.collisionObject = null
        rayTestCallBack.closestHitFraction = 1f
        rayTestCallBack.setRayFromWorld(rayFrom)
        rayTestCallBack.setRayToWorld(rayTo)

        bulletCollisionSystem.rayTest(rayFrom, rayTo, rayTestCallBack)

        if (rayTestCallBack.hasHit() && rayTestCallBack.collisionObject.userData is Entity) {
            val collisionObject = rayTestCallBack.collisionObject.userData as Entity

            if (collisionObject.has(EnemyComponent.mapper)) {
                collisionObject[EnemyComponent.mapper]!!.isDead = true
            }
        }
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