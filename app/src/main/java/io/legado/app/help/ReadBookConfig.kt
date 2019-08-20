package io.legado.app.help

import android.graphics.drawable.Drawable

object ReadBookConfig {
    val configList = arrayListOf<Config>()
    var bg: Drawable? = null

    init {

    }

    data class Config(
        var bg: String = "#F3F3F3",
        var bgInt: Int = 0,
        var bgType: Int = 0,
        var darkStatusIcon: Boolean = true,
        var textColor: String = "#3E3D3B",
        var textSize: Int = 16,
        var letterSpacing: Int = 1,
        var lineSpacingExtra: Int = 15,
        var lineSpacingMultiplier: Int = 3
    )

}