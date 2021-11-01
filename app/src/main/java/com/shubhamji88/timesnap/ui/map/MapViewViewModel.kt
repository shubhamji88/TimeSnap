package com.shubhamji88.timesnap.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.data.getDatabase
import com.shubhamji88.timesnap.data.model.Item
import com.shubhamji88.timesnap.repo.AppRepository
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.render.ImageSource
import gov.nasa.worldwind.shape.Placemark
import kotlinx.coroutines.*

class MapViewViewModel(application: Application) : AndroidViewModel(application)  {
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _placemarks = MutableLiveData<Map<String, MutableList<Placemark>>?>()
    val placemarks: LiveData<Map<String, MutableList<Placemark>>?>
        get() = _placemarks
    val repo=AppRepository(getDatabase(getApplication()).cacheDao)
    val allItemData=repo.allItems
    init {
        cacheAllPlacemark()
    }
    private fun cacheAllPlacemark() {
        uiScope.launch(Dispatchers.IO) {
            repo.cacheAllItems()
        }
    }
    fun getKeysOfTimeTravel(): Array<String>? {
        val array= arrayListOf<String>()
        return _placemarks.value?.keys?.toTypedArray()
    }
    fun getAllPlacemark(data: List<Item>){
        uiScope.launch(Dispatchers.IO){
            val placemarkList= mutableMapOf<String,MutableList<Placemark>>()
            data.forEach { item ->
                if (placemarkList.containsKey(item.title))
                    placemarkList[item.title]!!.add(
                        createPlaceMark(
                            Position.fromDegrees(
                                item.latitude!!,
                                item.longitude!!,
                                0.0
                            ), item.name, item.picUrl!!
                        )
                    )
                else
                    placemarkList[item.title!!] = mutableListOf(
                        createPlaceMark(
                            Position.fromDegrees(
                                item.latitude!!,
                                item.longitude!!,
                                0.0
                            ), item.name, item.picUrl!!
                        )
                    )
            }
            _placemarks.postValue(placemarkList)
        }
    }
    private fun createPlaceMark(position: Position, name: String, url:String): Placemark {
        val placeMark = Placemark.createWithImage(
            position,
            ImageSource.fromBitmap(Utils.bindImage(getApplication(),url))
        )
        placeMark.attributes.setImageOffset(Offset.bottomCenter()).imageScale = 1.0
        placeMark.displayName = name
        return placeMark
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}