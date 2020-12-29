package com.bubba.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.mapperFor

class RenderComponent(val texture: Texture) : Component {
    companion object {
        val mapper = mapperFor<RenderComponent>()
    }
}