package com.tap.tapper.favv.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.tap.tapper.favv.factories.ScreenFactory
import com.tap.tapper.favv.interfaces.Screen
import com.tap.tapper.favv.objects.Bee
import com.tap.tapper.favv.objects.Flower
import com.tap.tapper.favv.utils.TextUtils

class Game: Screen, InputProcessor {
    private var next: Screen? = null
    private var ready = false
    private val screenFactory: ScreenFactory = ScreenFactory()
    private val background: Texture = Texture("game.png")
    private var honey = 0
    private var golden = 0
    private var dapper = 0
    private var speedy = 0
    private var flower = 0
    private var flowers = 0
    private var fps = 0
    private var bps = 180
    private var hps = 60
    private val textUtils: TextUtils = TextUtils()
    private val bees: ArrayList<Bee> = ArrayList()
    private val flowersToDraw: ArrayList<Flower> = ArrayList()
    private val beeTextures: Array<Texture> = arrayOf(
        Texture("default_bee.png"),
        Texture("dapper_bee.png"),
        Texture("flower_bee.png"),
        Texture("golden_bee.png"),
        Texture("speedy_bee.png")
    )
    private val flowerTextures: Array<Texture> = arrayOf(
        Texture("flower_1.png"),
        Texture("flower_2.png")
    )
    private val sellR: Rectangle = Rectangle(
        Gdx.graphics.width * 747f/1080f,
        Gdx.graphics.height * 1646f/1920f,
        Gdx.graphics.width * 268f/1080f,
        Gdx.graphics.height * 122f/1920f
    )
    private val shopR: Rectangle = Rectangle(
        Gdx.graphics.width * 78f/1080f,
        Gdx.graphics.height * 1646f/1920f,
        Gdx.graphics.width * 268f/1080f,
        Gdx.graphics.height * 122f/1920f
    )

    init {
        honey = Gdx.app.getPreferences("data").getInteger("honey", 0)
        golden = Gdx.app.getPreferences("data").getInteger("golden", 0)
        dapper = Gdx.app.getPreferences("data").getInteger("dapper", 0)
        speedy = Gdx.app.getPreferences("data").getInteger("speedy", 0)
        flower = Gdx.app.getPreferences("data").getInteger("flower", 0)
        flowers = Gdx.app.getPreferences("data").getInteger("flowers", 0)
        bps = Gdx.app.getPreferences("data").getInteger("bps", 180)

        Gdx.input.inputProcessor = this
        Gdx.input.setCatchKey(Input.Keys.BACK, true)

        generateFlowers()
    }

    override fun draw(batch: SpriteBatch) {
        if (hps == 60) {
            honey += (1 + (180 - bps)/10) * (1 + (golden * 1 + speedy * 3 + dapper * 2 + flower * 4 + flowers))
            Gdx.app.getPreferences("data").putInteger("honey", honey).flush()
            Gdx.app.getPreferences("data").putInteger("golden", golden).flush()
            Gdx.app.getPreferences("data").putInteger("dapper", dapper).flush()
            Gdx.app.getPreferences("data").putInteger("speedy", speedy).flush()
            Gdx.app.getPreferences("data").putInteger("flower", flower).flush()
            Gdx.app.getPreferences("data").putInteger("flowers", flower).flush()
            Gdx.app.getPreferences("data").putInteger("bps", bps).flush()
            hps = 0
        }
        if (fps == bps) {
            fps = 0
            generateBee()
        }
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        var i = 0
        while (i < bees.size) {
            if ((bees[i].r.x < -bees[i].r.width && bees[i].vx < 0f) || (bees[i].r.x > Gdx.graphics.width && bees[i].vx > 0f)) {
                bees.removeAt(i)
                continue
            }
            bees[i].draw(batch)
            i++
        }
        for (flower in flowersToDraw) {
            flower.draw(batch)
        }
        textUtils.draw(
            batch,
            honeyToMoney(),
            0f,
            Gdx.graphics.height * 0.83f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height * 0.1f,
            0.25f * Gdx.graphics.width/1080f
        )
        fps++
        hps++
    }

    override fun getNext(): Screen {
        return next!!
    }

    override fun isNextScreenReady(): Boolean {
        return ready
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            next = screenFactory.prepareScreen()
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
        if (sellR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            next = screenFactory.prepareScreen("sell")
            ready = true
        }
        if (shopR.contains(screenX.toFloat(), (Gdx.graphics.height - screenY).toFloat())) {
            next = screenFactory.prepareScreen("shop")
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

    private fun honeyToMoney(): String {
        return when {
            honey < 10 -> "00000$honey"
            honey < 100 -> "0000$honey"
            honey < 1000 -> "000$honey"
            honey < 10000 -> "00$honey"
            honey < 100000 -> "0$honey"
            else -> honey.toString()
        }
    }

    private fun generateBee() {
        val mods: ArrayList<Int> = ArrayList()
        mods.add(0)
        if (dapper > 0) mods.add(1)
        if (flower > 0) mods.add(2)
        if (golden > 0) mods.add(3)
        if (speedy > 0) mods.add(4)
        val seed = MathUtils.random(1f)
        bees.add(
            Bee(
                beeTextures[mods.random()],
                Rectangle(
                    if (seed < 0.5f) Gdx.graphics.width.toFloat() else -100f,
                    MathUtils.random(Gdx.graphics.height * 0.6f) + Gdx.graphics.height * 0.1f,
                    Gdx.graphics.width * 0.1f,
                    Gdx.graphics.width * 0.1f
                ),
                if (seed < 0.5f) -300f else 300f
            )
        )
    }

    private fun generateFlowers() {
        val seed = MathUtils.random(1)
        for (i in 0 until flowers) {
            flowersToDraw.add(
                Flower(
                    flowerTextures[seed],
                    Rectangle(
                        MathUtils.random(Gdx.graphics.width * 0.95f),
                        MathUtils.random(Gdx.graphics.height * 0.12f),
                        if (seed == 1) Gdx.graphics.width * 62f/1080f else Gdx.graphics.width * 40f/1080f,
                        if (seed == 1) Gdx.graphics.height * 110f/1920f else Gdx.graphics.height * 86f/1920f
                    )
                )
            )
        }
    }
}