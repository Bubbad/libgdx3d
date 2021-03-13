package com.bubba

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.bullet.collision.ContactListener
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.EnemyComponent
import ktx.ashley.get
import ktx.ashley.has
import ktx.ashley.hasNot
import ktx.log.info
import ktx.log.logger

class PlayerCollidingWithEnemyListener : ContactListener() {

    override fun onContactStarted(colObj0: btCollisionObject, colObj1: btCollisionObject) {
        if (colObj0.userData !is Entity || colObj1.userData !is Entity) {
            return
        }

        val entity0 = colObj0.userData as Entity
        val entity1 = colObj1.userData as Entity
        // Assumes CharacterMoveComponent is only applied to Enemies and Player
        if (entity0.hasNot(CharacterMoveComponent.mapper) || entity1.hasNot(CharacterMoveComponent.mapper)) {
            return
        }

        if (entity0.has(EnemyComponent.mapper)) {
            val enemyComponent = entity0[EnemyComponent.mapper]!!
            enemyComponent.isDead = true
        } else if (entity1.has(EnemyComponent.mapper)) {
            val enemyComponent = entity1[EnemyComponent.mapper]!!
            enemyComponent.isDead = true
        }
    }
}