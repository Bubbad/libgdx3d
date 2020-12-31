package com.bubba.ecs.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.physics.bullet.collision.btBoxShape
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.bubba.MotionState
import com.bubba.ecs.components.BulletComponent
import com.bubba.ecs.components.CharacterMoveComponent
import com.bubba.ecs.components.ModelComponent
import com.bubba.ecs.systems.BulletCollisionSystem

class EntityFactory {
    companion object {
        fun createStaticEntity(model: Model, x: Float, y: Float, z: Float): Entity {
            val entity = Entity()

            val boundingBox = BoundingBox()
            model.calculateBoundingBox(boundingBox)

            val boxHalfVector = Vector3(
                    boundingBox.width * 0.5f,
                    boundingBox.height * 0.5f,
                    boundingBox.depth * 0.5f)
            val collisionShape = btBoxShape(boxHalfVector)

            val bodyInfo = btRigidBody.btRigidBodyConstructionInfo(0f, null, collisionShape, Vector3.Zero) // Why not specify motionState here?
            val body = btRigidBody(bodyInfo)
            body.userData = entity

            val modelComponent = ModelComponent(model, x, y, z)
            val motionState = MotionState(modelComponent.modelInstance.transform)
            body.motionState = motionState

            val bulletComponent = BulletComponent(motionState, bodyInfo, body)

            entity.add(bulletComponent)
            entity.add(modelComponent)

            return entity
        }

        fun createCharacter(model: Model,
                             x: Float,
                             y: Float,
                             z: Float,
                             bulletSystem: BulletCollisionSystem): Entity {
            val entity = Entity()

            val modelComponent = ModelComponent(model, x, y, z)
            entity.add(modelComponent)

            val ghostObject = btPairCachingGhostObject()
            ghostObject.worldTransform = modelComponent.modelInstance.transform

            val ghostShape = btCapsuleShape(2f, 2f)
            ghostObject.collisionShape = ghostShape
            ghostObject.collisionFlags = btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT
            ghostObject.userData = entity

            val movingDirection = Vector3()
            val characterController = btKinematicCharacterController(ghostObject, ghostShape, 0.35f, Vector3(0f, 1.0f, 0f))

            val characterMoveComponent = CharacterMoveComponent(
                    ghostShape,
                    ghostObject,
                    characterController,
                    movingDirection)
            entity.add(characterMoveComponent)

            bulletSystem.collisionWorld.addCollisionObject(ghostObject,
                    btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                    btBroadphaseProxy.CollisionFilterGroups.AllFilter)

            bulletSystem.collisionWorld.addAction(characterController)

            return entity
        }
    }
}