package mati.minesweeper.statistics

import mati.advancedgdx.io.Serializable
import mati.minesweeper.Game
import mati.minesweeper.screens.NewGame.Difficulty
import mati.minesweeper.screens.NewGame.Size
import java.util.*
import kotlin.properties.Delegates

class Statistics() {
    // Win-Lose statistics
    class WinLose() {
        var win: Boolean by Delegates.notNull<Boolean>()
        var difficulty: Difficulty by Delegates.notNull<Difficulty>()
        var size: Size by Delegates.notNull<Size>()
        var time: Float by Delegates.notNull<Float>()

        constructor(win: Boolean, difficulty: Difficulty, size: Size, time: Float) : this() {
            this.win = win
            this.difficulty = difficulty
            this.size = size
            this.time = time
        }
    }

    var wins: MutableList<WinLose> = ArrayList()

    fun winPercentage(wins: List<WinLose> = this.wins): Float {
        var sum: Float = 0f
        for (e in wins) if (e.win) sum++
        return sum / wins.size.toFloat()
    }

    fun winStreak(wins: List<WinLose> = this.wins): Int {
        var streak: Int = 0
        for (i in (wins.size - 1).downTo(0)) {
            if (wins[i].win) streak++
            else break
        }
        return streak
    }

    fun filterWins(difficulty: Difficulty, size: Size): List<WinLose> {
        return wins.filter {
            it.difficulty == difficulty && it.size == size
        }
    }

    fun save() = Game.game.ioManager.save("stats", this, StatisticsSerializer())

    fun print() {
        if (Game.superDebug)
            Game.game.ioManager.print(this, StatisticsSerializer())
    }

    class StatisticsSerializer : Serializable<Statistics> {
        var wins: MutableList<WinLose> = ArrayList()

        override fun preserialize(t: Statistics) {
            wins = t.wins
        }

        override fun recover(): Statistics {
            val stats: Statistics = Statistics()
            stats.wins = wins
            return stats
        }
    }
}
