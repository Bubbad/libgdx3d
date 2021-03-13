package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.bubba.GameScreen
import com.bubba.PlayerCollidingWithEnemyListener
import com.bubba.ecs.components.BulletComponent
import com.bubba.ecs.components.CharacterMoveComponent
import ktx.ashley.get
import ktx.ashley.has

class BulletCollisionSystem: EntitySystem(), EntityListener {

    private val collisionConfiguration = btDefaultCollisionConfiguration()
    private val collisionDispatcher = btCollisionDispatcher(collisionConfiguration)
    private val broadPhase = btDbvtBroadphase() // Is this OK?
    private val constraintSolver = btSequentialImpulseConstraintSolver()
    val collisionWorld = btDiscreteDynamicsWorld(collisionDispatcher, broadPhase, constraintSolver, collisionConfiguration)
    private val ghostpairCallback = btGhostPairCallback()

    private val maxSimulationSteps = 5
    private val fixedSimulationTimeStep = 1 / 60f

    private val playerCollidingWithEnemyListener = PlayerCollidingWithEnemyListener()

    init {
        broadPhase.overlappingPairCache.setInternalGhostPairCallback(ghostpairCallback)
        collisionWorld.gravity = Vector3(0f, -10.5f, 0f)

        playerCollidingWithEnemyListener.enable()
    }

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(BulletComponent::class.java).get(), this)
    }

    override fun update(deltaTime: Float) {
        collisionWorld.stepSimulation(deltaTime, maxSimulationSteps, fixedSimulationTimeStep)
    }

    override fun entityAdded(entity: Entity) {
        val bulletComponent = entity[BulletComponent.mapper]!!
        collisionWorld.addRigidBody(bulletComponent.body)
    }

    override fun entityRemoved(entity: Entity) {
        if (entity.has(BulletComponent.mapper)) {
            val bulletComponent = entity[BulletComponent.mapper]!!
            collisionWorld.removeRigidBody(bulletComponent.body)
        } else if (entity.has(CharacterMoveComponent.mapper)) {
            val characterMoveComponent = entity[CharacterMoveComponent.mapper]!!
            collisionWorld.removeCharacter(characterMoveComponent.characterController)
            collisionWorld.removeCollisionObject(characterMoveComponent.ghostObject)
        }
    }

    fun dispose() {
        collisionWorld.dispose()
        ghostpairCallback.dispose()
        constraintSolver.dispose()
        collisionConfiguration.dispose()
        collisionDispatcher.dispose()
        broadPhase.dispose()
        playerCollidingWithEnemyListener.dispose()
    }

}