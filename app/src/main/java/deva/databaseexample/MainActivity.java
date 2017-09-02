package deva.databaseexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText e1,e2;
    private SQLiteDatabase myDatabase;
    public TextView t3,t4;
    private static final String LOG_TAG = "MainActivityDebug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG,"At onCreate");

        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);

        myDatabase = new BaseHelper(getApplicationContext())
                .getWritableDatabase();
        Button sbm_button = (Button) findViewById(R.id.button);
        sbm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });

        Button update_button = (Button) findViewById(R.id.button2);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEntry();
            }
        });

        Button read_button = (Button) findViewById(R.id.button3);
        read_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                read();
            }
        });
        Log.d(LOG_TAG,"onCreate finished");
    }

    private void write()
    {
        Log.d(LOG_TAG,"At write");
        ContentValues values = getContentValues();
        myDatabase.insert(DBSchema.Table.TNAME,null,values);

        Toast.makeText(MainActivity.this, "Submitted Successfully!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"write finished");
    }

    private void read()
    {
        Log.d(LOG_TAG,"At read");
        Cursor cursor = myQuery(null,null);
        try {
            if (cursor.moveToFirst())                       //Check if cursor is not NULL
            {
                do {
                    Log.d(LOG_TAG, "Got id " + cursor.getString(0));
                    t3.setText("" + cursor.getString(1));
                    t4.setText("" + cursor.getInt(2));

                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "Exception encountered. " + e);
        }
        finally
        {
            cursor.close();
        }
        Log.d(LOG_TAG,"read finished");
    }

    private Cursor myQuery(String whereClause,String[] whereArgs)
    {
        Log.d(LOG_TAG,"At myQuery");

        //String[] columns = new String[] { DBSchema.Table.Cols.Person_NAME, DBSchema.Table.Cols.Phone_NO };
        Cursor cursor = myDatabase.query
                (DBSchema.Table.TNAME,      // a. table
                        null,               // b. column names
                        whereClause,        // c. selections
                        whereArgs,          // d. selections args
                        null,               // e. group by
                        null,               // f. having
                        null,               // g. order by
                        null);

        Log.d(LOG_TAG,"myQuery finished");
        return cursor;
    }

    private void updateEntry()
    {
        Log.d(LOG_TAG,"At updateEntry");
        String nameString = e1.getText().toString();
        ContentValues values = getContentValues();
        myDatabase.update(DBSchema.Table.TNAME,values,
                DBSchema.Table.Cols.Phone_NO + " = ?",
                new String[] {nameString});

        Toast.makeText(MainActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"updateEntry finished");
    }


    private ContentValues getContentValues()            //Packs up info and forms a single row
    {
        Log.d(LOG_TAG,"At getContentValues");
        ContentValues values = new ContentValues();
        values.put(DBSchema.Table.Cols.Person_NAME,e1.getText().toString());
        values.put(DBSchema.Table.Cols.Phone_NO,e2.getText().toString());
        Log.d(LOG_TAG,"getContentValues finished");
        return values;
    }

}
