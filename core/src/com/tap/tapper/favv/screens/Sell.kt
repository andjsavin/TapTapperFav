package com.tap.tapper.favv.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.tap.tapper.favv.factories.ScreenFactory
import com.tap.tapper.favv.interfaces.Screen
import com.tap.tapper.favv.utils.TextUtils

class Sell: Screen, InputProcessor {
    private var next: Screen? = null
    private var ready = false
    private val screenFactory: ScreenFactory = ScreenFactory()
    private var honey = 0
    private var gold = 0
    private val background: Texture = Texture("sell.png")
    private val textUtils: TextUtils = TextUtils()
    private val sellR: Rectangle = Rectangle(
        0f,
        Gdx.graphics.height * 1111f/1920f,
        Gdx.graphics.width.toFloat(),
        Gdx.graphics.height * 302f/1920f
    )

    init {
        honey = Gdx.app.getPreferences("data").getInteger("honey", 0)
        gold = Gdx.app.getPreferences("data").getInteger("gold", 0)
        Gdx.input.inputProcessor = this
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
    }

    override fun draw(batch: SpriteBatch) {
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        textUtils.draw(
            batch,
            intToStr(honey),
            Gdx.graphics.width * 0.005f,
            Gdx.graphics.height * 0.31f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height * 0.1f,
            0.25f * Gdx.graphics.width/1080f
        )
        textUtils.draw(
            batch,
            intToStr(gold),
            Gdx.graphics.width * 0.005f,
            Gdx.graphics.height * 0.15f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height * 0.1f,
            0.25f * Gdx.graphics.width/1080f
        )
    }

    override fun getNext(): Screen {
        return next!!
    }

    override fun isNextScreenReady(): Boolean {
        return ready
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            Gdx.app.getPreferences("data").putInteger("honey", honey).flush()
            Gdx.app.getPreferences("data").putInteger("gold", gold).flush()
            next = screenFactory.prepareScreen("game")
            ready = true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (sellR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat()) && honey > 0) {
            honey -= 1
            gold += 2
            Gdx.app.getPreferences("data").putInteger("honey", honey).flush()
            Gdx.app.getPreferences("data").putInteger("gold", gold).flush()
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (sellR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat()) && honey > 0) {
            honey -= 1
            gold += 2
            Gdx.app.getPreferences("data").putInteger("honey", honey).flush()
            Gdx.app.getPreferences("data").putInteger("gold", gold).flush()
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }

    private fun intToStr(int: Int): String {
        return when {
            int < 10 -> "00000$int"
            int < 100 -> "0000$int"
            int < 1000 -> "000$int"
            int < 10000 -> "00$int"
            int < 100000 -> "0$int"
            else -> int.toString()
        }
    }
}