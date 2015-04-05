package com.claire.fml;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog.Builder;
import android.widget.TextView;
import android.location.LocationListener;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, LocationListener {
    SQLiteDatabase db;
    Button btnAdd, btnViewAll, btnDelete, updateGPS;
    LocationManager lm;
    TextView lt, ln;
    String provider;
    Location l;
    LocationListener locationListener;
    Toast clairetoast;

    //Overriding location class methods and shiz
    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onLocationChanged(Location arg0)
    {
        double lng=l.getLongitude();
        double lat=l.getLatitude();
        ln.setText(""+lng);
        lt.setText(""+lat);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnViewAll=(Button)findViewById(R.id.btnViewAll);
        btnViewAll.setOnClickListener(this);

        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        updateGPS=(Button)findViewById(R.id.updateGPS);
        updateGPS.setOnClickListener(this);

        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(latitude VARCHAR,longitude VARCHAR);");

        ln=(TextView)findViewById(R.id.lng);
        lt=(TextView)findViewById(R.id.lat);
        locationListener = new MyLocationListener();
        lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        provider=lm.getBestProvider(c, true);
        //l=lm.getLastKnownLocation(provider);
        l=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        provider=LocationManager.GPS_PROVIDER;


        if(l!=null)
        {
            //get latitude and longitude of the location

            double lng=l.getLongitude();
            double lat=l.getLatitude();
            //display on text view
            ln.setText(""+lng);
            lt.setText(""+lat);
        }
        else
        {
            l=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            provider=LocationManager.NETWORK_PROVIDER;
            if(l!=null){
                double lng=l.getLongitude();
                double lat=l.getLatitude();
                //display on text view
                ln.setText(""+lng);
                lt.setText(""+lat);
            }
            else {

                ln.setText("No Provider");
                lt.setText("No Provider");
            }
        }

        lm.requestLocationUpdates(provider, 0, 1, this);


    }

        private final class MyLocationListener implements LocationListener {
            @Override
            public void onLocationChanged(Location locFromGps) {
                // called when the listener is notified with a location update from the GPS
                //showMessage("Listener", "Location Updated");
                double lng=locFromGps.getLongitude();
                double lat=locFromGps.getLatitude();
                clairetoast(lat + ", " + lng);
                //ln.setText("boner");
                l=locFromGps;
                //display on text view
                ln.setText("hi"+lng);
                lt.setText(""+lat);
            }
            @Override
            public void onProviderDisabled(String provider) {
                // called when the GPS provider is turned off (user turning off the GPS on the phone)
                showMessage("yo fam", "what the fuck you doing");
            }
            @Override
            public void onProviderEnabled(String provider) {
                // called when the GPS provider is turned on (user turning on the GPS on the phone)
                showMessage("coco", "crack cocaine wh00t");
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // called when the status of the GPS provider changes
            }

        }

        public void onClick(View view)
        {
        if(view==btnAdd)
        {
            double lng=l.getLongitude();
            double lat=l.getLatitude();
        db.execSQL("INSERT INTO student VALUES("+lat+", "+lng+");");
        showMessage("Success", "Record added");
        //clearText();
        }
        if(view==btnViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Latitude: "+c.getString(0)+"\n");
                buffer.append("Longitude: "+c.getString(1)+"\n");

            }
            showMessage("GPS Details", buffer.toString());
        }

        if(view==btnDelete)
        {

            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.moveToFirst())
            {
                db.execSQL("DROP TABLE student;");
                showMessage("Success", "Records Deleted");
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
            }

        }
        if(view==updateGPS){
            //provider, interval, distance, listener

            double lng=l.getLongitude();
            double lat=l.getLatitude();
            //display on text view
            ln.setText(""+lng);
            lt.setText(""+lat);

        }


    }

    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clairetoast(String message){


        clairetoast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);

        clairetoast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
