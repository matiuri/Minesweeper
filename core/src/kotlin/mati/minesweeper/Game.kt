package mati.minesweeper

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.InputListener
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.FontLoader.FontLoaderParameter
import mati.advancedgdx.utils.isDesktop
import mati.minesweeper.board.Cell
import mati.minesweeper.screens.GameS
import mati.minesweeper.screens.Title
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Game(val cellInput: KClass<out InputListener>) : AdvancedGame() {
    var cursors: Array<Cursor> by Delegates.notNull<Array<Cursor>>()

    override fun create() {
        super.create()
        init(this)
        Gdx.app.logLevel = LOG_DEBUG
        prepare()
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
    }

    private fun prepare() {
        scrManager.add("Title", Title(this)).add("Game", GameS(this))
        astManager.queue("UbuntuBGen", "fonts/Ubuntu-B.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuRGen", "fonts/Ubuntu-R.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuMRGen", "fonts/UbuntuMono-R.ttf", FreeTypeFontGenerator::class)
                .queue("Title", "TitleFont", BitmapFont::class, FontLoaderParameter(astManager["UbuntuBGen"]) {
                    it.size = 64
                    it.color = Color.YELLOW
                    it.borderColor = Color.BLACK
                    it.borderWidth = 5f
                })
                .queue("GeneralW", "GeneralFontW", BitmapFont::class, FontLoaderParameter(astManager["UbuntuRGen"]) {
                    it.size = 32
                    it.color = Color.WHITE
                    it.borderColor = Color.BLACK
                    it.borderWidth = 1f
                })
                .queue("GeneralB", "GeneralFontB", BitmapFont::class, FontLoaderParameter(astManager["UbuntuRGen"]) {
                    it.size = 32
                    it.color = Color.BLACK
                    it.borderColor = Color.WHITE
                    it.borderWidth = 1f
                })
                .queue("TimerF", "TimerFont", BitmapFont::class, FontLoaderParameter(astManager["UbuntuMRGen"]) {
                    it.size = 24
                    it.color = Color.GOLD
                    it.borderColor = Color.WHITE
                    it.borderWidth = 0.5f
                })
                .queue("ButtonUp", "GUI/ButtonUp.png", Texture::class)
                .queue("ButtonDown", "GUI/ButtonDown.png", Texture::class)
                .queue("ButtonLocked", "GUI/ButtonLocked.png", Texture::class)
                .queue("CellUp", "game/CellUp.png", Texture::class)
                .queue("CellDown", "game/CellDown.png", Texture::class)
                .queue("CellOpen", "game/CellOpen.png", Texture::class)
                .queue("N", "game/", ".png", Texture::class, 1, 7)
                .queue("Mine", "game/Mine.png", Texture::class)
                .queue("Mine1", "game/Mine1.png", Texture::class)
                .queue("Flag", "game/Flag.png", Texture::class)
                .queue("CursorBlue", "GUI/CursorBlue.png", Pixmap::class)
                .queue("CursorRed", "GUI/CursorRed.png", Pixmap::class)
                .queue("GUIb", "GUI/GUIb.png", Texture::class)
                .queue("GUIt", "GUI/GUIt.png", Texture::class)
                .queue("CamDD", "GUI/CamDownDown.png", Texture::class)
                .queue("CamDU", "GUI/CamDownUp.png", Texture::class)
                .queue("CamLD", "GUI/CamLeftDown.png", Texture::class)
                .queue("CamLU", "GUI/CamLeftUp.png", Texture::class)
                .queue("CamRD", "GUI/CamRightDown.png", Texture::class)
                .queue("CamRU", "GUI/CamRightUp.png", Texture::class)
                .queue("CamUD", "GUI/CamUpDown.png", Texture::class)
                .queue("CamUU", "GUI/CamUpUp.png", Texture::class)
                .queue("CamPU", "GUI/CamPlusUp.png", Texture::class)
                .queue("CamPD", "GUI/CamPlusDown.png", Texture::class)
                .queue("CamMU", "GUI/CamMinusUp.png", Texture::class)
                .queue("CamMD", "GUI/CamMinusDown.png", Texture::class)
                .load {
                    if (isDesktop()) {
                        cursors = arrayOf(Gdx.graphics.newCursor(astManager["CursorBlue", Pixmap::class], 0, 0),
                                Gdx.graphics.newCursor(astManager["CursorRed", Pixmap::class], 0, 0))
                        Gdx.graphics.setCursor(cursors[0])
                    }
                    Cell.init(this)
                    scrManager.loadAll()
                    //FIXME: scrManager.change("Title")
                    scrManager.change("Game")
                }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()

        //FIXME: Remove this
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit()
    }
}
