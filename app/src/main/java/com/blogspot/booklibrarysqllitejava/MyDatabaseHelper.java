package com.blogspot.booklibrarysqllitejava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Library.db";
    private static final int version = 1;
    public static final String TABLE_NAME = "Book_Table";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_TITLE = "book_title";
    public static String COLUMN_AUTHOR = "book_author";
    public static String COLUMN_PAGE = "book_page";

    private Context context;

    private String TAG = MyDatabaseHelper.class.getSimpleName();

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: db creation");

        String createBookTable = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + " TEXT , " + COLUMN_AUTHOR + " TEXT, " + COLUMN_PAGE + " INTEGER );";
        Log.d(TAG, "onCreate:createBookTable= " + createBookTable);
        sqLiteDatabase.execSQL(createBookTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: db ");
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);


    }

    public Boolean saveBook(String bookTitle, String author, int page) {
        Log.d(TAG, "saveBook: bookTitle " + bookTitle + " author " + author + " page-> " + page);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, bookTitle);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGE, page);
        Log.d(TAG, "saveBook:cv----> " + cv.toString());
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(context, "Book Added successfully", Toast.LENGTH_LONG).show();
            return true;
        }

    }

    Cursor getAllBooks() {
        String readALlQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (db != null) {
            c = db.rawQuery(readALlQuery, null);
        }
        return c;
    }

    Boolean update(int id, String title, String author, String page) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGE, page);
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    Boolean deleteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
       long result= db.delete(TABLE_NAME,"_id=?",new String[]{String.valueOf(id)});
       if (result ==-1){return false; }else {return  true;}

    }
    void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("DELETE  FROM " + TABLE_NAME);

    }
    Cursor getAllBooksById(String id) {
        String readALlQuery = "SELECT * FROM " + TABLE_NAME +" WHERE "+COLUMN_ID+" = '"+id+ "'";
        Log.e(TAG, "getAllBooksById: "+readALlQuery );
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (db != null) {
            c = db.rawQuery(readALlQuery, null);
            Log.e(TAG, "getAllBooksById: "+c.getCount());
        }
        return c;
    }
}
