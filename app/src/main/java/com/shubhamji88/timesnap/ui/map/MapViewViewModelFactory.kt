package com.shubhamji88.timesnap.ui.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MapViewViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}