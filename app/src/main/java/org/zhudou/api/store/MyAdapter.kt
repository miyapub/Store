package org.zhudou.api.store

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.item.view.*
import java.net.URL
import org.zhudou.api.store.R.id.url
import java.io.FileOutputStream
import java.net.HttpURLConnection
import org.zhudou.api.store.R.id.url
import android.R.attr.bitmap
import android.R.attr.bitmap
import android.R.attr.bitmap
import android.app.Activity












class MyAdapter(context: Context, items: ArrayList<Map<String, Any>>, resource: Int, from: Array<String>, to: IntArray) : SimpleAdapter(context, items, resource, from, to) {
    var resource=resource
    var context=context
    var to=to
    var items=items
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view=super.getView(position, convertView, parent)
        var icon_url=view.icon_url.text.toString()
        var url=view.url.text.toString()
        Tools().setNetworkBitmap(view.icon,icon_url)
        return view
    }
}
