package com.shubhamji88.timesnap.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.view.Gravity
import android.widget.LinearLayout
import com.shubhamji88.timesnap.R

class PopUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up)
        findViewById<LinearLayout>(R.id.linearlayout).setOnClickListener { finish() }
//        val width= resources.displayMetrics.widthPixels
//        val height= resources.displayMetrics.heightPixels*0.75
//        window.attributes.gravity= Gravity.CENTER
//        window.setGravity(Gravity.BOTTOM)
//        window.setLayout(width.toInt(),height.toInt())

    }
}