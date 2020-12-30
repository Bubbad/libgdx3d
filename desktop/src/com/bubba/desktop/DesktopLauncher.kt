package com.bubba.desktop

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.bubba.DropGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.height = DropGame.VIRTUAL_HEIGHT
        config.width = DropGame.VIRTUAL_WIDTH
        LwjglApplication(DropGame(), config)
    }
}