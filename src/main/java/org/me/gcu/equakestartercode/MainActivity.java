package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.me.gcu.equakestartercode.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener
{
    Calendar dateAndTime = Calendar.getInstance();
    TextView dateAndTimeLabel;
    ArrayList<String> mylist = new ArrayList<String>();
    GoogleMap map;
    private Spinner spinner;
    private static final String[] paths = {"item 1", "item 2", "item 3"};

    private static final String TAG = "ProjectServerDemo";
    public static final String APP_ID = "3fa19507-c746-4dfa-8ded-ec60ef4d30d9";
    public static final String SERVER_URL = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml​";
    public static final String QUERY_FILE = "MhSeismology.xml";
    public static final String QUERY_OPTIONS = "?appid=" + APP_ID;
    public static final String QUERY_URL = SERVER_URL + QUERY_FILE + QUERY_OPTIONS;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateAndTimeLabel=(TextView)findViewById(R.id.dateAndTime);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);



        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.OptionsArray, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }
    public void chooseDate(View v){
        new DatePickerDialog(MainActivity.this,d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel(){
        dateAndTimeLabel.setText("Date Selected: "+ DateUtils.formatDateTime(this,dateAndTime.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR,year);
            dateAndTime.set(Calendar.MONTH,month);
            dateAndTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)  {

        switch (position) {
            case 0:
                TextView textViewGeo = (TextView) findViewById(R.id.textViewGeo);
                TextView textView = (TextView) findViewById(R.id.displayData);
                TextView descripTxt = (TextView) findViewById(R.id.textViewDes);
                TextView datelbl = (TextView) findViewById(R.id.dateAndTime);
                Button invisiBtn = (Button) findViewById(R.id.dateBtn);
                textViewGeo.setText("");
                textView.setText("");
                datelbl.setVisibility(View.INVISIBLE);
                invisiBtn.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                descripTxt.setVisibility(View.VISIBLE);
                break;
            case 1: {
                TextView textViewGeo1 = (TextView) findViewById(R.id.textViewGeo);
                TextView textView1 = (TextView) findViewById(R.id.displayData);
                TextView descripTxt1 = (TextView) findViewById(R.id.textViewDes);
                TextView datelbl1 = (TextView) findViewById(R.id.dateAndTime);
                Button invisiBtn1 = (Button) findViewById(R.id.dateBtn);
                datelbl1.setVisibility(View.INVISIBLE);
                invisiBtn1.setVisibility(View.INVISIBLE);
                textView1.setVisibility(View.VISIBLE);
                descripTxt1.setVisibility(View.INVISIBLE);

                Log.i(TAG, "Query server...");
                AsyncDownloader downloader = new AsyncDownloader();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                Runnable yourTaskRunner = new Runnable() {
                    public void run() {
                        new AsyncDownloader().execute("start");
                    }
                };
                scheduler.scheduleAtFixedRate(yourTaskRunner, 0, 60, TimeUnit.SECONDS);
                System.out.println("Updating ");
                downloader.execute();
            }
            break;
            case 2: {




                TextView datelbl2 = (TextView) findViewById(R.id.dateAndTime);
                Button invisiBtn2 = (Button) findViewById(R.id.dateBtn);

                datelbl2.setVisibility(View.VISIBLE);
                invisiBtn2.setVisibility(View.VISIBLE);

            }





            Log.i(TAG, "Query server...");
            AsyncDownloader downloader = new AsyncDownloader();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            Runnable yourTaskRunner = new Runnable() {
                public void run() { new AsyncDownloader().execute("start"); }
            };
            scheduler.scheduleAtFixedRate(yourTaskRunner, 0, 60, TimeUnit.SECONDS);
            System.out.println("Updating ");
            downloader.execute();

            TextView textViewGeo1 = (TextView) findViewById(R.id.textViewGeo);
            TextView textView1 = (TextView) findViewById(R.id.displayData);
            TextView descripTxt1 =(TextView) findViewById(R.id.textViewDes);
            textView1.setVisibility(View.VISIBLE);
            descripTxt1.setVisibility(View.INVISIBLE);
            break;

        }
    }

    private void dateSet(String pubDate)  {


        mylist.add(pubDate);
        for (int i=0; i<mylist.size(); i++) {
            String newStuff = mylist.get(i);
            Log.i(TAG, "THE LIST  "+ newStuff);
        }

        Log.i(TAG, "date list  "+ mylist);

        TextView textView = (TextView) findViewById(R.id.displayData);

        String pubdatemessage =
                textView.getText()+  pubDate ;

        textView.setText(pubdatemessage);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


        LatLng UnitedKingdom = new LatLng(53.871,-3.255);
        map.moveCamera(CameraUpdateFactory.newLatLng(UnitedKingdom));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(UnitedKingdom,4));

        LatLng MULL_ARGYLL_BUTE = new LatLng(56.410,-6.210);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(MULL_ARGYLL_BUTE).title("MULL, Argyll & Bute"));


        LatLng NorthAtlantic = new LatLng(   56.607,-10.454);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(NorthAtlantic).title("North Atlantic Ocean"));


        LatLng LINCOLNSHIRE = new LatLng(    52.871	,-0.255);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(LINCOLNSHIRE).title("Lincolnshire"));

        LatLng Perth= new LatLng(56.284,-3.759);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(Perth).title("Perth"));


        LatLng southnorthSea= new LatLng( 52.194,2.183);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(southnorthSea).title("South North Sea"));

        LatLng beattock= new LatLng( 55.281	,-3.524);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(beattock).title("Dumfries & Galloway"));


        LatLng centralNorthsea= new LatLng(  56.114,-2.288);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).position(centralNorthsea).title("Central North Sea"));

        LatLng solwayFirth= new LatLng(  54.816,-3.667);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(solwayFirth).title("Solway Firth"));

        LatLng cumbria= new LatLng(  54.576,-2.6948);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(cumbria).title("Cumbria"));

        LatLng argyle= new LatLng(  56.389,-5.544);
        map.addMarker(new MarkerOptions() .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(argyle).title("Kerrera, Argyll & Bute"));
    }








    public void onClick_data(View v ) {

        Log.i(TAG, "Get Geo...");
        AsyncDownloader downloader = new AsyncDownloader();
        downloader.execute();


    }



    public void handleNewGeo(String geo) {






    }













   /* public void onClick_QueryServer(View v) {
        Log.i(TAG, "Query server...");

        AsyncDownloader downloader = new AsyncDownloader();


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable yourTaskRunner = new Runnable() {
            public void run() { new AsyncDownloader().execute("start"); }
        };
        scheduler.scheduleAtFixedRate(yourTaskRunner, 0, 20, TimeUnit.SECONDS);
        System.out.println("Updating ");


        downloader.execute();
    }*/



    private void handleNewRecord(String title ) {

        TextView textView = (TextView) findViewById(R.id.displayData);


        String message =
                textView.getText()+ "\n"+title ;

        textView.setText(message);
    }



    public void handleNewDescription(String description)  {



        TextView textdescription = (TextView) findViewById(R.id.displayData);


        description = textdescription.getText().toString() + description  + "\n"  ;
        SpannableString spannableString = new SpannableString(description);


        textdescription.setText(spannableString);
        description = description.replace("M 0.1", "<font color='#02f7eb'>M 0.1</font>");
        description = description.replace("Magnitude: 0.1", "<font color='#02f7eb'>Magnitude: 0.1</font>");
        description = description.replace("M 0.2", "<font color='#02f7eb'>M 0.2</font>");
        description = description.replace("Magnitude: 0.2", "<font color='#02f7eb'>Magnitude: 0.2</font>");
        description = description.replace("M 0.3", "<font color='#02f7eb'>M 0.3</font>");
        description = description.replace("Magnitude: 0.3", "<font color='#02f7eb'>Magnitude: 0.3</font>");
        description = description.replace("M 0.4", "<font color='#02f7eb'>M 0.4</font>");
        description = description.replace("Magnitude: 0.4", "<font color='#02f7eb'>Magnitude: 0.4</font>");
        description = description.replace("M 0.5", "<font color='#02f7eb'>M 0.5</font>");
        description = description.replace("Magnitude: 0.5", "<font color='#02f7eb'>Magnitude: 0.5</font>");
        description = description.replace("M 0.6", "<font color='#02f7eb'>M 0.6</font>");
        description = description.replace("Magnitude: 0.6", "<font color='#02f7eb'>Magnitude: 0.6</font>");
        description = description.replace("M 0.7", "<font color='#02f7eb'>M 0.7</font>");
        description = description.replace("Magnitude: 0.7", "<font color='#02f7eb'>Magnitude: 0.7</font>");
        description = description.replace("M 0.8", "<font color='#02f7eb'>M 0.8</font>");
        description = description.replace("Magnitude: 0.8", "<font color='#02f7eb'>Magnitude: 0.8</font>");
        description = description.replace("M 0.9", "<font color='#02f7eb'>M 0.9</font>");
        description = description.replace("Magnitude: 0.9", "<font color='#02f7eb'>Magnitude: 0.9</font>");

        description = description.replace("M 1.0", "<font color='#1aff00'>M 1.0</font>");
        description = description.replace("Magnitude: 1.0", "<font color='#1aff00'>Magnitude: 1.0</font>");
        description = description.replace("M 1.1", "<font color='#1aff00'>M 1.1</font>");
        description = description.replace("Magnitude: 1.1", "<font color='#1aff00'>Magnitude: 1.1</font>");
        description = description.replace("M 1.2", "<font color='#1aff00'>M 1.2</font>");
        description = description.replace("Magnitude: 1.2", "<font color='#1aff00'>Magnitude: 1.2</font>");
        description = description.replace("M 1.3", "<font color='#1aff00'>M 1.3</font>");
        description = description.replace("Magnitude: 1.3", "<font color='#1aff00'>Magnitude: 1.3</font>");
        description = description.replace("M 1.4", "<font color='#1aff00'>M 1.4</font>");
        description = description.replace("Magnitude: 1.4", "<font color='#1aff00'>Magnitude: 1.4</font>");
        description = description.replace("M 1.5", "<font color='#ff9317'>M 1.5</font>");
        description = description.replace("Magnitude: 1.5", "<font color='#ff9317'>Magnitude: 1.5</font>");
        description = description.replace("M 1.6", "<font color='#ff9317'>M 1.6</font>");
        description = description.replace("Magnitude: 1.6", "<font color='#ff9317'>Magnitude: 1.6</font>");
        description = description.replace("M 1.7", "<font color='#ff9317'>M 1.7</font>");
        description = description.replace("Magnitude: 1.7", "<font color='#ff9317'>Magnitude: 1.7</font>");
        description = description.replace("M 1.8", "<font color='#ff9317'>M 1.8</font>");
        description = description.replace("Magnitude: 1.8", "<font color='##ff9317'>Magnitude: 1.8</font>");
        description = description.replace("M 1.9", "<font color='#ff9317'>M 1.9</font>");
        description = description.replace("Magnitude: 1.9", "<font color='#ff9317'>Magnitude: 1.9</font>");

        description = description.replace("M 2.0", "<font color='#ff9317'>M 2.0</font>");
        description = description.replace("Magnitude: 2.0", "<font color='#ff9317'>Magnitude: 2.0</font>");
        description = description.replace("M 2.1", "<font color='#ff9317'>M 2.1</font>");
        description = description.replace("Magnitude: 2.1", "<font color='#ff9317'>Magnitude: 2.1</font>");
        description = description.replace("M 2.2", "<font color='#ff9317'>M 2.2</font>");
        description = description.replace("Magnitude: 2.2", "<font color='#ff9317'>Magnitude: 2.2</font>");
        description = description.replace("M 2.3", "<font color='#ff9317'>M 2.3</font>");
        description = description.replace("Magnitude: 2.3", "<font color='#ff9317'>Magnitude: 2.3</font>");
        description = description.replace("M 2.4", "<font color='#ff9317'>M 2.4</font>");
        description = description.replace("Magnitude: 2.4", "<font color='#ff9317'>Magnitude: 2.4</font>");
        description = description.replace("M 2.5", "<font color='#ff9317'>M 2.5</font>");
        description = description.replace("Magnitude: 2.5", "<font color='#ff9317'>Magnitude: 2.5</font>");
        description = description.replace("M 2.6", "<font color='#ff9317'>M 2.6</font>");
        description = description.replace("Magnitude: 2.6", "<font color='#ff9317'>Magnitude: 2.6</font>");
        description = description.replace("M 2.7", "<font color='#ff9317'>M 2.7</font>");
        description = description.replace("Magnitude: 2.7", "<font color='#ff9317'>Magnitude: 2.7</font>");
        description = description.replace("M 2.8", "<font color='#ff9317'>M 2.8</font>");
        description = description.replace("Magnitude: 2.8", "<font color='#ff9317'>Magnitude: 2.8</font>");
        description = description.replace("M 2.9", "<font color='#ff9317'>M 2.9</font>");
        description = description.replace("Magnitude: 2.9", "<font color='#ff9317'>Magnitude: 2.9</font>");

        description = description.replace("M 3.0", "<font color='#c42727'>M 3.0</font>");
        description = description.replace("Magnitude: 3.0", "<font color='#c42727'>Magnitude: 3.0</font>");
        description = description.replace("M 3.1", "<font color='#c42727'>M 3.1</font>");
        description = description.replace("Magnitude: 3.1", "<font color='#c42727'>Magnitude: 3.1</font>");
        description = description.replace("M 3.2", "<font color='#c42727'>M 3.2</font>");
        description = description.replace("Magnitude: 3.2", "<font color='#c42727'>Magnitude: 3.2</font>");
        description = description.replace("M 3.3", "<font color='#c42727'>M 3.3</font>");
        description = description.replace("Magnitude: 3.3", "<font color='#c42727'>Magnitude: 3.3</font>");
        description = description.replace("M 3.4", "<font color='#c42727'>M 3.4</font>");
        description = description.replace("Magnitude: 3.4", "<font color='#c42727'>Magnitude: 3.4</font>");
        description = description.replace("M 3.5", "<font color='#c42727'>M 3.5</font>");
        description = description.replace("Magnitude: 3.5", "<font color='#c42727'>Magnitude: 3.5</font>");
        description = description.replace("M 3.6", "<font color='#c42727'>M 3.6</font>");
        description = description.replace("Magnitude: 3.6", "<font color='#c42727'>Magnitude: 3.6</font>");
        description = description.replace("M 3.7", "<font color='#c42727'>M 3.7</font>");
        description = description.replace("Magnitude: 3.7", "<font color='#c42727'>Magnitude: 3.7</font>");
        description = description.replace("M 3.8", "<font color='#c42727'>M 3.8</font>");
        description = description.replace("Magnitude: 3.8", "<font color='#c42727'>Magnitude: 3.8</font>");
        description = description.replace("M 3.9", "<font color='#c42727'>M 3.9</font>");
        description = description.replace("Magnitude: 3.9", "<font color='#c42727'>Magnitude: 3.9</font>");

        description = description.replace("Title", "<br>"+"<br>"+"<br>"+ "<big>Title</big>" + "<br>");
        description = description.replace("Description", "<br>"+"<br>"+"<big>Description</big>" + "<br>");


        textdescription.setText("\n"+description + "\n");
        textdescription.setText(Html.fromHtml(description  ));



    }






    // Inner class for doing background download.


    //Name:Arron Fairley StudentID: s1918040
    class AsyncDownloader extends AsyncTask<Object, String, Integer> {

        @Override
        protected Integer doInBackground(Object... arg0) {
            XmlPullParser receivedData = tryDownloadingXmlData();
            int ItemsFound = tryParsingXmlData(receivedData);
            TextView textViewGeo = (TextView) findViewById(R.id.textViewGeo);
            TextView textView = (TextView) findViewById(R.id.displayData);
            textViewGeo.setText("");
            textView.setText("");
            return ItemsFound;
        }

        private XmlPullParser tryDownloadingXmlData() {
            try {
                Log.i(TAG, "Now downloading...");
                URL xmlUrl = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
                XmlPullParser receivedData = XmlPullParserFactory.newInstance().newPullParser();
                receivedData.setInput(xmlUrl.openStream(), null);
                return receivedData;
            } catch (XmlPullParserException e) {
                Log.e(TAG, "XmlPullParserExecption", e);
            } catch (IOException e) {
                Log.e(TAG, "XmlPullParserExecption", e);
            }
            return null;
        }

        private int tryParsingXmlData(XmlPullParser receivedData) {
            if (receivedData != null) {
                try {
                    return processReceivedData(receivedData);
                } catch (XmlPullParserException e) {
                    Log.e(TAG, "Pull Parser failure", e);
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception parsing XML", e);
                }
            }
            return 0;
        }

        private int processReceivedData(XmlPullParser xmlData) throws XmlPullParserException, IOException {
            int itemsFound = 0;

            // Find values in the XML items
            String title = "";      // Attributes
            String description = "";
            String geo = "";
            String pubDate = "";       // Text

            int eventType = -1;
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                String tagName = xmlData.getName();

                switch (eventType) {
                    case XmlResourceParser.START_TAG:
                        // Start of a record, so pull values encoded as attributes.
                        if (tagName.equals("pubDate")) {
                            //link = xmlData.getAttributeValue(null, "link");
                            //   description = xmlData.getAttributeValue(null, "description");


                            pubDate = ""+"\n";
                            //data="";
                        }

                        if (tagName.equals("geo:lat")) {
                            //link = xmlData.getAttributeValue(null, "link");
                            //   description = xmlData.getAttributeValue(null, "description");


                            geo = "";
                            //data="";
                        }

                        if (tagName.equals("title")) {
                            // link = xmlData.getAttributeValue(null, "link");
                            description = xmlData.getAttributeValue(null, "description");
                            title = "Title"+"\n";
                            //data="";
                        }

                        if (tagName.equals("description")) {
                            //link = xmlData.getAttributeValue(null, "link");
                            description =  "Description"+"\n";
                            //title = "";
                            //data="";
                        }


                        break;


                    // Grab data text (very simple processing)
                    // NOTE: This could be full XML data to process.
                    case XmlResourceParser.TEXT:
                        //  data += xmlData.getText();
                        title += xmlData.getText();
                        description += xmlData.getText();
                        pubDate += xmlData.getText();

                        break;


                    case XmlPullParser.END_TAG:
                        if (tagName.equals("pubDate")) {
                            itemsFound++;
                            publishProgress("","","",pubDate);
                        }
                        if (tagName.equals("title")) {
                            itemsFound++;
                            publishProgress(title,"","","");
                        }
                        if (tagName.equals("description")) {
                            itemsFound++;
                            publishProgress("",description,"","");
                        }
                        if (tagName.equals("geo:lat")) {
                            itemsFound++;
                            publishProgress("", "", geo, "");
                        }
                        break;

                }
                eventType = xmlData.next();
            }

            // Handle no data available: Publish an empty event.
            if (itemsFound == 0) {
                publishProgress();
            }
            Log.i(TAG, "Finished processing "+itemsFound+" item.");
            return itemsFound;


        }


        @Override

        protected void onProgressUpdate(String... values) {
            if (values.length == 0) {
                Log.i(TAG, "No data downloaded");
            }
            if (values.length == 4) {
                String title = values[0];
                String description = values[1];
                String geo = values[2];
                String pubDate= values[3];




                // Log it
                Log.i(TAG, "Title: " + title + ",Description: " + description);
                Log.i(TAG, "Geo: " + geo + ",pubDate: " + pubDate);



                // Pass it to the application
                handleNewRecord(title);


                String sDate1= pubDate;
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "CONVERTED DATE  "+ sDate1);
                Log.i(TAG,  pubDate);




                dateSet(pubDate);



                handleNewDescription(description);




            }
            super.onProgressUpdate(values);



        }

    }
}

