package com.tap.tapper.favv.factories

import com.tap.tapper.favv.interfaces.Screen
import com.tap.tapper.favv.screens.Game
import com.tap.tapper.favv.screens.Menu
import com.tap.tapper.favv.screens.Sell
import com.tap.tapper.favv.screens.Shop

class ScreenFactory {
    fun prepareScreen(screen: String = "menu"): Screen {
        return when (screen) {
            "game" -> Game()
            "sell" -> Sell()
            "shop" -> Shop()
            else -> Menu()
        }
    }
}