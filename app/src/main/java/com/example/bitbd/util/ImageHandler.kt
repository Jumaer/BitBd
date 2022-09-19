package com.example.bitbd.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.bitbd.constant.MyApplication
import okhttp3.MultipartBody
import java.io.*
import java.util.*

object ImageHandler {

    // Method to save an bitmap to a file
     fun bitmapToFile(bitmap: Bitmap , fileReturn : FileReturn): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(MyApplication.appContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val fileOfMultiPart = saveBitmapToFile(file,fileReturn)
        Log.d("file size 2 :: " , fileOfMultiPart?.length().toString())

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri(ac: Activity, uri: Uri?): Bitmap? {
        var input = ac.contentResolver.openInputStream(uri!!)
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Call some material design APIs here
        } else {
            @Suppress("DEPRECATION")
            onlyBoundsOptions.inDither = true //optional
        }
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1) return null
        //Image resolution is based on 480x800
        val hh = 800f //The height is set as 800f here
        val ww = 480f //Set the width here to 480f
        //Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
        var be = 1 //be=1 means no scaling
        if (originalWidth > originalHeight && originalWidth > ww) { //If the width is large, scale according to the fixed size of the width
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) { //If the height is high, scale according to the fixed size of the width
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        //Proportional compression
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be //Set scaling

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Call some material design APIs here
        } else {
            @Suppress("DEPRECATION")
            bitmapOptions.inDither = true //optional
        }
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        input = ac.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return bitmap
    }

    fun saveBitmapToFile(file: File, fileReturn : FileReturn): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 50

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            fileReturn.getFileForMultipart(file)
            file
        } catch (e: Exception) {
            null
        }
    }


    interface FileReturn{
        fun getFileForMultipart(file : File)
    }


}