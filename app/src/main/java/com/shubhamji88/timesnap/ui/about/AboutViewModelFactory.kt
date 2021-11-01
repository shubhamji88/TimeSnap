package com.shubhamji88.timesnap.ui.about

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AboutViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AboutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AboutViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}