package com.shubhamji88.timesnap.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.databinding.TimePickDialogBinding

class YearPicker : DialogFragment() {
    private lateinit var mListener: OnClickListener
    private lateinit var binding:TimePickDialogBinding
    private var timeList:Array<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            timeList = arguments.getStringArray(KEY_DEFAULT_ARRAY)
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
        binding= DataBindingUtil.inflate(inflater,R.layout.time_pick_dialog,container,false)
        binding.numberPicker.minValue=0
        binding.numberPicker.maxValue=timeList?.size!!-1
        binding.numberPicker.displayedValues=timeList
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.go.setOnClickListener {
            mListener.onYearPick(timeList?.get(binding.numberPicker.value))
            dismiss()
        }
    }

    interface OnClickListener {
        fun onYearPick(year:String?)
    }

    companion object {
        private const val KEY_DEFAULT_ARRAY = "default_array"
        fun getInstance(timeArray: Array<String>?, listener: OnClickListener): YearPicker {
            val dialog = YearPicker()
            val bundle = Bundle()
            bundle.putStringArray(KEY_DEFAULT_ARRAY,timeArray)
            dialog.arguments = bundle
            dialog.mListener = listener
            return dialog
        }
    }
}