package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.bubba.ecs.components.EnemyComponent
import com.bubba.ecs.components.PlayerComponent
import ktx.ashley.get
import ktx.ashley.has

private const val PLAYER_SHOOTING_RANGE_LIMIT = 50f

class ShootSystem(private val camera: PerspectiveCamera,
                  private val bulletCollisionSystem: BulletCollisionSystem)
    : EntitySystem(), EntityListener {

    private val rayTestCallBack = ClosestRayResultCallback(Vector3.Zero, Vector3.Z)
    private val rayFrom = Vector3()
    private val rayTo = Vector3()

    private var playerComponent: PlayerComponent? = null

    override fun addedToEngine(engine: Engine) {
        val family = Family.all(PlayerComponent::class.java).get()
        engine.addEntityListener(family, this)
        val playerComponents = engine.getEntitiesFor(family)
        if (playerComponents.size() > 0) {
            entityAdded(playerComponents[0])
        }
    }

    override fun update(deltaTime: Float) {
        if (playerComponent == null) return

        fireIfMouseClicked()
    }

    private fun fireIfMouseClicked() {
        if (Gdx.input.justTouched()) {
            fire()
        }
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
                playerComponent!!.increaseScore()
            }
        }
    }

    override fun entityAdded(entity: Entity) {
        playerComponent = entity[PlayerComponent.mapper]
    }

    override fun entityRemoved(entity: Entity) {
        playerComponent = null
    }
}
