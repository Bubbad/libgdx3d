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
import com.bubba.ecs.components.BulletComponent
import ktx.ashley.get

class BulletCollisionSystem: EntitySystem(), EntityListener {

    private val collisionConfiguration = btDefaultCollisionConfiguration()
    private val collisionDispatcher = btCollisionDispatcher(collisionConfiguration)
    private val broadPhase = btDbvtBroadphase() // Is this OK?
    private val constraintSolver = btSequentialImpulseConstraintSolver()
    public val collisionWorld = btDiscreteDynamicsWorld(collisionDispatcher, broadPhase, constraintSolver, collisionConfiguration)
    private val ghostpairCallback = btGhostPairCallback()

    private val maxSimulationSteps = 5
    private val fixedSimulationTimeStep = 1 / 60f

    init {
        broadPhase.overlappingPairCache.setInternalGhostPairCallback(ghostpairCallback)
        collisionWorld.gravity = Vector3(0f, -10.5f, 0f)
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
        val bulletComponent = entity[BulletComponent.mapper]!!
        collisionWorld.removeRigidBody(bulletComponent.body)
    }

    fun dispose() {
        collisionWorld.dispose()
        ghostpairCallback.dispose()
        constraintSolver.dispose()
        collisionConfiguration.dispose()
        collisionDispatcher.dispose()
        broadPhase.dispose()
    }

}