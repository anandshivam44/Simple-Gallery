package com.simplemobiletools.gallery.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.REFRESH_PATH
import com.simplemobiletools.gallery.extensions.galleryDB
import com.simplemobiletools.gallery.helpers.TYPE_GIFS
import com.simplemobiletools.gallery.helpers.TYPE_IMAGES
import com.simplemobiletools.gallery.helpers.TYPE_RAWS
import com.simplemobiletools.gallery.helpers.TYPE_VIDEOS
import com.simplemobiletools.gallery.models.Medium
import java.io.File

class RefreshMediaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val path = intent.getStringExtra(REFRESH_PATH) ?: return

        Thread {
            val medium = Medium(null, path.getFilenameFromPath(), path, path.getParentPath(), System.currentTimeMillis(), System.currentTimeMillis(),
                    File(path).length(), getFileType(path), false)
            context.galleryDB.MediumDao().insert(medium)
        }.start()
    }

    private fun getFileType(path: String) = when {
        path.isImageFast() -> TYPE_IMAGES
        path.isVideoFast() -> TYPE_VIDEOS
        path.isGif() -> TYPE_GIFS
        else -> TYPE_RAWS
    }
}
