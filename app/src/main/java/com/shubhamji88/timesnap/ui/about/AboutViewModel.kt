package com.shubhamji88.timesnap.ui.about

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.data.getDatabase
import com.shubhamji88.timesnap.data.model.Item
import com.shubhamji88.timesnap.repo.AppRepository
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.render.ImageSource
import gov.nasa.worldwind.shape.Placemark
import kotlinx.coroutines.*

class AboutViewModel(application: Application) : AndroidViewModel(application)  {
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _currentItem=MutableLiveData<Item>()
    val currentItem:LiveData<Item>
        get()=_currentItem
    private val _currentItemBitmap=MutableLiveData<Bitmap?>()
    val currentItemBitmap:LiveData<Bitmap?>
        get()=_currentItemBitmap
    val repo=AppRepository(getDatabase(getApplication()).cacheDao)
    fun getItemByName(name: String){
        viewModelScope.launch(Dispatchers.IO) {
           _currentItem.postValue(repo.getItemDetails(name))
        }
    }
    fun getBitmap(url:String){
        viewModelScope.launch(Dispatchers.IO){
            _currentItemBitmap.postValue(Utils.bindImage(getApplication(),url))
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}