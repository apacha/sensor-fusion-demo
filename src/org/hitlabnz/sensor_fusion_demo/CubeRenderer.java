package org.hitlabnz.sensor_fusion_demo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.hitlabnz.sensor_fusion_demo.orientationProvider.OrientationProvider;
import org.hitlabnz.sensor_fusion_demo.representation.Quaternion;

import android.opengl.GLSurfaceView;

/**
 * Class that implements the rendering of a cube with the current rotation of the device that is provided by a
 * OrientationProvider
 * 
 * @author Alexander Pacha
 * 
 */
public class CubeRenderer implements GLSurfaceView.Renderer {
    /**
     * The colour-cube that is drawn repeatedly
     */
    private Cube mCube;

    /**
     * The current provider of the device orientation.
     */
    private OrientationProvider orientationProvider = null;

    /**
     * Initialises a new CubeRenderer
     */
    public CubeRenderer() {
        mCube = new Cube();
    }

    /**
     * Sets the orientationProvider of this renderer. Use this method to change which sensor fusion should be currently
     * used for rendering the cube. Simply exchange it with another orientationProvider and the cube will be rendered
     * with another approach.
     * 
     * @param orientationProvider The new orientation provider that delivers the current orientation of the device
     */
    public void setOrientationProvider(OrientationProvider orientationProvider) {
        this.orientationProvider = orientationProvider;
    }

    /**
     * Perform the actual rendering of the cube for each frame
     * 
     * @param gl The surface on which the cube should be rendered
     */
    public void onDrawFrame(GL10 gl) {
        // clear screen
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // set-up modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);

        if (orientationProvider != null) {
            // Get the rotation from the current orientationProvider
            //gl.glMultMatrixf(orientationProvider.getRotationMatrix().getMatrix(), 0);

            Quaternion q = orientationProvider.getQuaternion();
            gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI), q.getX(), q.getY(), q.getZ());
        }

        // draw our object
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mCube.draw(gl);
    }

    /**
     * Update view-port with the new surface
     * 
     * @param gl the surface
     * @param width new width
     * @param height new height
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // set view-port
        gl.glViewport(0, 0, width, height);
        // set projection matrix
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // dither is enabled by default, we don't need it
        gl.glDisable(GL10.GL_DITHER);
        // clear screen in black
        gl.glClearColor(0, 0, 0, 1);
    }
}
