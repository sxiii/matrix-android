package com.example.matrixwallpaper

import android.content.Context

object ThemePrefs {
    private const val PREFS = "matrix_prefs"
    private const val KEY_THEME = "selected_theme"

    fun save(context: Context, themeId: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME, themeId)
            .apply()
    }

    fun load(context: Context): String {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_THEME, MatrixThemes.classicGreen.id)
            ?: MatrixThemes.classicGreen.id
    }
}
