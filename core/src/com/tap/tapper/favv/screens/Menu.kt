package com.tap.tapper.favv.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.tap.tapper.favv.factories.ScreenFactory
import com.tap.tapper.favv.interfaces.Screen

class Menu: Screen, InputProcessor {
    private var next: Screen? = null
    private var ready = false
    private val screenFactory: ScreenFactory = ScreenFactory()
    private val background: Texture = Texture("main_menu.png")
    private val startR: Rectangle = Rectangle(
        Gdx.graphics.width * 194f/1080f,
        Gdx.graphics.height * 197f/1920f,
        Gdx.graphics.width * 704f/1080f,
        Gdx.graphics.height * 250f/1920f
    )

    init {
        Gdx.input.inputProcessor = this
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
    }

    override fun draw(batch: SpriteBatch) {
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun getNext(): Screen {
        return next!!
    }

    override fun isNextScreenReady(): Boolean {
        return ready
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            Gdx.app.exit()
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
        if (startR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            next = screenFactory.prepareScreen("game")
            ready = true
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}