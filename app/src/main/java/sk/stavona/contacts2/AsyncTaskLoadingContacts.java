package sk.stavona.contacts2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;


public class AsyncTaskLoadingContacts extends AsyncTask<String,String,String> {

    private List<EntityContact> listOfEntityContact;
    private ContentResolver cr;
    private ProgressDialog dialog;
    private MainActivity ma;

    public AsyncTaskLoadingContacts( ContentResolver cr,ProgressDialog dialog,MainActivity ma) {
        this.cr = cr;
        this.dialog = dialog;
        this.ma = ma;
        listOfEntityContact = new ArrayList<>();
    }

    @Override
    protected void onProgressUpdate(String... values) {

        dialog.setProgress(Integer.parseInt(values[0]));
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String drawable) {

        dialog.hide();
        dialog=null;
        ma.contactLoaderLogic(listOfEntityContact);
      //  super.onPostExecute(drawable);
    }

    @Override
    protected String doInBackground(String... params) {
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        dialog.setMax(cur.getCount());
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(cur.getString(
                       // ContactsContract.Contacts.PHOTO_URI to add
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if(pCur != null ) {
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            publishProgress(cur.getPosition() + "");
                            //listOfEntityContact.add(new EntityContact(name, phoneNo, id, getPhotoUri(id)));
                            listOfEntityContact.add(new EntityContact(name, phoneNo, id, null));
                        }
                        pCur.close();
                    }

                }
            }
        }

        return null;
    }


/* Commented out code for Photo URI Logic (Working and tested)
    We decided not to use it, because we did not have enough time +
    the functionality of the program was getting too much.

public Uri getPhotoUri(String id) {
        try {
            Cursor cur = cr.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                //cur.
                if(cur.getCount()==0){
                    return null;
                }
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(id));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }*/



}

