package org.zhudou.api.store

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    val list = ArrayList<Map<String, Any>>()
    var simpleAdapter: MyAdapter? =null
    fun upUI(){
        //list.clear()
        simpleAdapter?.notifyDataSetChanged()
    }
    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {  //这个是发送过来的消息
            val message = this.obtainMessage()
            val data = msg.data
            when (msg.what) {
                1 -> {
                    //post success!
                    val jsonString = data.get("json").toString()
                    //val jsonobj = JSONObject("{'name':'scott','age':18}")
                    var jsonArray = JSONArray(jsonString);
                    for (i in 0..jsonArray.length() - 1) {
                        val name = jsonArray.getJSONObject(i).getString("name")
                        val icon = jsonArray.getJSONObject(i).getString("icon")
                        val url = jsonArray.getJSONObject(i).getString("url")
                        val map = HashMap<String, Any>()
                        map.put("name", name);
                        map.put("icon", icon);
                        map.put("url", url);
                        list.add(map)
                    }
                    upUI()
                }
                else -> {

                }
            }
            super.handleMessage(msg)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleAdapter = MyAdapter(baseContext, list,
                R.layout.item, arrayOf("name","url","icon"), intArrayOf(R.id.name,R.id.url,R.id.icon_url))
        apps.adapter=simpleAdapter
        //
        Thread(Runnable {
            //
            val path = "http://api.zhudou.org/apps.php"
            var result = ""
            var url: URL? = null
            var conn: HttpURLConnection? = null
            try {
                url = URL(path)
                try {
                    conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)")
                    //区别3、必须指定两个请求的参数
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")//请求的类型  表单数据
                    val data = "from=douya"
                    conn.setRequestProperty("Content-Length", data.length.toString() + "")//数据的长度
                    conn.connectTimeout = 5000
                    conn.readTimeout = 5000
                    //区别4、记得设置把数据写给服务器
                    conn.doOutput = true//设置向服务器写数据
                    val bytes = data.toByteArray()
                    conn.outputStream.write(bytes)//把数据以流的方式写给服务器
                    val code = conn.responseCode
                    //System.out.println(code);
                    if (code == 200) {
                        var `is`: InputStream? = null
                        `is` = conn.inputStream
                        result = Tools().readFromStream(`is`)
                        val message = handler.obtainMessage()
                        message.what = 1
                        val msgData = Bundle()
                        msgData.putString("json", result)
                        message.data = msgData
                        message.sendToTarget()
                    } else {
                        result = "error"
                        val message = handler.obtainMessage()
                        message.what = 2
                        val msgData = Bundle()
                        msgData.putString("msg", result)
                        message.data = msgData
                        message.sendToTarget()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                val message = handler.obtainMessage()
                message.what = 2
                val msgData = Bundle()
                msgData.putString("msg", e.toString())
                message.data = msgData
                message.sendToTarget()
            }
        }).start()
        //
    }
}
