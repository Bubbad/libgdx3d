package com.bubba.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.bubba.ecs.components.MoveComponent
import com.bubba.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class InputSystem(bucketEntity: Entity, private val camera: OrthographicCamera): EntitySystem() {

    private val bucketSpeed = bucketEntity.getComponent(MoveComponent::class.java).speed
    private val bucketTransform = bucketEntity[TransformComponent.mapper]!!
    private val touchPos = Vector3()

    override fun update(deltaTime: Float) {
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucketTransform.bounds.x = touchPos.x - 64f / 2f
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> {
                bucketSpeed.x = -500f
            }
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> {
                bucketSpeed.x = 500f
            }
            else -> {
                bucketSpeed.x = 0f
            }
        }
    }
}