package com.dimfcompany.videotrimtest2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class ActMain : AppCompatActivity()
{
    val RQ_PICK_VIDEO_GALLERY = 9000
    val RQ_PICK_VIDEO_CAMERA = 9001
    val RQ_TRIM_ACTIVITY = 9002
    lateinit var messagesManager: MessagesManager

    var currentVideoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messagesManager = MessagesManager(this)

        btn_gallery.setOnClickListener(
            {
                getGalleryVideo()
            })

        btn_camera.setOnClickListener(
            {
                get_video_from_camera()
            })
    }

    fun getGalleryVideo()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(intent, RQ_PICK_VIDEO_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == RQ_PICK_VIDEO_GALLERY)
            {
                val uri: Uri = data!!.data
                val video_file = get_file_from_video_uri(data)

                if (!video_file.isFileOk())
                {
                    Log.e("Asddfg", "Erro file is note ok: ")
                    return
                }

                val file_name = video_file!!.name
                picked_video_file(video_file, file_name)
            }

            if (requestCode == RQ_PICK_VIDEO_CAMERA)
            {
                val uri = data!!.data

                val video_file = get_file_from_video_uri(data)

                if (!video_file.isFileOk())
                {
                    Log.e("Asddfg", "Erro file is note ok: ")
                    return
                }

                val file_name = video_file!!.name
                picked_video_file(video_file, file_name)
            }
        }
    }

    fun get_file_from_video_uri(data: Intent): File?
    {
        var file: File? = null
        val uri: Uri = data.data
        val extension = FileManager.getExtension(uri)!!

        file = FileManager.createRandomFile(extension)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        FileManager.copy(inputStream, outputStream)
        return file
    }

    fun get_video_from_camera()
    {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, RQ_PICK_VIDEO_CAMERA)
    }

    fun picked_video_file(file: File, file_name: String)
    {
        if (Helper.getVideoLength(file) > 30)
        {
            messagesManager.show_too_long_dialog(
                {
                    val intent = Intent(this, ActTrim::class.java)
                    intent.putExtra("file_name", file_name)
                    startActivityForResult(intent, RQ_TRIM_ACTIVITY)
                })
            return
        }
    }


}



