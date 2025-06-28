package com.example.papanibadah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "papan.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE info (id INTEGER PRIMARY KEY AUTOINCREMENT, judul TEXT, isi TEXT, waktu TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS info");
        onCreate(db);
    }
    public void deleteInfo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("info", "id=?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllInfo() {
        return getReadableDatabase().rawQuery("SELECT * FROM info ORDER BY id DESC", null);
    }

    public void insertInfo(String judul, String isi, String waktu) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("judul", judul);
        values.put("isi", isi);
        values.put("waktu", waktu);
        db.insert("info", null, values);
    }
    public void updateInfo(int id, String judul, String isi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("judul", judul);
        values.put("isi", isi);
        db.update("info", values, "id=?", new String[]{String.valueOf(id)});
    }

}

