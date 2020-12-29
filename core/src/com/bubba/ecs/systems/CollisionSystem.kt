package com.bubba.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.audio.Sound
import com.bubba.ecs.components.RainDropCounterComponent
import com.bubba.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class CollisionSystem(bucket: Entity, private val dropSound: Sound): IteratingSystem(allOf(TransformComponent::class).exclude(RainDropCounterComponent::class.java).get()) {

    private val bucketBound = bucket.getComponent(TransformComponent::class.java).bounds
    private val bucketCounter = bucket.getComponent(RainDropCounterComponent::class.java)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val entityBound = entity[TransformComponent.mapper]!!.bounds

        if (entityBound.overlaps(bucketBound)) {
            bucketCounter.dropsCollected++
            engine.removeEntity(entity)
            dropSound.play()
        }
    }
}