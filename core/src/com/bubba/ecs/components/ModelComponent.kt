package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import ktx.ashley.mapperFor

class ModelComponent(private val model: Model, x: Float, y: Float, z: Float): Component {
    companion object {
        val mapper = mapperFor<ModelComponent>()
    }

    val modelInstance: ModelInstance = ModelInstance(model, x, y, z)
}