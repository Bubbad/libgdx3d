package com.bubba

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState

class MotionState(private val transform: Matrix4): btMotionState() {

    override fun getWorldTransform(worldTrans: Matrix4) {
        worldTrans.set(transform)
    }

    override fun setWorldTransform(worldTrans: Matrix4) {
        transform.set(worldTrans)
    }
}