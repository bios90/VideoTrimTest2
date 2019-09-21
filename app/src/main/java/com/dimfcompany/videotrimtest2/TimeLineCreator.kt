package com.dimfcompany.videotrimtest2

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import java.io.File
import kotlin.math.ceil

class TimeLineCreator(val trim_activity: ActTrim)
{
    fun create_line(video_file: File)
    {
        val la_for_time_line: LinearLayout = trim_activity.findViewById(R.id.la_for_timeline)
        val images_array: ArrayList<Bitmap> = ArrayList()
        val layoutInflater = LayoutInflater.from(trim_activity)

        la_for_time_line.post(
            {
                val width = la_for_time_line.measuredWidth
                val height = la_for_time_line.measuredHeight

                val images_count = (width.toFloat() / height.toFloat()).toInt()
                val each_image_width = width / images_count

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(AppClass.getApp(), FileManager.uriFromFile(video_file))
                val video_length = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
                val frame_difference = video_length / images_count.toLong()

                for (i in 0 until images_count)
                {
                    val frame_index = i * frame_difference * 1000
                    val bitmap = retriever.getFrameAtTime(frame_index, OPTION_CLOSEST_SYNC)
                    images_array.add(bitmap)
                }

                for (bitmap in images_array)
                {
                    val image_view = layoutInflater.inflate(R.layout.img_trim_single, la_for_time_line, false) as ImageView
                    val params = LinearLayout.LayoutParams(each_image_width, LinearLayout.LayoutParams.MATCH_PARENT)
                    image_view.layoutParams = params
                    image_view.setImageBitmap(bitmap)
                    la_for_time_line.addView(image_view)
                }
            })
    }
}