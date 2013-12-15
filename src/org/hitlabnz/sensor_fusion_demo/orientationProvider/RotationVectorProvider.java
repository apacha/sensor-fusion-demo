package org.hitlabnz.sensor_fusion_demo.orientationProvider;

import org.hitlabnz.sensor_fusion_demo.representation.Matrixf4x4;
import org.hitlabnz.sensor_fusion_demo.representation.Quaternion;

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
    private final Matrixf4x4 currentOrientationRotationMatrix;

    /**
     * The quaternion that holds the current rotation
     */
    private final Quaternion currentOrientationQuaternion;

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

        // Initialise with identity
        currentOrientationRotationMatrix = new Matrixf4x4();

        // Initialise with identity
        currentOrientationQuaternion = new Quaternion();
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
            SensorManager.getRotationMatrixFromVector(currentOrientationRotationMatrix.matrix, event.values);

            // Get Quaternion
            float[] q = new float[4];
            // Calculate angle. Starting with API_18, Android will provide this value as event.values[3], but if not, we have to calculate it manually.
            SensorManager.getQuaternionFromVector(q, event.values);
            currentOrientationQuaternion.setXYZW(q[1], q[2], q[3], -q[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not doing anything
    }

    @Override
    public Matrixf4x4 getRotationMatrix() {
        return currentOrientationRotationMatrix;
    }

    @Override
    public Quaternion getQuaternion() {
        return currentOrientationQuaternion;
    }
}
