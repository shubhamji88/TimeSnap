package com.shubhamji88.timesnap.ui.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.worldwindtest.CameraController
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import com.shubhamji88.timesnap.databinding.MapViewFragmentBinding
import com.shubhamji88.timesnap.ui.dialog.AboutDialog
import com.shubhamji88.timesnap.ui.dialog.YearPicker
import gov.nasa.worldwind.BasicWorldWindowController
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Location
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import gov.nasa.worldwind.layer.RenderableLayer
import gov.nasa.worldwind.shape.Placemark

class MapViewFragment : Fragment() , YearPicker.OnClickListener,AboutDialog.OnClickListener,PickNavigateController.OnClickListener{
    private lateinit var wwd: WorldWindow
    private var atmosphereLayer: AtmosphereLayer? = null
    private var sunLocation = Location(0.0, -100.0)
    private lateinit var application: Application
    private lateinit var viewModel: MapViewViewModel
    private lateinit var binding: MapViewFragmentBinding
    val array=arrayOf("100 year ago","200 million year ago","1000 year ago","1000 million year ago")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.map_view_fragment,container,false)
        application=requireNotNull(this.activity).application
        val viewModelFactory = MapViewViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewViewModel::class.java]
        binding.globe.addView(createWorldWindow())
        putPlaceMarks("70-100 MILLION YEARS BEFORE")
        handleButton()
        handleData()
        return binding.root
    }

    private fun handleData() {
        viewModel.allItemData.observe(viewLifecycleOwner,{
            viewModel.getAllPlacemark(it)
        })
    }

    private fun handleButton() {
        val yearPicker=YearPicker.getInstance(array,this)
        binding.travelButton.setOnClickListener {
            val supportFragmentManager = activity?.supportFragmentManager!!
            yearPicker.show(supportFragmentManager,"year")
        }
    }

    private fun createWorldWindow(): WorldWindow {
        wwd = WorldWindow(application)

        val globe = wwd.globe
        wwd.navigator.setAsCamera(globe, createCamera())
        atmosphereLayer= AtmosphereLayer()
        atmosphereLayer!!.lightLocation=sunLocation
        wwd.worldWindowController = CameraController(atmosphereLayer!!,sunLocation)
        wwd.layers.addLayer(BackgroundLayer())
        wwd.layers.addLayer(BlueMarbleLandsatLayer())
        wwd.layers.addLayer(atmosphereLayer)

        return wwd
    }
    private fun putPlaceMarks(timeAgo:String){
        val layer = RenderableLayer("Placemarks")
        wwd.layers.addLayer(layer)
        wwd.worldWindowController= PickNavigateController(application,this)
        viewModel.placemarks.observe(viewLifecycleOwner,{map->
            Log.d("placemark",map.toString())
            map?.let {
                if(it.containsKey(timeAgo))
                    layer.addAllRenderables(map[timeAgo])
            }
        })

    }
    private fun createCamera(): Camera {
        val camera = Camera()
        camera.set(
            34.2, -119.2,
            10000.0, WorldWind.ABSOLUTE,
            90.0,
            70.0,
            0.0
        )
        return camera
    }



    fun openAboutDialog(name:String){

    }

    override fun onResume() {
        super.onResume()
        wwd.onResume() // resumes a paused rendering thread
    }

    override fun onPause() {
        super.onPause()
        wwd.onPause() // pauses the rendering thread
    }

    override fun onYearPick(yearIndex: Int?) {
        Toast.makeText(context, "${array[yearIndex!!]}", Toast.LENGTH_SHORT).show()
    }

    override fun onShareSticker() {
        TODO("Not yet implemented")
    }

    override fun onAR() {
        TODO("Not yet implemented")
    }

    override fun openItemDetails(name: String) {
        val aboutDialog=AboutDialog.getInstance(name,this)
        aboutDialog.show(activity?.supportFragmentManager!!,"About")
    }
}
class PickNavigateController(context: Context,listner:PickNavigateController.OnClickListener) :
    BasicWorldWindowController() {
    private val mListener: PickNavigateController.OnClickListener=listner
    interface OnClickListener {
        fun openItemDetails(name: String)
    }
    private var pickedObject // last picked object from onDown events
            : Any? = null
    private var pickGestureDetector = GestureDetector(
        context.applicationContext, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
                pick(event)
                return false // By not consuming this event, we allow it to pass on to the navigation gesture handlers
            }

        })
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        pickGestureDetector.onTouchEvent(event)
        return true
    }
    fun pick(event: MotionEvent) {
        pickedObject = null
        val pickList = worldWindow.pick(event.x, event.y)

        val topPickedObject = pickList.topPickedObject()
        if (topPickedObject != null) {
            pickedObject = topPickedObject.userObject
            if(pickedObject is Placemark) {
                val obj = topPickedObject.userObject as Placemark
                mListener.openItemDetails(obj.displayName)
                Log.i("touch",obj.displayName)
            }
        }
    }
}