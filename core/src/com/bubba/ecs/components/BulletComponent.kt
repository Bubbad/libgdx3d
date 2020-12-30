package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.bubba.MotionState
import ktx.ashley.mapperFor

class BulletComponent(public val motionState: MotionState,
                      public val bodyInfo: btRigidBody.btRigidBodyConstructionInfo,
                      public val body: btRigidBody) : Component {
      companion object {
          val mapper = mapperFor<BulletComponent>()
      }

}