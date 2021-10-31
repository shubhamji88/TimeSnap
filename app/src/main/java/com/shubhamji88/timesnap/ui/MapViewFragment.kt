package com.shubhamji88.timesnap.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shubhamji88.timesnap.R

class MapViewFragment : Fragment() {

    companion object {
        fun newInstance() = MapViewFragment()
    }

    private lateinit var viewModel: MapViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}