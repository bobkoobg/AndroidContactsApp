package sk.stavona.contacts2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class StatsDB extends ActionBarActivity {

    StringBuffer buffer;
    TextView textViewStatsDBContact;

    //Activity layout logic
    String databaseData;
    private static final String DB_DATA = "stringOfDatabaseData";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_db);

        textViewStatsDBContact = (TextView) findViewById(R.id.textViewStatsDbContacts);
        buffer = new StringBuffer();

        activityLoadingDBLogic();

}

    private void activityLoadingDBLogic(){
        DatabaseContactHelper myDb = new DatabaseContactHelper(this);
        Cursor res = myDb.getAllData();

        buffer = new StringBuffer();
        if(res.getCount()==0){
            //show message
            showMessage("Error","Nothing found");
        }
        else {

            while (res.moveToNext()){
                buffer.append("Id :"+res.getString(0)+"\n");
                buffer.append("Number :"+res.getString(1)+"\n");
                buffer.append("Duration :"+res.getString(2)+"\n");
                buffer.append("CountOfSms :"+res.getString(3)+"\n");
                buffer.append("PicPath :"+res.getString(4)+"\n\n");
            }
            // Show all data
            textViewStatsDBContact.setText(buffer.toString());
            databaseData = buffer.toString();
        }
    }

    public void showMessage (String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setTitle(message);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statsdb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Load_DB) {
            DatabaseContactHelper myDb = new DatabaseContactHelper(this);
            Cursor res = myDb.getAllData();

            buffer = new StringBuffer();
            if(res.getCount()==0){
                //show message
                showMessage("Error","Nothing found");
            }
            else {

                while (res.moveToNext()){
                    buffer.append("Id :"+res.getString(0)+"\n");
                    buffer.append("Number :"+res.getString(1)+"\n");
                    buffer.append("Duration :"+res.getString(2)+"\n");
                    buffer.append("CountOfSms :"+res.getString(3)+"\n");
                    buffer.append("PicPath :"+res.getString(4)+"\n\n");
                }
                // Show all data
                textViewStatsDBContact.setText(buffer.toString());
                databaseData = buffer.toString();
            }
        } else if (id == R.id.action_Export_to_TXT) {
            String actualMessageForSD = buffer.toString();
            String path = "MyFile.txt";
            BufferedWriter bufferedWriter = null;
            try {
                //File myNewTXTfile = new File(getFilesDir() + File.separator + path);
                File myNewTXTfile = new File("/sdcard/" + path);
                if(myNewTXTfile.exists()){
                    myNewTXTfile.delete();
                }
                bufferedWriter = new BufferedWriter(new FileWriter(myNewTXTfile));
                bufferedWriter.write(actualMessageForSD);
                bufferedWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            File f = new File(getFilesDir()+ File.separator + path);
            if(f.exists() && !f.isDirectory()){
                Toast.makeText(getBaseContext(), "File was created ;)" ,Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_SEND_to_MAIL) {
            Intent iMailSender = new Intent(StatsDB.this, MailSenderActivity.class);
            iMailSender.putExtra("list",buffer.toString());
            startActivity(iMailSender);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Toast.makeText(this,"Change",Toast.LENGTH_LONG).show();
        super.onConfigurationChanged(newConfig);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(DB_DATA, databaseData);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            databaseData = savedInstanceState.getString(DB_DATA);
            textViewStatsDBContact.setText(databaseData);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

}
