package com.shubhamji88.timesnap.ui.about

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.worldwindtest.CameraController
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import com.shubhamji88.timesnap.data.model.Item
import com.shubhamji88.timesnap.databinding.AboutDialogBinding
import com.shubhamji88.timesnap.ui.dialog.AboutDialog
import com.shubhamji88.timesnap.ui.dialog.YearPicker
import gov.nasa.worldwind.BasicWorldWindowController
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Location
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import gov.nasa.worldwind.layer.RenderableLayer
import gov.nasa.worldwind.shape.Placemark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AboutFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: AboutDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater,R.layout.about_dialog,container,false)
        application=requireNotNull(this.activity).application
        val viewModelFactory = AboutViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[AboutViewModel::class.java]
        val name=activity?.intent?.getStringExtra("name")
        if(name.isNullOrBlank())
            activity?.finish()
        viewModel.getItemByName(name!!)
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        viewModel.currentItem.observe(viewLifecycleOwner,{currentItemDetails->
            binding.name.text=currentItemDetails.name
            binding.details.text=currentItemDetails.text
            if(currentItemDetails.picUrl!=null)
                viewModel.getBitmap(currentItemDetails.picUrl)
        })
        viewModel.currentItemBitmap.observe(viewLifecycleOwner,{
            binding.imageView.setImageBitmap(it)
        })
        binding.sticker.setOnClickListener {
            generateSticker()
        }
    }

    private fun generateSticker() {
        binding.topText.alpha= 1.0F
        binding.imageView.setImageBitmap(viewModel.getBitmapFromView(binding.stickerLayout))
        binding.topText.alpha=0.0F
    }

}