package com.shubhamji88.timesnap.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.data.Animal
import com.shubhamji88.timesnap.databinding.AboutDialogBinding
import com.shubhamji88.timesnap.databinding.TimePickDialogBinding
import com.shubhamji88.timesnap.repo.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AboutDialog : DialogFragment() {
    private lateinit var mListener: OnClickListener
    private lateinit var binding:AboutDialogBinding
    private var itemName:String?=null
    val job=Job()
    private lateinit var dialogScope: CoroutineScope
    val repo=AppRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            itemName = arguments.getString(KEY_DEFAULT_NAME)
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

        val itemDetails=getItemDetails()
//        binding.name.text=itemDetails.name
//        binding.details.text=itemDetails.text
//        binding.imageView.setImageBitmap(Utils.bindImage(requireContext(),itemDetails.picUrl))
//        binding.numberPicker.minValue=0
//        binding.numberPicker.maxValue=timeList?.size!!-1
//        binding.numberPicker.displayedValues=timeList
        return binding.root
    }
    private fun getItemDetails(): Animal? {
        var details:Animal?=null
        dialogScope.launch(Dispatchers.IO) {
           details= itemName?.let { repo.getItemDetails(it) }
        }
        Log.d("Item",details.toString()+" "+itemName)
       return details
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
        private const val KEY_DEFAULT_NAME = "default_name"
        fun getInstance(name: String, listener: OnClickListener): AboutDialog {
            val dialog = AboutDialog()
            val bundle = Bundle()
            bundle.putString(KEY_DEFAULT_NAME,name)
            dialog.arguments = bundle
            dialog.mListener = listener
            return dialog
        }
    }
}