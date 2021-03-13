package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.ModelComponent
import com.bubba.ecs.components.PlayerComponent
import ktx.ashley.get
import kotlin.math.atan2

class EnemySystem: EntitySystem(), EntityListener {

    private val entities = mutableListOf<Entity>()

    private val ghostMatrix4 = Matrix4()
    private val enemyGhostTranslation = Vector3()

    private val rotationBetweenEnemyAndPlayer = Quaternion()
    private val enemyModelTranslation = Vector3()
    private val playerModelTranslation = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(
            Family.all(
            CharacterMoveComponent::class.java,
            ModelComponent::class.java,
            EnemyComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {

        val playerEntity = engine.getEntitiesFor(
            Family.all(
                PlayerComponent::class.java,
                ModelComponent::class.java
            ).get()
        ) ?: return

        entities.forEach { entity ->
            val enemyModelComponent = entity[ModelComponent.mapper]!!
            val playerModel = playerEntity[0][ModelComponent.mapper]!!
            val quaternion = getRotationBetweenEnemyAndPlayer(playerModel, enemyModelComponent)

            val characterMoveComponent = entity[CharacterMoveComponent.mapper]!!
            characterMoveComponent.ghostObject.getWorldTransform(ghostMatrix4)
            ghostMatrix4.getTranslation(enemyGhostTranslation)
            enemyModelComponent.modelInstance.transform.set(
                enemyGhostTranslation.x,
                enemyGhostTranslation.y,
                enemyGhostTranslation.z,
                quaternion.x,
                quaternion.y,
                quaternion.z,
                quaternion.w
            )

            characterMoveComponent.movingDirection.set(-1f, 0f, 0f).rot(enemyModelComponent.modelInstance.transform).scl(3f * deltaTime)
            characterMoveComponent.characterController.setWalkDirection(characterMoveComponent.movingDirection)
        }
    }

    private fun getRotationBetweenEnemyAndPlayer(playerModel: ModelComponent, enemyModel: ModelComponent): Quaternion {
        playerModel.modelInstance.transform.getTranslation(playerModelTranslation)
        enemyModel.modelInstance.transform.getTranslation(enemyModelTranslation)

        val deltaX = playerModelTranslation.x - enemyModelTranslation.x
        val deltaZ = playerModelTranslation.z - enemyModelTranslation.z

        val angle = atan2(deltaX.toDouble(), deltaZ.toDouble())

        return rotationBetweenEnemyAndPlayer.setFromAxis(Vector3.Y, (Math.toDegrees(angle) + 90).toFloat())
    }

    override fun entityAdded(entity: Entity) {
        entities.add(entity)
    }

    override fun entityRemoved(entity: Entity) {
        entities.remove(entity)
    }
}