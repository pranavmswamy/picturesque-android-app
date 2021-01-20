package com.example.pranav.picturesque;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranav on 28-10-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createTable = "create table notes(picid text, note text)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void insertRow(String picid, String note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("picid",picid);
        cv.put("note",note);
        db.insert("notes",null,cv);
        db.close();
    }

    public String displayNotes(String picid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select note from notes where picid=\""+picid+"\"",null);
        if(c.getCount()>0)
        {
            c.moveToLast();
            String note = c.getString(0);
            db.close();
            return note;
        }
        else
        {
            db.close();
            return "";
        }

    }
}
