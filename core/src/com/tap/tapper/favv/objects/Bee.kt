package com.tap.tapper.favv.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Bee(
    private val t: Texture,
    val r: Rectangle,
    val vx: Float,
    private var vy: Float = 100f
) {
    private var fps = 0

    fun draw(batch: SpriteBatch) {
        if (fps == 15) {
            vy *= -1f
            fps = 0
        }
        batch.draw(t, r.x, r.y, r.width, r.height, 0, 0, 186, 186, vx > 0, false)
        r.x += vx * Gdx.graphics.deltaTime
        r.y += vy * Gdx.graphics.deltaTime
        fps++
    }
}