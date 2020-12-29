package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.mapperFor

class TransformComponent(val bounds: Rectangle) : Component {
    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}