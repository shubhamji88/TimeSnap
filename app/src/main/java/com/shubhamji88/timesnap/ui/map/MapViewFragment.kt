package com.shubhamji88.timesnap.ui.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.worldwindtest.CameraController
import com.shubhamji88.timesnap.R
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import com.shubhamji88.timesnap.databinding.MapViewFragmentBinding
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

class MapViewFragment : Fragment() {
    private lateinit var wwd: WorldWindow
    private var atmosphereLayer: AtmosphereLayer? = null
    private var sunLocation = Location(0.0, -100.0)
    private lateinit var application: Application
    private lateinit var viewModel: MapViewViewModel
    private lateinit var binding: MapViewFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.map_view_fragment,container,false)
        application=requireNotNull(this.activity).application
        val viewModelFactory = MapViewViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewViewModel::class.java]
        binding.globe.addView(createWorldWindow())
        putPlaceMarks()
        return binding.root
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
    private fun putPlaceMarks(){
        val layer = RenderableLayer("Placemarks")
        wwd.layers.addLayer(layer)
        wwd.worldWindowController= PickNavigateController(application)
        layer.addRenderable(
            createAirportPlacemark(
                Position.fromDegrees(34.2000, -119.2070, 0.0),
                "Sample Name"
            )
        )
    }
    private fun createCamera(): Camera {
        // Create a camera position above KOXR airport, Oxnard, CA
        val camera = Camera()
        camera.set(
            34.2, -119.2,
            10000.0, WorldWind.ABSOLUTE,
            90.0,  // Looking east
            70.0,  // Lookup up from nadir
            0.0
        ) // No roll
        return camera
    }
    override fun onResume() {
        super.onResume()
        wwd.onResume() // resumes a paused rendering thread
    }

    private fun createAirportPlacemark(position: Position?, airportName: String?): Placemark? {
        val placeMark = Placemark.createWithImage(
            position,
            ImageSource.fromResource(R.drawable.aircraft_fighter)
        )

        placeMark.attributes.setImageOffset(Offset.bottomCenter()).imageScale = 3.0
        placeMark.displayName = airportName
        return placeMark
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

        /**
         * Delegates events to the pick handler or the native WorldWind navigation handlers.
         */
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            super.onTouchEvent(event)
            pickGestureDetector.onTouchEvent(event)
            return true
        }

        /**
         * Performs a pick at the tap location.
         */
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

    override fun onPause() {
        super.onPause()
        wwd.onPause() // pauses the rendering thread
    }

}