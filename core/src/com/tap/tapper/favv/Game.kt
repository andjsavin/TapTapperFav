package com.tap.tapper.favv

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.tap.tapper.favv.factories.ScreenFactory
import com.tap.tapper.favv.interfaces.Screen

class Game : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var screenFactory: ScreenFactory
    private lateinit var screen: Screen
    private lateinit var music: Music
    override fun create() {
        batch = SpriteBatch()
        screenFactory = ScreenFactory()
        screen = screenFactory.prepareScreen()
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"))
        music.isLooping = true
        music.play()
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch.begin()

        if (screen.isNextScreenReady()) screen = screen.getNext()
        screen.draw(batch)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}