package org.hitlabnz.sensor_fusion_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import org.hitlabnz.sensorfusionlib.orientationProvider.ImprovedOrientationSensor1Provider;
import org.hitlabnz.sensorfusionlib.orientationProvider.OrientationProvider;

public class SensorSelectionActivity extends AndroidApplication {
    private OrientationProvider orientationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        orientationProvider = new ImprovedOrientationSensor1Provider(sensorManager);

        // create our 3D compass instance
        // disable the use of Accelerometer/Compass/Gyroscope in LibGDX directly
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useGyroscope = false;
        config.useRotationVectorSensor = false;
        initialize(new Compass3D(this), config);

        // Check if device has a hardware gyroscope
        // boolean gyroAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        SensorChecker checker = new HardwareChecker(sensorManager);
        if(!checker.IsGyroscopeAvailable()) {
        	// If a gyroscope is unavailable, display a warning.
        	displayHardwareMissingWarning();
        }
    }

    private void displayHardwareMissingWarning() {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setCancelable(false); // This blocks the 'BACK' button
    	alertDialog.setTitle(getResources().getString(R.string.gyroscope_missing));
    	alertDialog.setMessage(getResources().getString(R.string.gyroscope_missing_message));
    	alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
    	    @Override  
    	    public void onClick(DialogInterface dialog, int which) {  
    	        dialog.dismiss();                      
    	    }  
    	});  
    	alertDialog.show();
	}

    public OrientationProvider getOrientationProvider() {
        return orientationProvider;
    }

    public void setOrientationProvider(OrientationProvider newOrientationProvider) {
        orientationProvider.stop();
        orientationProvider = newOrientationProvider;
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationProvider.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationProvider.stop();
    }
}
