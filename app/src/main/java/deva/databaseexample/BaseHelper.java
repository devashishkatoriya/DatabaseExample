package deva.databaseexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static deva.databaseexample.DBSchema.*;

/*
 * Created by Devashish Katoriya on 10/13/16.
 */

public class BaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String LOG_TAG = "BaseHelperDebug";

    public BaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG,"At onCreate");

        sqLiteDatabase.execSQL("create table " + Table.TNAME + " ( " +
                " _id integer primary key autoincrement, " +
                Table.Cols.Person_NAME + ", " +
                Table.Cols.Phone_NO +
                " ) "
        );

        Log.d(LOG_TAG,"onCreate finished");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
