package com.shubhamji88.timesnap.ui.about

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.databinding.AboutDialogBinding
import com.snapchat.kit.sdk.SnapCreative
import com.snapchat.kit.sdk.creative.api.SnapCreativeKitApi
import com.snapchat.kit.sdk.creative.media.SnapLensLaunchData
import com.snapchat.kit.sdk.creative.media.SnapMediaFactory
import com.snapchat.kit.sdk.creative.media.SnapSticker
import com.snapchat.kit.sdk.creative.models.SnapLensContent
import com.snapchat.kit.sdk.creative.models.SnapLiveCameraContent
import java.io.File
import java.io.FileOutputStream

class AboutFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: AboutDialogBinding
    private lateinit var snapCreativeKitApi: SnapCreativeKitApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater,R.layout.about_dialog,container,false)
        application=requireNotNull(this.activity).application
        val viewModelFactory = AboutViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[AboutViewModel::class.java]
         snapCreativeKitApi= SnapCreative.getApi(requireContext())
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
            if(viewModel.currentItem.value!=null)
                shareToSnapChat(generateSticker())
            else
                Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
        }
        binding.ar.setOnClickListener {
            useAR()
        }
    }

    private fun shareToSnapChat(sticker: Bitmap) {
        val snapMediaFactory: SnapMediaFactory = SnapCreative.getMediaFactory(requireContext())
        val snapContent=SnapLiveCameraContent()
        val stickerFile = File(context?.cacheDir, "${viewModel.currentItem.value?.name}")
        if(!stickerFile.exists())
        viewModel.saveImageToStream(sticker, FileOutputStream(stickerFile))

        snapContent.snapSticker=addSticker(snapMediaFactory,stickerFile)
        snapContent.captionText="Today i went "+viewModel.currentItem.value?.title+ " to see "+viewModel.currentItem.value?.name
        snapCreativeKitApi.send(snapContent)
    }
    private fun addSticker(snapMediaFactory: SnapMediaFactory, stickerFile:File): SnapSticker {
        val snapSticker = snapMediaFactory.getSnapStickerFromFile(stickerFile)
        snapSticker.setHeightDp(250F)
        snapSticker.setWidthDp(250F)
        snapSticker.setPosX(0.35f)
        snapSticker.setPosY(0.8f)
        snapSticker.setRotationDegreesClockwise(340.0f)
        return snapSticker
    }
    private fun useAR(){
        if(viewModel.currentItem.value!=null && !viewModel.currentItem.value?.lens_id.isNullOrBlank())
            snapCreativeKitApi.send(getLens())
        else
            Toast.makeText(context, "Lens not available!!", Toast.LENGTH_SHORT).show()
    }
    private fun getLens(): SnapLensContent {
        val launchData = SnapLensLaunchData.Builder()
            .build()
        val snapLensContent = SnapLensContent.createSnapLensContent(
            viewModel.currentItem.value?.lens_id!!,
            launchData
        )
        snapLensContent.attachmentUrl = "https://snapchat.com"
        return snapLensContent

    }

    private fun generateSticker(): Bitmap {
        binding.topText.alpha= 1.0F
        val bitmap=viewModel.getBitmapFromView(binding.stickerLayout)
        binding.topText.alpha=0.0F
        return bitmap
    }


}