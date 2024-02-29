package org.hitlabnz.sensor_fusion_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import org.hitlabnz.sensorfusionlib.orientationProvider.ImprovedOrientationSensor1Provider;
import org.hitlabnz.sensorfusionlib.orientationProvider.OrientationProvider;

/**
 * The main activity
 * 
 * @author Ladislav Heller
 * 
 */
public class SensorSelectionActivity extends AndroidApplication {
    private OrientationProvider op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        op = new ImprovedOrientationSensor1Provider(sm);

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
        SensorChecker checker = new HardwareChecker(sm);
        if(!checker.IsGyroscopeAvailable()) {
        	// If a gyroscope is unavailable, display a warning.
        	displayHardwareMissingWarning();
        }

        Toast.makeText(this, "Volume Up/Down keys: Switch orientation provider!", Toast.LENGTH_LONG).show();
    }

    private void displayHardwareMissingWarning() {
    	AlertDialog ad = new AlertDialog.Builder(this).create();  
    	ad.setCancelable(false); // This blocks the 'BACK' button    
    	ad.setTitle(getResources().getString(R.string.gyroscope_missing)); 
    	ad.setMessage(getResources().getString(R.string.gyroscope_missing_message));
    	ad.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {  
    	    @Override  
    	    public void onClick(DialogInterface dialog, int which) {  
    	        dialog.dismiss();                      
    	    }  
    	});  
    	ad.show();  
	}

    public OrientationProvider getOrientationProvider() {
        return op;
    }

    public void setOrientationProvider(OrientationProvider newOrientationProvider) {
        op.stop();
        op = newOrientationProvider;
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensor_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.action_about:
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        op.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        op.stop();
    }
}
