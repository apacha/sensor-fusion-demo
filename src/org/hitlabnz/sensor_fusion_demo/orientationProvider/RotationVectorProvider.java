package org.hitlabnz.sensor_fusion_demo.orientationProvider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * The orientation provider that delivers the current orientation from the {@link Sensor#TYPE_ROTATION_VECTOR Android
 * Rotation Vector sensor}.
 * 
 * @author Alexander Pacha
 * 
 */
public class RotationVectorProvider implements SensorEventListener, OrientationProvider {

    /**
     * The rotation vector sensor that is being used for this provider to get device orientation
     */
    private Sensor rotationVectorSensor;

    /**
     * The matrix that holds the current rotation
     */
    private final float[] currentOrientationRotationMatrix = new float[16];

    /**
     * The sensor manager for accessing android sensors
     */
    private SensorManager sensorManager;

    /**
     * Initialises a new RotationVectorProvider
     * 
     * @param sensorManager The android sensor manager
     */
    public RotationVectorProvider(SensorManager sensorManager) {
        this.sensorManager = sensorManager;

        // find the rotation-vector sensor
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // initialize the rotation matrix to identity
        currentOrientationRotationMatrix[0] = 1;
        currentOrientationRotationMatrix[4] = 1;
        currentOrientationRotationMatrix[8] = 1;
        currentOrientationRotationMatrix[12] = 1;
    }

    @Override
    public void start() {
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        sensorManager.registerListener(this, rotationVectorSensor, 10000);
    }

    @Override
    public void stop() {
        // make sure to turn our sensor off when the activity is paused
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
            SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix, event.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public float[] getRotationMatrix() {
        return currentOrientationRotationMatrix;
    }
}
