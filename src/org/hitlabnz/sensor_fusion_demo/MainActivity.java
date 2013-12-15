/* Copyright (C) 2007 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package org.hitlabnz.sensor_fusion_demo;

import org.hitlabnz.sensor_fusion_demo.orientationProvider.GravityCompassProvider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.OrientationProvider;

import android.app.Activity;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Main controller activity for demo-application. Processes user-input and delegates calls to orientation providers.
 */
public class MainActivity extends Activity {
    /**
     * The surface that will be drawn upon
     */
    private GLSurfaceView mGLSurfaceView;
    /**
     * The class that renders the cube
     */
    private CubeRenderer mRenderer;
    /**
     * The current orientation provider that delivers device orientation.
     */
    private OrientationProvider currentOrientationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the orientationProvider
        //currentOrientationProvider = new RotationVectorProvider((SensorManager) getSystemService(SENSOR_SERVICE));
        //currentOrientationProvider = new AccelerometerCompassProvider((SensorManager) getSystemService(SENSOR_SERVICE));
        currentOrientationProvider = new GravityCompassProvider((SensorManager) getSystemService(SENSOR_SERVICE));

        // Create our Preview view and set it as the content of our Activity
        mRenderer = new CubeRenderer();
        mRenderer.setOrientationProvider(currentOrientationProvider);
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(mRenderer);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        currentOrientationProvider.start();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        currentOrientationProvider.stop();
        mGLSurfaceView.onPause();
    }
}