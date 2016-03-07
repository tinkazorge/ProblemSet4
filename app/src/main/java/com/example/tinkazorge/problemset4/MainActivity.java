package com.example.tinkazorge.problemset4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * To do list: In this class the user can add and remove lists using a long click. A toast will pop up that says "Removed". With a
 * short click the user can open the list to add items.
 */

public class MainActivity extends Activity {

    //declare widgets
    Button addButton;
    EditText list_edit;
    TextView listText;

    //create array to store values from edittext
    ArrayList<String> list_input;

    //create adapter
    ArrayAdapter<String> arrayAdapterMain;

    //create context
    Context context_lists;

    //
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find widgets by ID
        final ListView listview1 = (ListView) findViewById(R.id.list_view_shapes);
        Button addbutton = (Button) findViewById(R.id.add_button);
        final EditText list_edit = (EditText) findViewById(R.id.list_text);
        TextView listtext = (TextView) findViewById(R.id.todo_lists);

        //get text from edittext
        String list = list_edit.getText().toString();

        context_lists = MainActivity.this;
        list_input = new ArrayList<String>();
        arrayAdapterMain = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_input);

        //save data in preferences
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        preferenceEditor.putString("list", list);
        preferenceEditor.commit();

        //if there is something to save
        if (preferenceEditor.commit() == true)
        {
            //retrieve the string
          String aList = preferenceSettings.getString("list", "aList");
            System.out.println(aList);
        }

        //set data behind listview
        listview1.setAdapter(arrayAdapterMain);

        //when a user uses a short click
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //define next activity
                Intent individual_list = new Intent(MainActivity.this, IndividualListActivity.class);

                //start next activity
                startActivity(individual_list);
            }
        });

        //when a user uses a long click
        listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //delete string at position
                list_input.remove(listview1.getItemAtPosition(position));

                //notify the adapter of data change
                arrayAdapterMain.notifyDataSetChanged();

                //let toast pop up with the text "removed"
                String text = "Removed";
                Toast toast = Toast.makeText(context_lists, text, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            }
        });

        //when the adddbutton is clicked
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edittext
                String list = list_edit.getText().toString();

                //add it to the list
                MainActivity.this.list_input.add(list);

                //update the list
                MainActivity.this.arrayAdapterMain.notifyDataSetChanged();

                //clear edittext
                list_edit.setText("");
            }
        });
    }
    //if the user can clicks anywhere in the screen
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //and the focus is in edittext
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    //clear focus
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}


