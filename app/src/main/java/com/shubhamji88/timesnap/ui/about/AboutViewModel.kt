package com.shubhamji88.timesnap.ui.about

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
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
    fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}