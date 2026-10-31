package com.example.matrixwallpaper

import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class MatrixWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = MatrixEngine()

    private inner class MatrixEngine : Engine() {

        private val renderer = MatrixRenderer()
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false
        private var needsFullBg = true

        private val drawRunnable = object : Runnable {
            override fun run() {
                draw()
                if (visible) handler.postDelayed(this, 40L) // ~25 fps
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            loadTheme()
        }

        override fun onVisibilityChanged(vis: Boolean) {
            visible = vis
            if (vis) {
                loadTheme()
                needsFullBg = true
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            renderer.resize(width, height)
            needsFullBg = true
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            visible = false
            handler.removeCallbacks(drawRunnable)
            super.onSurfaceDestroyed(holder)
        }

        private fun loadTheme() {
            val id = ThemePrefs.load(this@MatrixWallpaperService)
            renderer.setTheme(MatrixThemes.byId(id))
        }

        private fun draw() {
            val holder = surfaceHolder ?: return
            var canvas: android.graphics.Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    if (needsFullBg) {
                        renderer.drawBackground(canvas)
                        needsFullBg = false
                    }
                    renderer.drawFrame(canvas)
                }
            } finally {
                if (canvas != null) {
                    try { holder.unlockCanvasAndPost(canvas) } catch (_: Exception) {}
                }
            }
        }
    }
}
