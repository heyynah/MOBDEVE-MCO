package com.mobdeve.s20.teves.hannah.mco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbReferences.CREATE_TABLE_STATEMENT);
    }

    // Called when a new version of the DB is present; hence, an "upgrade" to a newer version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DbReferences.DROP_TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }

        return instance;
    }

    public ArrayList<HomeDailiesData> getAllTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.query(
                DbReferences.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbReferences.COLUMN_NAME_TIME + " ASC",
                null
        );
        ArrayList<HomeDailiesData> tasks = new ArrayList<>();
        while(c.moveToNext()) {
            tasks.add(new HomeDailiesData(
                    c.getLong(c.getColumnIndexOrThrow(DbReferences._ID)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_TIME)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_TASK))

            ));
        }
        return tasks;
    }

    public void insertDailies(String time, String task) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbReferences.COLUMN_NAME_TIME, time);
        values.put(DbReferences.COLUMN_NAME_TASK, task);
        database.insert(DbReferences.TABLE_NAME, null, values);

        database.close();
    }

    public void updateTask(long id, String time, String task) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbReferences.COLUMN_NAME_TIME, time);
        values.put(DbReferences.COLUMN_NAME_TASK, task);

        database.update(DbReferences.TABLE_NAME, values, DbReferences._ID + " = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void deleteTask(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DbReferences.TABLE_NAME, DbReferences._ID + " = " + id, null);
        database.close();
    }

    public long getLastInsertedId() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT last_insert_rowid()", null);
        long lastId = -1;
        if (cursor.moveToFirst()) {
            lastId = cursor.getLong(0);
        }
        cursor.close();
        database.close();
        return lastId;
    }

    private final class DbReferences {
        public static final String DATABASE_NAME = "dailies.db";
        public static final int DATABASE_VERSION = 1;
        public static final String TABLE_NAME = "dailies";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String _ID = "_id";
        public static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_TIME + " TEXT, " +
                COLUMN_NAME_TASK + " TEXT)";
        public static final String DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
