package org.hitlabnz.sensor_fusion_demo;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Activity, that displays a single WebView with the text shown under the section About in the settings
 * 
 * @author Alexander Pacha
 * 
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Get the locale substring to access the localised assets
        String localPrefix = Locale.getDefault().getLanguage().substring(0, 2).toLowerCase(Locale.US);

        // Load the website as the only action for this activity
        WebView webView = (WebView) findViewById(R.id.webViewAbout);
        webView.loadUrl("file:///android_asset/about/" + localPrefix + "/index.html");

        // Enable the logo in the top left corner to bring the user back to another activity.
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
