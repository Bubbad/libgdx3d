package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class EnemyComponent : Component {
    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }
    var isDead = false
}