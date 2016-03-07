package com.example.tinkazorge.problemset4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
/**
 * To do list: With this class the user can add tasks to a to do list and delete them
 * using a long click. A toast will pop up that says "Removed".
 */

public class IndividualListActivity extends Activity {

    //declare widgets
    Button addButton;
    EditText editItem;
    TextView itemText;

    //create array to store values from edittext
    ArrayList<String> item_input;

    //create adapter
    ArrayAdapter<String> arrayAdapterItems;

    //create context
    Context context_items;

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_list);
        context_items = IndividualListActivity.this;
        item_input = new ArrayList<String>();
        arrayAdapterItems = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item_input);

        //find widgets by ID
        final ListView listview2 = (ListView) findViewById(R.id.list_view_shapes);
        Button addbutton = (Button) findViewById(R.id.add_button);
        final EditText edit_item = (EditText) findViewById(R.id.item_edit);
        TextView itemtext = (TextView) findViewById(R.id.todo_items);

        //get text from edittext
        final String item = edit_item.getText().toString();

        //set data behind listview
        listview2.setAdapter(arrayAdapterItems);

        //save data in preferences
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        preferenceEditor.putString("item", item);
        preferenceEditor.commit();

        //if theres soomething to be saved
        if (preferenceEditor.commit()==true)
        {
            //retrieve string
            String anItem = preferenceSettings.getString("item", "anItem");
        };

        //when a user uses a long click
        listview2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //delete string at position
                item_input.remove(listview2.getItemAtPosition(position));

                //notify the adapter of data change
                arrayAdapterItems.notifyDataSetChanged();

                //let toast pop up with the text "removed"
                String text = "Removed";
                Toast toast = Toast.makeText(context_items, text, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            }
        });

        //when the adddbutton is clicked
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = edit_item.getText().toString();

                //add it to the list
                IndividualListActivity.this.item_input.add(item);

                //update the list
                IndividualListActivity.this.arrayAdapterItems.notifyDataSetChanged();

                //clear edittext
                edit_item.setText("");
            }
        });
    }

    private void getDatabasePath() {
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





