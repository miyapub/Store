package org.zhudou.api.store

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

/**
 * Created by morgan on 2017/8/22.
 */
class Tools {
    //设置图片
    fun setNetworkBitmap(img: ImageView, url: String) {
        var bitmap: Bitmap? = null
        val networkImg = Runnable {
            try {
                val conn = URL(url)
                var inputStream= conn.openConnection().getInputStream()
                bitmap= BitmapFactory.decodeStream(inputStream)
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            while(bitmap==null){
                continue
            }

            val activity = img.getContext() as Activity
            activity.runOnUiThread {
                img.setImageBitmap(bitmap)
            }
        }
        Thread(networkImg).start()
    }

    //读取字节流
    @Throws(IOException::class)
    fun readFromStream(`is`: InputStream): String {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        var flag=true
        while (flag){
            len = `is`.read(buffer)
            if(len!=-1){
                baos.write(buffer, 0, len)
            }else{
                flag=false
            }
        }
        `is`.close()
        val result = baos.toString()
        baos.close()
        return result
    }
}