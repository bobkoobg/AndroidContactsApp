package sk.stavona.contacts2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShowDetails extends ActionBarActivity {

    //Local DB
    DatabaseContactHelper myDB;

    //TextViews
    private TextView textViewContactInfo;
    private TextView textViewSmsLog;
    private TextView textViewCallLog;

    //Primitives
    private String phoneNoOfParticularContact = "";
    private String nameParticularContact = "";
    private Integer finalCountOfSMS = 0;
    private Integer durationOfCalls = 0;

    //Activity layout logic
    String callLog = "";
    String smsLog = "";
    private static final String CALL_LOG = "stringOfCallLog";
    private static final String SMS_LOG = "stringOfSmsLog";


    //Lists
    private List<EntityCallLog> listOfEntityCallLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        //Initialize database
        myDB = new DatabaseContactHelper(this);
        //in case of update in database use this code:
        //myDB.onUpgrade(new DatabaseContactHelper(this),1,2);

        //Initialize textView to represent the values from the Intent
        textViewCallLog = (TextView) findViewById(R.id.textViewCallLogOfParticularContact);
        textViewSmsLog = (TextView)findViewById(R.id.textViewSmsLog);
        textViewContactInfo = (TextView) findViewById(R.id.textViewContactInfo);

        //Retrieve information from intent
        Bundle intentBundle = getIntent().getExtras();
        phoneNoOfParticularContact = intentBundle.getString("phoneNoCurrentContact");
        nameParticularContact = intentBundle.getString("nameCurrentContact");

        //Set Info
        textViewContactInfo.setText("Name:" + nameParticularContact
                + ", phoneNo:" + phoneNoOfParticularContact);

        listOfEntityCallLog = new ArrayList();

        loadingAllCallLogs();
        loadingAllMessageLogs();
        }

    private void loadingAllCallLogs(){
        StringBuilder sb = new StringBuilder();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");

        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            if(phNum.contains(phoneNoOfParticularContact)) {
                listOfEntityCallLog.add(new EntityCallLog(phNum,callType,callDate.toString(),callDuration));
                sb.append("\nPhone Number:--- " + phNum + " \nCall Type:--- "
                        + callType + " \nCall Date:--- " + callDate
                        + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            }
        }
        managedCursor.close();

        if (sb.toString().isEmpty()){
            sb.append("No Call log with this Contact");
        }

        //Fill in result from query
        textViewCallLog.setText(sb.toString());
        callLog = sb.toString();
    }

    private void loadingAllMessageLogs(){
        StringBuffer sb = new StringBuffer();

        Uri uriSms=Uri.parse("content://sms/inbox");

        Cursor cursor=getContentResolver()
                .query(uriSms, new String[]{"_id","address","date","body"}, null, null, null);

        cursor.moveToFirst();

        while(cursor.moveToNext() ){

            String address=cursor.getString(1);
            String body=cursor.getString(3);

            if (phoneNoOfParticularContact.equals(address)){
                sb.append("[SMS] TO :"+address+"\n TEXT : "+body + "\n\n");
                finalCountOfSMS++;
            }
        }

        if (sb.toString().isEmpty()){
            sb.append("No SMS log with this Contact");
        }

        textViewSmsLog.setText(sb.toString());
        smsLog = sb.toString();
    }


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
        if (id == R.id.action_Load_Call_log) {

            Toast.makeText(this,"Reloading you Call log...",Toast.LENGTH_SHORT).show();
            //***START OF CALL LOG LOGIC

            StringBuilder sb = new StringBuilder();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                    null, null, strOrder);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            //sb.append(":) :)");
            sb.append("Call Log :");

            while (managedCursor.moveToNext()) {
                String phNum = managedCursor.getString(number);
                String callTypeCode = managedCursor.getString(type);
                String strcallDate = managedCursor.getString(date);
                Date callDate = new Date(Long.valueOf(strcallDate));
                String callDuration = managedCursor.getString(duration);
                String callType = null;
                int callcode = Integer.parseInt(callTypeCode);
                switch (callcode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }
                if(phNum.contains(phoneNoOfParticularContact)) {
                    listOfEntityCallLog.add(new EntityCallLog(phNum,callType,callDate.toString(),callDuration));
                    sb.append("\nPhone Number:--- " + phNum + " \nCall Type:--- "
                            + callType + " \nCall Date:--- " + callDate
                            + " \nCall duration in sec :--- " + callDuration);
                    sb.append("\n----------------------------------");
                }
            }
            managedCursor.close();

            if (sb.toString().isEmpty()){
                sb.append("No Call log with this Contact");
            }

            //Fill in result from query
            textViewCallLog.setText(sb.toString());
            callLog = sb.toString();
            //***END OF LOAD CALL LOG LOGIC
        }else if(id ==R.id.action_Load_SMS_Log){

            Toast.makeText(this,"Reloading you SMS log...",Toast.LENGTH_SHORT).show();
            //***START OF LOADING SMS LOGIC

            StringBuffer sb = new StringBuffer();
            //sb.append(":) :)");

            Uri uriSms=Uri.parse("content://sms/inbox");

            Cursor cursor=getContentResolver()
                    .query(uriSms, new String[]{"_id","address","date","body"}, null, null, null);

            cursor.moveToFirst();

            while(cursor.moveToNext() ){

                String address=cursor.getString(1);
                String body=cursor.getString(3);

                if (phoneNoOfParticularContact.equals(address)){
                    sb.append("[SMS] TO :"+address+"\n TEXT : "+body + "\n\n");
                    finalCountOfSMS++;
                }
            }

            if (sb.toString().isEmpty()){
                sb.append("No SMS log with this Contact");
            }

            textViewSmsLog.setText(sb.toString());
            smsLog = sb.toString();
            //***END OF LOADING SMS LOGIC
        }else if(id ==R.id.actions_Export_to_DB){
            //***START OF SAVE TO DB LOGIC

            Cursor res = myDB.getAllData();
            List<String> arrayOfPhoneNumbersInLocalDB = new ArrayList();


            if(res.getCount()==0){
                Toast.makeText(getApplicationContext(), "Nothing found in Local DB", Toast.LENGTH_LONG).show();
            }
            else {
                while (res.moveToNext()){
                    arrayOfPhoneNumbersInLocalDB.add(res.getString(1));
                }
            }
            if(arrayOfPhoneNumbersInLocalDB.contains(phoneNoOfParticularContact)){
                for (EntityCallLog ecl:listOfEntityCallLog){
                    durationOfCalls += Integer.parseInt(ecl.getDuration()) ;
                }
                myDB.updateData(phoneNoOfParticularContact,durationOfCalls+"",finalCountOfSMS,"picPath");
                Toast.makeText(getApplicationContext(),"You update the information of "+ nameParticularContact +" in the DB",Toast.LENGTH_LONG).show();
            }else{
                boolean isInserted =   myDB.insertDate(phoneNoOfParticularContact,durationOfCalls+"",finalCountOfSMS,"path");
                if (isInserted){
                    Toast.makeText(getApplicationContext(),"You posted the info of "+ nameParticularContact +" in the DB",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error : Not Inserted in DB : " + isInserted,Toast.LENGTH_LONG).show();
                }
            }
            //***END OF SAVE TO DB LOGIC
        }else if(id == R.id.actions_Load_DB){
            Intent iStatsDB = new Intent(ShowDetails.this, StatsDB.class);
            startActivity(iStatsDB);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SMS_LOG, smsLog);
        outState.putString(CALL_LOG,callLog);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            smsLog = savedInstanceState.getString(SMS_LOG);
            callLog = savedInstanceState.getString(CALL_LOG);
            textViewSmsLog = (TextView) findViewById(R.id.textViewSmsLog);
            textViewSmsLog.setText(smsLog);
            textViewCallLog = (TextView) findViewById(R.id.textViewCallLogOfParticularContact);
            textViewCallLog.setText(callLog);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
