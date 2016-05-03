package sk.stavona.contacts2;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity  extends Activity {

    //Contacts Loading Logic
    ListView listViewOfContacts;
    ArrayAdapter<String> adapterContactNames;
    ProgressDialog dialog;

    //Activity layout logic
    ArrayList<String> arrayOfContactNames;
    private ArrayList<EntityContact> parcelableListOfContacts;
    private static final String LIST_CONTACTS_NAME = "listOfContactsNames";
    private static final String LIST_CONTACTS = "listOfContacts";
    private static final String LIST_BUTTON_BOOLEAN = "booleanOfButton";

    //Buttons
    Button buttonLoadContacts;
    Button buttonMoveToStatsDB;
    Button buttonOpenGpsService;

    int counter = 0;


    //Image testing purposes
    ImageView imageViewPicture;

    //Screen positioning, vital for error-handling
    //Integer screenOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLogic();

    }

    //Connecting to activity objects
    private void mainLogic(){
        //screenOrientation = 0;

        buttonLoadContacts = (Button) findViewById(R.id.buttonLoadContacts);
        buttonLoadContacts.setOnClickListener(contactLoader);

        buttonMoveToStatsDB = (Button) findViewById(R.id.buttonStatDB);
        buttonMoveToStatsDB.setOnClickListener(statsDbLoader);

        buttonOpenGpsService = (Button) findViewById(R.id.buttonOpenGpsService);
        buttonOpenGpsService.setOnClickListener(serviceLoader);

        listViewOfContacts = (ListView)findViewById(R.id.listOfContacts);


    }

    //onClickListener for Activity for loading statistics from SQLite
    View.OnClickListener statsDbLoader = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent iStatsDB = new Intent(MainActivity.this, StatsDB.class);
            startActivity(iStatsDB);
        }
    };

    //onClickListener for the Activity for Service (GPS Service on-Phone-Call-Received)
    View.OnClickListener serviceLoader = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent iStatsDB = new Intent(MainActivity.this, ServiceActivity.class);
            startActivity(iStatsDB);
        }
    };

    //onClickListener for loading the Contacts with Query from DB in AsyncTask
    View.OnClickListener contactLoader = new View.OnClickListener() {

        public void onClick(View v) {
            buttonLoadContacts.setEnabled(false);
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading Contacts");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.setProgress(0);
            dialog.show();

            AsyncTaskLoadingContacts runner = new AsyncTaskLoadingContacts(getContentResolver(),dialog,MainActivity.this); // Creating instance of class

            //Locking screen orientation
            //ScreenOrientation so = new ScreenOrientation();
            //screenOrientation = so.getScreenOrientation(MainActivity.this);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

            //Starting AsyncTask
            runner.execute();
        };
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_details, menu);
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

    //Called on AsyncTask onPostExecute (aka when the AsyncTask is done)
    public void contactLoaderLogic(List<EntityContact>listOfEntityContacts){

        parcelableListOfContacts = (ArrayList) listOfEntityContacts;
        //Logic for structuring the items in the List View
        final List<EntityContact> listOfEntityContact = parcelableListOfContacts;
        arrayOfContactNames = new ArrayList();
        for (EntityContact value : listOfEntityContact) {
            arrayOfContactNames.add(value.getDisplay_name());
        }
        Collections.sort(arrayOfContactNames.subList(0, arrayOfContactNames.size()));

        adapterContactNames = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, arrayOfContactNames);
        listViewOfContacts.setAdapter(adapterContactNames);

       // listViewOfContacts = getListView();
        listViewOfContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //setOnItemClickListener if user clicks on one of the names in the List
        listViewOfContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedContactName = arrayOfContactNames.get(position);
                for(EntityContact curEC : listOfEntityContact){
                    if(curEC.getDisplay_name().equals(clickedContactName)){
                        //Vibration for success :P
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);

                        //Move to details activity
                        Intent iDetails = new Intent(MainActivity.this, ShowDetails.class);
                        iDetails.putExtra("phoneNoCurrentContact",curEC.getNumber());
                        iDetails.putExtra("nameCurrentContact",curEC.getDisplay_name());
                        startActivity(iDetails);
                        break;
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        if(dialog != null){
            dialog.dismiss();
        }
        super.onPause();
    }
    //End of not sure how to use them


    //*****************CHANGING  VIEW ***********


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LIST_CONTACTS_NAME, arrayOfContactNames);
        outState.putBoolean(LIST_BUTTON_BOOLEAN,buttonLoadContacts.isEnabled());
        outState.putParcelableArrayList(LIST_CONTACTS,parcelableListOfContacts);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

            arrayOfContactNames = (ArrayList<String>) savedInstanceState.getSerializable(LIST_CONTACTS_NAME);

        if (arrayOfContactNames != null){
            listViewOfContacts = (ListView) findViewById(R.id.listOfContacts);

            adapterContactNames = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_list_item_1, arrayOfContactNames);
            listViewOfContacts.setAdapter(adapterContactNames);
            listViewOfContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            parcelableListOfContacts = savedInstanceState.getParcelableArrayList(LIST_CONTACTS);
            contactLoaderLogic(parcelableListOfContacts);

            buttonLoadContacts.setEnabled(savedInstanceState.getBoolean(LIST_BUTTON_BOOLEAN));
        }


        super.onRestoreInstanceState(savedInstanceState);
    }
}




