package sk.stavona.contacts2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import version2.GpsService;


public class ServiceActivity extends ActionBarActivity {
    Button buttonStartGps;
    Button buttonStopGps;
    Button buttonGetPosition;
    TextView textViewGPSResult;

    TextView position;
    GpsService gps;
    private static final String LOCATION = null;
 //   Location location;

    LocationManager locationManager;
  //  String PROVIDER = LocationManager.GPS_PROVIDER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        buttonStartGps = (Button)findViewById(R.id.buttonStartGPS);
        buttonStartGps.setOnClickListener(startGpsServiceLoader);

        buttonStopGps = (Button)findViewById(R.id.buttonStopGPS);
        buttonStopGps.setOnClickListener(stopGpsServiceLoader);

        textViewGPSResult = (TextView) findViewById(R.id.textViewPosition);

        buttonGetPosition = (Button)findViewById(R.id.buttonGetPosition);
    //    buttonGetPosition.setOnClickListener(getPositionLoader);

        position = (TextView)findViewById(R.id.textViewPosition);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    //    final Location location = locationManager.getLastKnownLocation(PROVIDER);

        buttonGetPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GpsService(ServiceActivity.this);

                    if(gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        textViewGPSResult.setText("Your Location is -\nLat: " + latitude + "\nLong: "
                                + longitude);
                        Toast.makeText(
                                getApplicationContext(),
                                "Your Location is -\nLat: " + latitude + "\nLong: "
                                        + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        gps.showSettingsAlert();
                    }

            }
        });

    }



 // ******************* SERVICE FUNCTIONALITY *****************

    View.OnClickListener startGpsServiceLoader = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            startService(new Intent(getBaseContext(),GpsService.class));

        }
    };

    View.OnClickListener stopGpsServiceLoader = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            stopService(new Intent(getBaseContext(),GpsService.class));

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    //----------------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LOCATION,textViewGPSResult.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            textViewGPSResult.setText(savedInstanceState.getString(LOCATION));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
