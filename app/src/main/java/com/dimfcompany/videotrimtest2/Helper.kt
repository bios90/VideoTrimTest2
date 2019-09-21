package com.dimfcompany.videotrimtest2

import android.media.MediaMetadataRetriever
import androidx.core.content.ContextCompat
import java.io.File


fun File?.isFileOk(): Boolean
{
    return this != null && this.exists() && this.length() > 5000
}

fun getColorMy(id: Int): Int
{
    return ContextCompat.getColor(AppClass.getApp(), id)
}

class Helper
{
    companion object
    {
        fun getVideoLength(file: File): Int
        {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(AppClass.getApp(), FileManager.uriFromFile(file))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val time_in_seconds = time.toInt() / 1000
            return time_in_seconds
        }
    }
}