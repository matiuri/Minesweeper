package mati.minesweeper.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import mati.minesweeper.Game
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment

fun main(arg: Array<String>) {
    val dm: DisplayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode;
    val cfg = LwjglApplicationConfiguration()
    cfg.resizable = false
    cfg.fullscreen = true
    cfg.width = dm.width
    cfg.height = dm.height
    LwjglApplication(Game(), cfg)
}
