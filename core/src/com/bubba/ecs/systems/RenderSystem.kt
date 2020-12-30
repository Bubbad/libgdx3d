package com.bubba.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.bubba.ecs.components.ModelComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem(private val camera: Camera, private val environment: Environment) :
        IteratingSystem(allOf(ModelComponent::class).get()) {

    private val batch = ModelBatch()

    override fun update(deltaTime: Float) {
        camera.update()
        batch.begin(camera)
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val model = entity[ModelComponent.mapper]!!.modelInstance
        batch.render(model, environment)
    }

}