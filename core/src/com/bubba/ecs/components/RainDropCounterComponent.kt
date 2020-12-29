package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class RainDropCounterComponent : Component {
    companion object {
        val mapper = mapperFor<RainDropCounterComponent>()
    }

    var dropsCollected = 0
}