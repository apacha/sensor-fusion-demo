/**
 * 
 */
package org.hitlabnz.sensor_fusion_demo.orientationProvider;

import org.hitlabnz.sensor_fusion_demo.representation.Matrixf4x4;

/**
 * Classes implementing this interface provide an orientation of the device either by directly accessing hardware, using
 * Android sensor fusion or fusing sensors itself.
 * 
 * The orientation can be provided as rotation matrix or quaternion.
 * 
 * @author Alexander Pacha
 * 
 */
public interface OrientationProvider {

    /**
     * 
     * @return Returns the current rotation of the device in the rotation matrix format (4x4 matrix)
     */
    public Matrixf4x4 getRotationMatrix();

    /**
     * Starts the sensor fusion (e.g. when resuming the activity)
     */
    public void start();

    /**
     * Stops the sensor fusion (e.g. when pausing/suspending the activity)
     */
    public void stop();

}
