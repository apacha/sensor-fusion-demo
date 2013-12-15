package org.hitlabnz.sensor_fusion_demo;

import java.util.Locale;

import org.hitlabnz.sensor_fusion_demo.orientationProvider.AccelerometerCompassProvider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.CalibratedGyroscopeProvider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.GravityCompassProvider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.ImprovedOrientationSensor1Provider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.ImprovedOrientationSensor2Provider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.OrientationProvider;
import org.hitlabnz.sensor_fusion_demo.orientationProvider.RotationVectorProvider;

import android.content.Intent;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

/**
 * The main activity where the user can select which sensor-fusion he wants to try out
 * 
 * @author Alexander Pacha
 * 
 */
public class SensorSelectionActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative,
     * which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_selection);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Initialises a new sectionPagerAdapter
         * 
         * @param fm the fragment Manager
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new OrientationVisualisationFragment();
            Bundle args = new Bundle();
            args.putInt(OrientationVisualisationFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return getString(R.string.title_section4).toUpperCase(l);
            case 4:
                return getString(R.string.title_section5).toUpperCase(l);
            case 5:
                return getString(R.string.title_section6).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A fragment that contains the same visualisation for different orientation providers
     */
    public static class OrientationVisualisationFragment extends Fragment {
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

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public void onResume() {
            // Ideally a game should implement onResume() and onPause()
            // to take appropriate action when the activity looses focus
            super.onResume();
            currentOrientationProvider.start();
            mGLSurfaceView.onResume();
        }

        @Override
        public void onPause() {
            // Ideally a game should implement onResume() and onPause()
            // to take appropriate action when the activity looses focus
            super.onPause();
            currentOrientationProvider.stop();
            mGLSurfaceView.onPause();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Initialise the orientationProvider
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                currentOrientationProvider = new ImprovedOrientationSensor1Provider((SensorManager) getActivity()
                        .getSystemService(SENSOR_SERVICE));
                break;
            case 2:
                currentOrientationProvider = new ImprovedOrientationSensor2Provider((SensorManager) getActivity()
                        .getSystemService(SENSOR_SERVICE));
                break;
            case 3:
                currentOrientationProvider = new RotationVectorProvider((SensorManager) getActivity().getSystemService(
                        SENSOR_SERVICE));
                break;
            case 4:
                currentOrientationProvider = new CalibratedGyroscopeProvider((SensorManager) getActivity()
                        .getSystemService(SENSOR_SERVICE));
                break;
            case 5:
                currentOrientationProvider = new GravityCompassProvider((SensorManager) getActivity().getSystemService(
                        SENSOR_SERVICE));
                break;
            case 6:
                currentOrientationProvider = new AccelerometerCompassProvider((SensorManager) getActivity()
                        .getSystemService(SENSOR_SERVICE));
                break;
            default:
                break;
            }

            // Create our Preview view and set it as the content of our Activity
            mRenderer = new CubeRenderer();
            mRenderer.setOrientationProvider(currentOrientationProvider);
            mGLSurfaceView = new GLSurfaceView(getActivity());
            mGLSurfaceView.setRenderer(mRenderer);

            mGLSurfaceView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mRenderer.toggleShowCubeInsideOut();
                    return true;
                }
            });

            return mGLSurfaceView;
        }
    }

}
