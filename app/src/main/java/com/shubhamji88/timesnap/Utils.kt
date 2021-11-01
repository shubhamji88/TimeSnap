package com.shubhamji88.timesnap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class Utils {
    companion object{
        fun bindImage(context: Context, imgUrl: String): Bitmap? {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                return Glide.with(context)
                    .asBitmap()
                    .load(imgUri)
                    .error(R.drawable.error)
                    .listener(object :RequestListener<Bitmap>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Error","Load failed"+imgUrl)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    })
                    .into(300,300)
                    .get()
        }
    }
}