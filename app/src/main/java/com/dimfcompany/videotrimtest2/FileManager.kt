package com.dimfcompany.videotrimtest2

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.dimfcompany.alisa2auditor.logic.utils.StringManager
import java.io.*

class FileManager
{
    companion object
    {
        val TAG: String = "FileManager"

        fun rename(from: File, newName: String, extansion: String?): Boolean
        {
            var newName = newName
            try
            {
                val dir = from.parentFile
                if (extansion != null)
                {
                    newName = "$newName.$extansion"
                }

                val destinaition = File(dir, newName)
                destinaition.createNewFile()


                return from.parentFile.exists() && from.exists() && from.renameTo(destinaition)

            }
            catch (e: Exception)
            {
                e.printStackTrace()
                return false
            }
        }

        fun createRandomFile(extansion: String): File
        {
            return createFile(StringManager.randomString(), extansion, Constants.FOLDER_TEMP_FILES)
        }

        fun createRandomFile(extansion: String, folder: String): File
        {
            return createFile(StringManager.randomString(), extansion, folder)
        }

        fun createFile(name: String, extansion: String?): File
        {
            return createFile(name, extansion, Constants.FOLDER_TEMP_FILES)
        }

        fun createFile(name: String, extansion: String?, folder: String): File
        {
            var file: File

            val folder_file = File(getRootFolder() + "/" + folder)
            if (!folder_file.exists())
            {
                folder_file.mkdirs()
            }

            var file_name = name
            if (extansion != null)
            {
                file_name = file_name + "." + extansion
            }

            file = File(folder_file, file_name)
            if (file.exists())
            {
                file.delete()
            }

            file.createNewFile()

            return file
        }

        fun getRootFolder(): String
        {
            return AppClass.getApp().getExternalFilesDir(null).toString()
        }

        fun getFileExtension(fileName: String): String?
        {
            val i = fileName.lastIndexOf('.')
            return if (i > 0)
            {
                fileName.substring(i + 1)
            }
            else null
        }

        fun copy(src: File, dst: File)
        {
            val ins: InputStream = FileInputStream(src)
            try
            {
                val out: OutputStream = FileOutputStream(dst)
                try
                {
                    copy(ins, out)
                }
                finally
                {
                    out.close()
                }
            }
            finally
            {
                ins.close()
            }
        }

        fun copy(input: InputStream, output: OutputStream)
        {
            try
            {
                val buf: ByteArray = ByteArray(1024)
                var len: Int = input.read(buf)

                while (len > 0)
                {
                    output.write(buf, 0, len)
                    len = input.read(buf)
                }
            }
            catch (e: java.lang.Exception)
            {
            }
            finally
            {
                if (input != null)
                {
                    try
                    {
                        input.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }

                }

                if (output != null)
                {
                    try
                    {
                        output.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }
                }
            }
        }

        fun saveBitmapToFile(bitmap: Bitmap): File?
        {
            try
            {
                val file = Companion.createRandomFile(Constants.EXTENSION_PNG)
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

                return file
            }
            catch (e: IOException)
            {
                Log.e(TAG, "saveBitmapToFile: Error on saving bitmap to file")
                return null
            }
        }

        fun getFileFromTemp(name: String, extension: String?): File?
        {
            return getFile(name, Constants.FOLDER_TEMP_FILES, extension)
        }

        fun getFile(name: String, extension: String?): File?
        {
            return getFileFromTemp(name, extension)
        }

        fun getFile(name: String, folder: String, extension: String?): File?
        {
            var file: File
            val file_folder = File(getRootFolder() + "/" + folder)
            var file_name = name
            if (extension != null)
            {
                file_name = file_name + "." + extension
            }

            file = File(file_folder, file_name)
            return file
        }

        fun uriFromFile(file: File): Uri
        {
            var uri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(AppClass.getApp(), AppClass.getApp().packageName + ".provider", file)
            }
            else
            {
                uri = Uri.fromFile(file)
            }

            if (uri == null)
            {
                throw RuntimeException("Error could not create uri!")
            }

            return uri
        }

        fun getExtension(file: File): String?
        {
            return getExtension(uriFromFile(file))
        }

        fun getExtension(uri: Uri): String?
        {
            val extension: String?

            if (uri.scheme == ContentResolver.SCHEME_CONTENT)
            {
                val mime = MimeTypeMap.getSingleton()
                extension = mime.getExtensionFromMimeType(AppClass.getApp().getContentResolver().getType(uri))
            }
            else
            {
                extension =
                        MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())

            }

            return extension
        }
    }
}