package sk.stavona.contacts2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

public class MailSenderActivity extends ActionBarActivity {

    EditText editTextEmail, editTextSubject, editTextMessage;
    Button btnSend;
    String email,subject,message;
    String actualMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender);

        //Initialization
        editTextEmail = (EditText) findViewById(R.id.editTextTo);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        btnSend = (Button) findViewById(R.id.buttonSend);

        btnSend.setOnClickListener(emailSenderLoader);

        //Retrieve information from intent
        Bundle intentBundle = getIntent().getExtras();
        actualMessage = intentBundle.getString("list");
        editTextMessage.setText(actualMessage);
    }


    View.OnClickListener emailSenderLoader = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            String file = "MyFile.txt";
            String filePath = "/sdcard/" + file;
            try {

                File f = new File(filePath);
                if(f.exists() && !f.isDirectory()) {

                    Uri uriFilePath = Uri.fromFile(f);
                    email = editTextEmail.getText().toString();
                    subject = editTextSubject.getText().toString();
                    message = editTextMessage.getText().toString();


                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

                    emailIntent.putExtra(Intent.EXTRA_STREAM, uriFilePath);

                    startActivityForResult(Intent.createChooser(emailIntent, "Sending email..."), 1);
                }
                else{
                    Toast.makeText(getBaseContext(), "You should first export your database", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable t) {
                Toast.makeText(getApplicationContext(),"Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mail_sender, menu);
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
}
