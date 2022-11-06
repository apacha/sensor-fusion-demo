package org.hitlabnz.sensor_fusion_demo.test;

import org.hitlabnz.sensor_fusion_demo.representation.Quaternion;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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

    @Test
    public void quaternion_performSlerp_expectCorrectValues() throws Exception {

        // Arrange
        Quaternion q1 = new Quaternion();
        Quaternion q2 = new Quaternion();
        Quaternion q3 = new Quaternion();
        q1.loadIdentityQuat();
        q2.setXYZW(0.5f, 0.5f, 0.5f, 1);
        q2.normalise();

        // Act
        q2.slerp(q1, q3, 0.5f);

        // Assert
        assertThat(q3.w(), is(not(equalTo(1.0f))));
        assertThat(q3.x(), is(not(equalTo(0.0f))));
        assertThat(q3.y(), is(not(equalTo(0.0f))));
        assertThat(q3.z(), is(not(equalTo(0.0f))));
    }

}

