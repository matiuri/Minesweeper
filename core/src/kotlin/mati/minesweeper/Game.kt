package mati.minesweeper

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import mati.advancedgdx.AdvancedGame
import mati.advancedgdx.assets.FontLoader.FontLoaderParameter
import mati.minesweeper.screens.Title

class Game : AdvancedGame() {
    override fun create() {
        super.create()
        init(this)
        Gdx.app.logLevel = LOG_DEBUG
        prepare()
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f)
    }

    private fun prepare() {
        scrManager.add("Title", Title(this))
        astManager.queue("UbuntuBGen", "fonts/Ubuntu-B.ttf", FreeTypeFontGenerator::class)
                .queue("UbuntuRGen", "fonts/Ubuntu-R.ttf", FreeTypeFontGenerator::class)
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
                .queue("ButtonUp", "GUI/ButtonUp.png", Texture::class)
                .queue("ButtonDown", "GUI/ButtonDown.png", Texture::class)
                .queue("ButtonLocked", "GUI/ButtonLocked.png", Texture::class)
                .load {
                    scrManager.loadAll()
                    scrManager.change("Title")
                }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()

        //FIXME: Remove this
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit()
    }
}
