package deva.databaseexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private EditText e1,e2;
    private SQLiteDatabase myDatabase;
    private static final String LOG_TAG = "MainActivityDebug";

    public ListView l1;
    public ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG,"At onCreate");

        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);

        l1 = (ListView) findViewById(R.id.listView);

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

        Button delete_button = (Button) findViewById(R.id.button4);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry();
            }
        });
        Log.d(LOG_TAG,"onCreate finished");
    }

    private void write()
    {
        Log.d(LOG_TAG,"At write");
        ContentValues values = getContentValues();
        long row_id = myDatabase.insert(DBSchema.Table.TNAME,null,values);              // returns row id of newly inserted row otherwise -1 if error

        if(row_id==-1)
            Toast.makeText(MainActivity.this, "Insertion Error.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "Successfully added!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"write finished");
    }

    private void read()
    {
        Log.d(LOG_TAG,"At read");
        Cursor cursor = myQuery(null,null);
        try
        {
            if (cursor.moveToFirst())                       //Check if cursor is not NULL
            {
                Vector<String> StringArray = new Vector<String>();
                do
                {
                    Log.d(LOG_TAG, "Got id " + cursor.getString(0));

                    StringArray.addElement("" + cursor.getString(1));
                    StringArray.addElement("" + cursor.getString(2));

                } while (cursor.moveToNext());
                arrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,StringArray);
                l1.setAdapter(arrayAdapter);
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
        int count = myDatabase.update(DBSchema.Table.TNAME,values,
                DBSchema.Table.Cols.Person_NAME + " = ?",
                new String[] {nameString});                         //returns number of affected rows

        Toast.makeText(MainActivity.this, count + " rows updated successfully!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"updateEntry finished");
    }

    private void deleteEntry()
    {
        Log.d(LOG_TAG,"At deleteEntry");
        String nameString = e1.getText().toString();
        int count = myDatabase.delete(DBSchema.Table.TNAME,
                DBSchema.Table.Cols.Person_NAME + " = ?",
                new String[] {nameString});                         //returns number of affected rows

        Toast.makeText(MainActivity.this, count + " rows deleted successfully!", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"deleteEntry finished");
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
