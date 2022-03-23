package com.tap.tapper.favv.interfaces

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface Screen {
    fun draw(batch: SpriteBatch)
    fun getNext(): Screen
    fun isNextScreenReady(): Boolean
}