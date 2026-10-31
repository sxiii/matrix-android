package com.example.matrixwallpaper

import android.graphics.Color

data class MatrixTheme(
    val id: String,
    val displayName: String,
    val background: Int,
    val headColor: Int,
    val bodyColor: Int,
    val trailColor: Int,
    val charSet: String
)

object MatrixThemes {

    private const val KATAKANA =
        "\u30A0\u30A1\u30A2\u30A3\u30A4\u30A5\u30A6\u30A7\u30A8\u30A9" +
        "\u30AA\u30AB\u30AC\u30AD\u30AE\u30AF\u30B0\u30B1\u30B2\u30B3" +
        "\u30B4\u30B5\u30B6\u30B7\u30B8\u30B9\u30BA\u30BB\u30BC\u30BD" +
        "\u30BE\u30BF\u30C0\u30C1\u30C2\u30C3\u30C4\u30C5\u30C6\u30C7"

    private const val DIGITS = "0123456789"
    private const val SYMBOLS = "@#\$%&*+=<>?/\\|{}[]"

    val classicGreen = MatrixTheme(
        id = "classic_green",
        displayName = "Classic Green",
        background = Color.BLACK,
        headColor = Color.WHITE,
        bodyColor = Color.rgb(0, 255, 70),
        trailColor = Color.rgb(0, 100, 0),
        charSet = KATAKANA + DIGITS
    )

    val blueIce = MatrixTheme(
        id = "blue_ice",
        displayName = "Blue Ice",
        background = Color.rgb(2, 4, 18),
        headColor = Color.rgb(200, 240, 255),
        bodyColor = Color.rgb(30, 144, 255),
        trailColor = Color.rgb(10, 50, 120),
        charSet = KATAKANA + DIGITS + SYMBOLS
    )

    val redFire = MatrixTheme(
        id = "red_fire",
        displayName = "Red Fire",
        background = Color.rgb(10, 0, 0),
        headColor = Color.rgb(255, 230, 150),
        bodyColor = Color.rgb(255, 50, 20),
        trailColor = Color.rgb(120, 10, 0),
        charSet = KATAKANA + DIGITS
    )

    val purpleCyber = MatrixTheme(
        id = "purple_cyber",
        displayName = "Purple Cyber",
        background = Color.rgb(5, 0, 15),
        headColor = Color.rgb(230, 200, 255),
        bodyColor = Color.rgb(180, 60, 255),
        trailColor = Color.rgb(70, 0, 130),
        charSet = DIGITS + SYMBOLS
    )

    val gold = MatrixTheme(
        id = "gold",
        displayName = "Gold",
        background = Color.rgb(8, 6, 0),
        headColor = Color.WHITE,
        bodyColor = Color.rgb(255, 215, 0),
        trailColor = Color.rgb(120, 90, 0),
        charSet = KATAKANA + DIGITS + SYMBOLS
    )

    val all = listOf(classicGreen, blueIce, redFire, purpleCyber, gold)

    fun byId(id: String): MatrixTheme = all.firstOrNull { it.id == id } ?: classicGreen
}
