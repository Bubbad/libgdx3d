package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class MoveComponent(val speed: Vector2): Component {
    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}
