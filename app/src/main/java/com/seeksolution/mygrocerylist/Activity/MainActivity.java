package com.seeksolution.mygrocerylist.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.seeksolution.mygrocerylist.Data.DatabaseHandler;
import com.seeksolution.mygrocerylist.Model.Grocery;
import com.seeksolution.mygrocerylist.R;
import com.seeksolution.mygrocerylist.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //db intialization
        db= new DatabaseHandler(this);

        //move to ListActivity
        bypassActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Popup dialog box
    private void createPopupDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        quantity = (EditText) view.findViewById(R.id.groceryQty);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: save to db
                //todo:Go to next screen

                if(!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()){

                    saveGroceryToDb(view);

                }

            }
        });
    }

    private void saveGroceryToDb(View view){

        Grocery grocery = new Grocery();
        String newGrocery = groceryItem.getText().toString();
        String newGroceryQty = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQty);

        //save to db
        db.addGrocery(grocery);

        //snackbar is like toast
        Snackbar.make(view , "Item Saved: ", Snackbar.LENGTH_LONG).show();

//        Log.d("Item added ID:", String.valueOf(db.getGroceryCount()));
        Toast.makeText(this, "Item added ID: "+String.valueOf(db.getGroceryCount()), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                //start a new activity

                startActivity(new Intent(MainActivity.this , ListActivity.class));

            }
        },1200);
    }

    //if there is data saved in database we move to listActivity
    public void bypassActivity(){
        //checks if database is empty : if not , then we just
        //go to ListActivity and show all added items

        if(db.getGroceryCount() > 0 ){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }

}