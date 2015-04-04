package com.claire_luis.gps.test_gps;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.database.sqlite.SQLiteCursorDriver;

public class MainActivity extends Activity implements LocationListener{
    LocationManager lm;
    TextView lt, ln, size;
    String provider;
    Location l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ln=(TextView)findViewById(R.id.lng);
        lt=(TextView)findViewById(R.id.lat);
        //get location service
        lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();
        //criteria object will select best service based on
        //Accuracy, power consumption, response, bearing and monetary cost
        //set false to use best service otherwise it will select the default Sim network
        //and give the location based on sim network
        //now it will first check satellite than Internet than Sim network location
        provider=lm.getBestProvider(c, false);
        //now you have best provider
        //get location
        l=lm.getLastKnownLocation(provider);

        /*if(l!=null)
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
            ln.setText("No Provider");
            lt.setText("No Provider");
        }*/
        int content;
        DataBassClaire x = new DataBassClaire(this);
        //x.add_stuff();
        String hello = x.getDatabaseName();
        String hello2 = x.toString();
        //content = x.bring_me_stuff();
        size=(TextView)findViewById(R.id.size);
        size.setText(hello);



    }
    //If you want location on changing place also than use below method
    //otherwise remove all below methods and don't implement location listener
    @Override
    public void onLocationChanged(Location arg0)
    {
        double lng=l.getLongitude();
        double lat=l.getLatitude();
        ln.setText(""+lng);
        lt.setText(""+lat);
    }

    public void onClick(View view){

        double boner = 4.0;
        double lng=l.getLongitude();
        double lat=l.getLatitude();
        ln.setText(""+lng);
        lt.setText("" + lat);

    }

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



}

class DataBassClaire extends SQLiteOpenHelper {

    private String DB_PATH = "/data/data/com.claire_luis.gps.test_gps/databases/";
    private String DB_NAME = "test_gps.db";
    private final int DATABASE_VERSION = 10;
    private final Context myContext;
    private SQLiteDatabase myDataBase;


    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "lat";
    public static final String COLUMN_COMMENT = "longi";
    //SCHEMA FOR DATABASE
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement);";

    public DataBassClaire(Context context){

        super(context, "test_gps.db", null, 10);
        this.myContext = context;
    }
    public SQLiteDatabase getdatabase(){
        return this.myDataBase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //super.onCreate(db);
        db.execSQL(DATABASE_CREATE);
        String BONER = "INSERT INTO "
                + TABLE_COMMENTS + "(" + COLUMN_ID
                + ") VALUES('10');";
        db.execSQL(BONER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);
        myContext.deleteDatabase(DB_NAME);
    }

    public void add_stuff(){

        String BONER = "INSERT INTO "
                + TABLE_COMMENTS + "(" + COLUMN_ID
                + ", " + COLUMN_COMMENT
                + ") VALUES('10', 'hello');";
        //this.execSQL(BONER);

        SQLiteDatabase db = this.getdatabase();

        // Inserting Row
        db.execSQL(BONER);
        db.close(); // Closing database connection

    }

    public int bring_me_stuff(){

        SQLiteDatabase db = this.getdatabase();

        String BONER = "SELECT * FROM " + TABLE_COMMENTS+ ";";
        Cursor cursor = db.rawQuery(BONER, null);
        // Inserting Row

        cursor.close();
        db.execSQL(BONER);
        db.close(); // Closing database connection
        return cursor.getCount();
    }

}