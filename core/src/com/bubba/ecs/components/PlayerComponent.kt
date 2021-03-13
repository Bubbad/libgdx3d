package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PlayerComponent: Component {
    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    private var score = 0

    fun increaseScore() {
        score += 1
    }

    fun getScore(): Int {
        return score
    }
}