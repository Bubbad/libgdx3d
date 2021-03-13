package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g3d.Model
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.ModelComponent
import com.bubba.ecs.entities.EntityFactory
import kotlin.random.Random

private const val SPAWN_RATE_MS = 5000
private const val MAX_ENEMIES = 5

class EnemySpawnSystem(
    private val model: Model,
    private val x: Float,
    private val y: Float,
    private val z: Float,
    private val bulletSystem: BulletCollisionSystem
) : EntitySystem(), EntityListener {

    private val entities = mutableListOf<Entity>()
    private var lastSpawnTimestamp: Long = 0

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(
            Family.all(
                CharacterMoveComponent::class.java,
                ModelComponent::class.java,
                EnemyComponent::class.java
            ).get(), this
        )
    }

    override fun update(deltaTime: Float) {
        val currentTimeMillis = System.currentTimeMillis()
        if (entities.size < MAX_ENEMIES && currentTimeMillis - lastSpawnTimestamp > SPAWN_RATE_MS) {
            lastSpawnTimestamp = currentTimeMillis
            spawnEnemy()
        }
    }

    private fun spawnEnemy() {
        val deltaX = (1..3).random()
        val deltaZ = (1..3).random()

        engine.addEntity(EntityFactory.createCharacter(model, x + deltaX, y, z + deltaZ, bulletSystem).add(EnemyComponent()))
    }

    override fun entityAdded(entity: Entity) {
        entities.add(entity)
    }

    override fun entityRemoved(entity: Entity) {
        entities.remove(entity)
    }
}