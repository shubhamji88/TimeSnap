package com.shubhamji88.timesnap.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.worldwindtest.CameraController
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import com.shubhamji88.timesnap.data.list
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import gov.nasa.worldwind.layer.RenderableLayer
import gov.nasa.worldwind.render.ImageSource
import gov.nasa.worldwind.shape.Placemark
import kotlinx.coroutines.*

class MapViewViewModel(application: Application) : AndroidViewModel(application)  {
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _placemarks = MutableLiveData<Map<String, MutableList<Placemark>>?>()
    val placemarks: LiveData<Map<String, MutableList<Placemark>>?>
        get() = _placemarks
    init {
        getAllPlacemark()
    }
    private fun getAllPlacemark(){
        uiScope.launch(Dispatchers.IO){
            val data= list
            val placemarkList= mutableMapOf<String,MutableList<Placemark>>()
            data.forEach { item ->
                if (placemarkList.containsKey(item.name))
                    placemarkList[item.name]!!.add(
                        createPlaceMark(
                            Position.fromDegrees(
                                item.latitude,
                                item.longitude,
                                0.0
                            ), item.name, item.picUrl
                        )
                    )
                else
                    placemarkList[item.name] = mutableListOf(
                        createPlaceMark(
                            Position.fromDegrees(
                                item.latitude,
                                item.longitude,
                                0.0
                            ), item.name, item.picUrl
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
        placeMark.attributes.setImageOffset(Offset.bottomCenter()).imageScale = 3.0
        placeMark.displayName = name
        return placeMark
    }
}