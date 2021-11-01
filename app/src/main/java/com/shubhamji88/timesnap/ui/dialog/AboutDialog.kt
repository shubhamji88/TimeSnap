package com.shubhamji88.timesnap.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.data.model.Item
//import com.shubhamji88.timesnap.data.Animal
import com.shubhamji88.timesnap.databinding.AboutDialogBinding
import kotlinx.coroutines.*

class AboutDialog : DialogFragment() {
    private lateinit var mListener: OnClickListener
    private lateinit var binding:AboutDialogBinding
    val job=Job()
    private lateinit var dialogScope: CoroutineScope
    private lateinit var currentItemDetails:Item
    private val currentItemBitmap=MutableLiveData<Bitmap?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            Log.i("Passsssss",arguments.getString(KEY_DEFAULT_DATA)+"d")
            currentItemDetails=Gson().fromJson(arguments.getString(KEY_DEFAULT_DATA),Item::class.java)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        if (window != null) {
            window.setGravity(Gravity.BOTTOM)
            isCancelable = true
            window.attributes.windowAnimations = R.style.DialogUpDownAnimation
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater,R.layout.about_dialog,container,false)
        dialogScope= CoroutineScope(Dispatchers.Main+job)
        binding.name.text=currentItemDetails.name
        binding.details.text=currentItemDetails.text
        dialogScope.launch(Dispatchers.IO) {
            currentItemBitmap.postValue(currentItemDetails.picUrl?.let { it1 ->
                Utils.bindImage(
                    requireContext(),
                    it1
                )
            })
        }
        currentItemBitmap.observe(viewLifecycleOwner,{
            binding.imageView.setImageBitmap(it)
        })

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.go.setOnClickListener {
//            mListener.onYearPick(binding.numberPicker.value)
//        }
    }

    interface OnClickListener {
        fun onShareSticker()
        fun onAR()
    }

    companion object {
        private const val KEY_DEFAULT_DATA = "default_data"
        fun getInstance(listener: OnClickListener, itemData:Item): AboutDialog {
            val dialog = AboutDialog()
            val bundle = Bundle()

            bundle.putString(Gson().toJson(itemData), KEY_DEFAULT_DATA)
            dialog.arguments = bundle
            dialog.mListener = listener
            return dialog
        }
    }
}