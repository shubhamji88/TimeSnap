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
import com.shubhamji88.timesnap.Utils
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import com.shubhamji88.timesnap.data.Animal
import com.shubhamji88.timesnap.databinding.MapViewFragmentBinding
import com.shubhamji88.timesnap.ui.dialog.YearPicker
import gov.nasa.worldwind.BasicWorldWindowController
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Location
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import gov.nasa.worldwind.layer.RenderableLayer
import gov.nasa.worldwind.render.ImageSource
import gov.nasa.worldwind.shape.Placemark

class MapViewFragment : Fragment() , YearPicker.OnClickListener {
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
        putPlaceMarks("INDOMINUS REX")
        handleButton()
        return binding.root
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
        wwd.worldWindowController= PickNavigateController(application)
        viewModel.placemarks.observe(viewLifecycleOwner,{map->
            Log.d("placemark",map.toString())
            map?.let {
                if(it.containsKey(timeAgo))
                    layer.addAllRenderables(map[timeAgo])
            }
        })
//        layer.addRenderable(
//            createPlaceMark(
//                Position.fromDegrees(34.2000, -119.2070, 0.0),
//                "Sample Name",
//                "v"
//            )
//        )

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


    class PickNavigateController(context: Context) :
        BasicWorldWindowController() {
        private var pickedObject // last picked object from onDown events
                : Any? = null
        private var pickGestureDetector = GestureDetector(
            context.applicationContext, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(event: MotionEvent): Boolean {
                    pick(event) // Pick the object(s) at the tap location
                    Log.d("touch",event.x.toString()+" "+event.y.toString())
//                Toast.makeText(context, "Touch at :${event.x},${event.y}", Toast.LENGTH_SHORT).show()
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
                val obj = topPickedObject.userObject as Placemark
//                Log.i("touch",obj.displayName)
//            Toast.makeText(, `object`.displayName, Toast.LENGTH_SHORT).show()
            }
        }
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
}