package com.example.worldwindtest

import gov.nasa.worldwind.BasicWorldWindowController
import android.view.Choreographer.FrameCallback
import gov.nasa.worldwind.gesture.GestureRecognizer
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.gesture.PinchRecognizer
import gov.nasa.worldwind.gesture.RotationRecognizer
import gov.nasa.worldwind.util.WWMath
import android.view.Choreographer
import com.shubhamji88.timesnap.atmosphere.AtmosphereLayer
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Location

internal class CameraController(
    private var atmosphereLayer: AtmosphereLayer,
    sunLocation: Location
) : BasicWorldWindowController(), FrameCallback {
    private var camera = Camera()
    private var beginCamera = Camera()

    // Animation settings
    private var cameraDegreesPerSecond = 2.0
    private var lightDegreesPerSecond = 6.0
    private var lastFrameTimeNanos: Long = 0
    private var activityPaused = false
    private var sunLocation = Location(0.0, -100.0)
    override fun handlePan(recognizer: GestureRecognizer) {
        val state = recognizer.state
        val dx = recognizer.translationX
        val dy = recognizer.translationY
        if (state == WorldWind.BEGAN) {
            gestureDidBegin()
            lastX = 0f
            lastY = 0f
        } else if (state == WorldWind.CHANGED) {
            // Get the navigator's current position.
            var lat = camera.latitude
            var lon = camera.longitude
            val alt = camera.altitude

            // Convert the translation from screen coordinates to degrees. Use the navigator's range as a metric for
            // converting screen pixels to meters, and use the globe's radius for converting from meters to arc degrees.
            val metersPerPixel = wwd.pixelSizeAtDistance(alt)
            val forwardMeters = (dy - lastY) * metersPerPixel
            val sideMeters = -(dx - lastX) * metersPerPixel
            lastX = dx
            lastY = dy
            val globeRadius = wwd.globe.getRadiusAt(lat, lon)
            val forwardDegrees = Math.toDegrees(forwardMeters / globeRadius)
            val sideDegrees = Math.toDegrees(sideMeters / globeRadius)

            // Adjust the change in latitude and longitude based on the navigator's heading.
            val heading = camera.heading
            val headingRadians = Math.toRadians(heading)
            val sinHeading = Math.sin(headingRadians)
            val cosHeading = Math.cos(headingRadians)
            lat += forwardDegrees * cosHeading - sideDegrees * sinHeading
            lon += forwardDegrees * sinHeading + sideDegrees * cosHeading

            // If the navigator has panned over either pole, compensate by adjusting the longitude and heading to move
            // the navigator to the appropriate spot on the other side of the pole.
            if (lat < -90 || lat > 90) {
                camera.latitude = Location.normalizeLatitude(lat)
                camera.longitude = Location.normalizeLongitude(lon + 180)
            } else if (lon < -180 || lon > 180) {
                camera.latitude = lat
                camera.longitude = Location.normalizeLongitude(lon)
            } else {
                camera.latitude = lat
                camera.longitude = lon
            }
            //this.camera.heading = WWMath.normalizeAngle360(heading + sideDegrees * 1000);
            wwd.navigator.setAsCamera(wwd.globe, camera)
            wwd.requestRedraw()
        } else if (state == WorldWind.ENDED || state == WorldWind.CANCELLED) {
            gestureDidEnd()
        }
    }

    override fun handlePinch(recognizer: GestureRecognizer) {
        val state = recognizer.state
        var scale = (recognizer as PinchRecognizer).scale
        if (state == WorldWind.BEGAN) {
            gestureDidBegin()
        } else if (state == WorldWind.CHANGED) {
            if (scale != 0f) {
                // Apply the change in scale to the navigator, relative to when the gesture began.
                scale = (scale - 1) * 0.1f + 1 // dampen the scale factor
                camera.altitude = camera.altitude * scale
                this.applyLimits(camera)
                wwd.navigator.setAsCamera(wwd.globe, camera)
                wwd.requestRedraw()
            }
        } else if (state == WorldWind.ENDED || state == WorldWind.CANCELLED) {
            gestureDidEnd()
        }
    }

    override fun handleRotate(recognizer: GestureRecognizer) {
        val state = recognizer.state
        val rotation = (recognizer as RotationRecognizer).rotation
        if (state == WorldWind.BEGAN) {
            gestureDidBegin()
            lastRotation = 0f
        } else if (state == WorldWind.CHANGED) {

            // Apply the change in rotation to the navigator, relative to the navigator's current values.
            val headingDegrees = (lastRotation - rotation).toDouble()
            camera.heading = WWMath.normalizeAngle360(camera.heading + headingDegrees)
            lastRotation = rotation
            wwd.navigator.setAsCamera(wwd.globe, camera)
            wwd.requestRedraw()
        } else if (state == WorldWind.ENDED || state == WorldWind.CANCELLED) {
            gestureDidEnd()
        }
    }

    override fun handleTilt(recognizer: GestureRecognizer) {
        val state = recognizer.state
        val dx = recognizer.translationX
        val dy = recognizer.translationY
        if (state == WorldWind.BEGAN) {
            gestureDidBegin()
            lastRotation = 0f
        } else if (state == WorldWind.CHANGED) {
            // Apply the change in tilt to the navigator, relative to when the gesture began.
            val headingDegrees = (180 * dx / wwd.width).toDouble()
            val tiltDegrees = (-180 * dy / wwd.height).toDouble()
            camera.heading = WWMath.normalizeAngle360(beginCamera.heading + headingDegrees)
            camera.tilt = beginCamera.tilt + tiltDegrees
            this.applyLimits(camera)
            wwd.navigator.setAsCamera(wwd.globe, camera)
            wwd.requestRedraw()
        } else if (state == WorldWind.ENDED || state == WorldWind.CANCELLED) {
            gestureDidEnd()
        }
    }

    override fun gestureDidBegin() {
        if (activeGestures++ == 0) {
            wwd.navigator.getAsCamera(wwd.globe, beginCamera)
            camera.set(beginCamera)
        }
    }

    private fun applyLimits(camera: Camera) {
        val distanceToExtents = wwd.distanceToViewGlobeExtents()
        val minAltitude = 100.0
        camera.altitude = WWMath.clamp(camera.altitude, minAltitude, distanceToExtents)

        // Limit the tilt to between nadir and the horizon (roughly)
        val r = wwd.globe.getRadiusAt(camera.latitude, camera.latitude)
        val maxTilt = Math.toDegrees(Math.asin(r / (r + camera.altitude)))
        val minTilt = 0.0
        camera.tilt = WWMath.clamp(camera.tilt, minTilt, maxTilt)
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTimeNanos != 0L) {
            // Compute the frame duration in seconds.
            val frameDurationSeconds = (frameTimeNanos - lastFrameTimeNanos) * 1.0e-9
            val cameraDegrees = frameDurationSeconds * cameraDegreesPerSecond
            val lightDegrees = frameDurationSeconds * lightDegreesPerSecond

            // Move the camera to simulate the Earth's rotation about its axis.
            val camera = camera
            camera.longitude -= cameraDegrees

            // Move the sun location to simulate the Sun's rotation about the Earth.
            sunLocation[sunLocation.latitude] = sunLocation.longitude - lightDegrees
            atmosphereLayer.lightLocation = sunLocation

            // Redraw the WorldWindow to display the above changes.
            this.worldWindow.requestRedraw()
        }
        if (!activityPaused) { // stop animating when this Activity is paused
            Choreographer.getInstance().postFrameCallback(this)
        }
        lastFrameTimeNanos = frameTimeNanos
    }

    init {
        this.sunLocation = sunLocation
    }
}