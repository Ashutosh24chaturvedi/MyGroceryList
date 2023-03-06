package com.seeksolution.mygrocerylist.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.seeksolution.mygrocerylist.Model.Grocery;
import com.seeksolution.mygrocerylist.Util.Contants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;

    public DatabaseHandler(Context context) {
        super(context, Contants.DB_NAME, null , Contants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //creating table

        String CREATE_GROCERY_TABLE = "CREATE TABLE "+ Contants.TABLE_NAME + "("
                + Contants.KEY_ID + " INTEGER PRIMARY KEY, " + Contants.KEY_GROCERY_ITEM + " TEXT, "
                + Contants.KEY_QTY_NUMBER + " TEXT, " + Contants.KEY_DATE_NAME
                + " LONG );";

        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    //CRUD operation

    //Add Grocery
    public void addGrocery(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();
        //key values data
        ContentValues values = new ContentValues();
        values.put(Contants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Contants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Contants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//get current system time

        //Insert the row
        db.insert(Contants.TABLE_NAME , null , values);

//        Log.d("Saved!!","Saved to Database");
    }

    //Get a Grocery
    @SuppressLint("Range")
    public Grocery getGrocery(int id){

        SQLiteDatabase db= this.getReadableDatabase();

        Cursor cursor = db.query(Contants.TABLE_NAME, new String[]
                {Contants.KEY_ID, Contants.KEY_GROCERY_ITEM, Contants.KEY_QTY_NUMBER,
                Contants.KEY_DATE_NAME}, Contants.KEY_ID+"=?",
                new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contants.KEY_ID))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Contants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Contants.KEY_QTY_NUMBER)));

            //time saved in mill seconds so we need to convert timestamp
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Contants.KEY_DATE_NAME)))
                    .getTime());
            grocery.setDateItemAdded(formatedDate);

            return grocery;

    }


    //Get all Groceries
    @SuppressLint("Range")
    public List<Grocery> getAllGrocery(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Contants.TABLE_NAME, new String[]
                {Contants.KEY_ID, Contants.KEY_GROCERY_ITEM, Contants.KEY_QTY_NUMBER,
                        Contants.KEY_DATE_NAME},null ,null,null,null,
                Contants.KEY_DATE_NAME+" DESC");

        if(cursor.moveToFirst()){
            do{
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Contants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Contants.KEY_QTY_NUMBER)));

                //time saved in mill seconds so we need to convert timestamp
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Contants.KEY_DATE_NAME)))
                        .getTime());
                grocery.setDateItemAdded(formatedDate);

                //Add to the grocery list
                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }
        return groceryList;
    }

    //Update Grocery
    public int updateGrocery(Grocery grocery){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Contants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Contants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//get current system time

        //update row

        return db.update(Contants.TABLE_NAME, values, Contants.KEY_ID+"=?",
                new String[]{String.valueOf(grocery.getId())});
    }

    //Delete Grocery
    public void deleteGrocery(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contants.TABLE_NAME, Contants.KEY_ID+"=?",
                new String[] {String.valueOf(id)});

        db.close();
    }

    //Get Count
    public int getGroceryCount(){

        String countQuery = "SELECT * FROM "+ Contants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

}
