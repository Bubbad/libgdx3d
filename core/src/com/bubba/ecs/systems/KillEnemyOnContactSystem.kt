package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.bubba.GameScreen
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.ModelComponent
import ktx.ashley.get

class KillEnemyOnContactSystem(private val game: GameScreen): EntitySystem(), EntityListener {

    private val entities = mutableListOf<Entity>()

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(
            Family.all(
                CharacterMoveComponent::class.java,
                ModelComponent::class.java,
                EnemyComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        entities.forEach { entity ->
            val enemyComponent = entity[EnemyComponent.mapper]!!
            if(enemyComponent.isDead) {
                game.remove(entity)
            }
        }
    }

    override fun entityAdded(entity: Entity) {
        entities.add(entity)
    }

    override fun entityRemoved(entity: Entity) {
        entities.remove(entity)
    }
}