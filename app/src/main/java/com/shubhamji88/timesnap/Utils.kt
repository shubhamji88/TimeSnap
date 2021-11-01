package com.shubhamji88.timesnap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class Utils {
    companion object{
        fun bindImage(context: Context, imgUrl: String): Bitmap? {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                return Glide.with(context)
                    .asBitmap()
                    .load(imgUri)
                    .error(R.drawable.error)
                    .into(100,100)
                    .get()
        }
    }
}