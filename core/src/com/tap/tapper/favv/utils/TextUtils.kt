package com.tap.tapper.favv.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class TextUtils {
    private var font: BitmapFont = BitmapFont(
        Gdx.files.internal("font.fnt"),
        Gdx.files.internal("font.png"), false)

    fun draw(batch: SpriteBatch, text: String, x: Float, y: Float, width: Float, height: Float, scale: Float) {
        font.data.setScale(scale)
        val layout = GlyphLayout(font, text)
        val fontX: Float = x + (width - layout.width) / 2
        val fontY: Float = y + (height + layout.height) / 2
        font.draw(batch, layout, fontX, fontY)
    }
}