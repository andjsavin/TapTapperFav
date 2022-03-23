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

class Shop: Screen, InputProcessor {
    private var next: Screen? = null
    private var ready = false
    private val screenFactory: ScreenFactory = ScreenFactory()
    private val background: Texture = Texture("shop.png")
    private var honey = 0
    private var golden = 0
    private var dapper = 0
    private var speedy = 0
    private var flower = 0
    private var flowers = 0
    private var gold = 0
    private var bps = 180
    private val textUtils: TextUtils = TextUtils()
    private val beeR: Rectangle = Rectangle(
        Gdx.graphics.width * 480f/1080f,
        Gdx.graphics.height * 893f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val flowerR: Rectangle = Rectangle(
        Gdx.graphics.width * 480f/1080f,
        Gdx.graphics.height * 1136f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val dapperR: Rectangle = Rectangle(
        Gdx.graphics.width * 266f/1080f,
        Gdx.graphics.height * 1013f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val speedyR: Rectangle = Rectangle(
        Gdx.graphics.width * 266f/1080f,
        Gdx.graphics.height * 771f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val goldenR: Rectangle = Rectangle(
        Gdx.graphics.width * 480f/1080f,
        Gdx.graphics.height * 643f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val hiveR: Rectangle = Rectangle(
        Gdx.graphics.width * 698f/1080f,
        Gdx.graphics.height * 1013f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )
    private val fbR: Rectangle = Rectangle(
        Gdx.graphics.width * 698f/1080f,
        Gdx.graphics.height * 771f/1920f,
        Gdx.graphics.width * 128f/1080f,
        Gdx.graphics.height * 215f/1920f
    )

    init {
        honey = Gdx.app.getPreferences("data").getInteger("honey", 0)
        golden = Gdx.app.getPreferences("data").getInteger("golden", 0)
        dapper = Gdx.app.getPreferences("data").getInteger("dapper", 0)
        speedy = Gdx.app.getPreferences("data").getInteger("speedy", 0)
        flower = Gdx.app.getPreferences("data").getInteger("flower", 0)
        flowers = Gdx.app.getPreferences("data").getInteger("flowers", 0)
        bps = Gdx.app.getPreferences("data").getInteger("bps", 180)
        gold = Gdx.app.getPreferences("data").getInteger("gold", 0)
        Gdx.input.inputProcessor = this
        Gdx.input.setCatchKey(Input.Keys.BACK, true)
    }

    override fun draw(batch: SpriteBatch) {
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        textUtils.draw(
            batch,
            intToStr(gold),
            0f,
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
            Gdx.app.getPreferences("data").putInteger("golden", golden).flush()
            Gdx.app.getPreferences("data").putInteger("dapper", dapper).flush()
            Gdx.app.getPreferences("data").putInteger("speedy", speedy).flush()
            Gdx.app.getPreferences("data").putInteger("flower", flower).flush()
            Gdx.app.getPreferences("data").putInteger("flowers", flower).flush()
            Gdx.app.getPreferences("data").putInteger("bps", bps).flush()
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
        if (beeR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 500) {
                gold -= 500
                if (bps > 20) bps -= 10
            }
        }
        if (flowerR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 1000) {
                gold -= 1000
                flowers += 2
            }
        }
        if (fbR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 3000) {
                gold -= 3000
                flowers += 6
            }
        }
        if (dapperR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 1000) {
                gold -= 1000
                dapper++
            }
        }
        if (goldenR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 1000) {
                gold -= 1000
                golden++
            }
        }
        if (speedyR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 1500) {
                gold -= 1500
                speedy++
            }
        }
        if (hiveR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            if (gold > 250) {
                gold -= 250
                honey += 3
            }
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