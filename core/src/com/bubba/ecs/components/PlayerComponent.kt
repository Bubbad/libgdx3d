package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PlayerComponent: Component {
    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    private var score = 0
    private var health = 50

    fun increaseScore() {
        score += 1
    }

    fun getScore() = score

    fun decreaseHealth(damage : Int) {
        health -= damage
    }

    fun getHealth() = health
}