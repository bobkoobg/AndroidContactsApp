package sk.stavona.contacts2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DatabaseContactHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Contacts_DB.db";
    public static final String TABLE_NAME = "contactStats_TBL";
    public static final String ContactsStats_TBL_col_1 = "ID";
    public static final String ContactsStats_TBL_col_2 = "NUMBER";
    public static final String ContactsStats_TBL_col_3 = "CALL_DURATION";
    public static final String ContactsStats_TBL_col_4 = "COUNT_OF_SMS";
    public static final String ContactsStats_TBL_col_5 = "PIC_PATH";


    public DatabaseContactHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
       // SQLiteDatabase db = this.getWritableDatabase();
    }

    public DatabaseContactHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER TEXT)");
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER TEXT,CALL_DURATION TEXT,COUNT_OF_SMS INTEGER,PIC_PATH TEXT);");
        //String DATABASE_CREATE = "create table CONTACTSTATS(_id integer primary key autoincrement, name text not null);";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME );
        onCreate(db);

    }

    public void updateData(String number, String duration,Integer countOfSms, String picPath){
        SQLiteDatabase db = this.getWritableDatabase();
/*        db.execSQL("UPDATE "+ TABLE_NAME +" SET "+ ContactsStats_TBL_col_3+"='"+duration+"' , " +
                ""+ ContactsStats_TBL_col_4 +"="+countOfSms+" , " +
                ""+ ContactsStats_TBL_col_5 +"='"+picPath+"'  " +
                "WHERE "+ContactsStats_TBL_col_2+"="+number);*/

        ContentValues values=new ContentValues();
        values.put(ContactsStats_TBL_col_3,duration);
        values.put(ContactsStats_TBL_col_4,countOfSms);
        values.put(ContactsStats_TBL_col_5,picPath);

        int id = db.update(TABLE_NAME,values,ContactsStats_TBL_col_2+"='"+number+"'",null);
    }


    public boolean insertDate (String number,String duration,Integer countOfSms,String picPath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsStats_TBL_col_5,picPath);
        contentValues.put(ContactsStats_TBL_col_3,duration);
        contentValues.put(ContactsStats_TBL_col_4,countOfSms);
        contentValues.put(ContactsStats_TBL_col_2,number);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result == -1){
            return false;
        }
        return true;
    };

    public Cursor getAllData (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return  res;
    }

    public void dropTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }
}


