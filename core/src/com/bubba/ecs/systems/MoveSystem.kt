package com.bubba.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.bubba.ecs.components.MoveComponent
import com.bubba.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem: IteratingSystem(allOf(MoveComponent::class, TransformComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speed = entity[MoveComponent.mapper]!!.speed
        val bounds = entity[TransformComponent.mapper]!!.bounds

        bounds.x += speed.x * deltaTime
        bounds.y += speed.y * deltaTime

        if (bounds.x < 0) {
            bounds.x = 0f
        } else if (bounds.x > 800 - 64) {
            bounds.x = 800 - 64f
        }
    }

}