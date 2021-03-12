package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.btConvexShape
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController
import ktx.ashley.mapperFor

class CharacterMoveComponent(
        val ghostShape: btConvexShape,
        val ghostObject: btPairCachingGhostObject,
        val characterController: btKinematicCharacterController,
        val movingDirection: Vector3
): Component {

    companion object {
        val mapper = mapperFor<CharacterMoveComponent>()
    }

    //val characterDirection = Vector3() // Re-add when needed
}