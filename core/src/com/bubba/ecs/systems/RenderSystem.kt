package com.bubba.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.bubba.ecs.components.RainDropCounterComponent
import com.bubba.ecs.components.RenderComponent
import com.bubba.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class RenderSystem(private val batch: Batch,
                   private val font: BitmapFont,
                   private val camera: OrthographicCamera,
                   bucketEntity: Entity) :
        IteratingSystem(allOf(TransformComponent::class, RenderComponent::class).get()) {

    private val dropsCollectedComponent = bucketEntity.getComponent(RainDropCounterComponent::class.java)

    override fun update(deltaTime: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.use { batch ->
            super.update(deltaTime)
            font.draw(batch, "Drops: ${dropsCollectedComponent.dropsCollected}", 0f, 600f)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]!!
        val texture = entity[RenderComponent.mapper]!!.texture
        batch.draw(texture, transform.bounds.x, transform.bounds.y)
    }

}