package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.ModelComponent
import ktx.ashley.get

class EnemySystem: EntitySystem(), EntityListener {

    private val entities = mutableListOf<Entity>()

    private val ghostMatrix4 = Matrix4()
    private val enemyTranslation = Vector3()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(
            Family.all(
            CharacterMoveComponent::class.java,
            ModelComponent::class.java,
            EnemyComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        entities.forEach { entity ->
            val characterMoveComponent = entity[CharacterMoveComponent.mapper]!!
            val modelComponent = entity[ModelComponent.mapper]!!

            characterMoveComponent.ghostObject.getWorldTransform(ghostMatrix4)
            ghostMatrix4.getTranslation(enemyTranslation)

            modelComponent.modelInstance.transform.set(enemyTranslation.x, enemyTranslation.y, enemyTranslation.z, 0f, 0f, 0f, 0f)
        }
    }

    override fun entityAdded(entity: Entity) {
        entities.add(entity)
    }

    override fun entityRemoved(entity: Entity) {
        entities.remove(entity)
    }
}