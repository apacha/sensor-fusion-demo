package org.hitlabnz.sensor_fusion_demo.test;

import org.hitlabnz.sensor_fusion_demo.representation.Quaternion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class QuaternionTest {

    @Test
    public void quaternion_loadUnityQuaternion_expectCorrectValues() throws Exception {

        // Arrange
        Quaternion q = new Quaternion();

        // Act
        q.loadIdentityQuat();

        // Assert
        assertThat(q.w(), is(equalTo(1.0f)));
        assertThat(q.x(), is(equalTo(0.0f)));
        assertThat(q.y(), is(equalTo(0.0f)));
        assertThat(q.z(), is(equalTo(0.0f)));
    }
}

