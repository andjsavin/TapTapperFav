package com.tap.tapper.favv.objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Flower(private val t: Texture, private val r: Rectangle) {
    fun draw(batch: SpriteBatch) {
        batch.draw(t, r.x, r.y, r.width, r.height)
    }
}