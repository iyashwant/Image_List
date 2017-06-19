package com.example.iyashwant.sqlite_image;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyashwant on 17/6/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "imagedb";

    private static final String TABLE_IMAGES = "images";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_IMAGES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        onCreate(db);
    }


    public void addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image._name);
        values.put(KEY_IMAGE, image._image);


        db.insert(TABLE_IMAGES, null, values);
        db.close();
    }
    
    
    Image getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
                        KEY_NAME, KEY_IMAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Image image = new Image(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getBlob(2));


        return image;

    }

    
    public List<Image> getAllImages() {
        List<Image> imageList = new ArrayList<Image>();
        String selectQuery = "SELECT  * FROM images ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.setID(Integer.parseInt(cursor.getString(0)));
                image.setName(cursor.getString(1));
                image.setImage(cursor.getBlob(2));
                imageList.add(image);
            } while (cursor.moveToNext());
        }

        db.close();
        return imageList;

    }


    public int updateImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.getName());
        values.put(KEY_IMAGE, image.getImage());

        // updating row
        return db.update(TABLE_IMAGES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(image.getID()) });

    }


    public void deleteImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, KEY_ID + " = ?",
                new String[] { String.valueOf(image.getID()) });
        db.close();
    }
    

    public void deleteall()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_IMAGES);
    }



}

