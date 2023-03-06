package com.seeksolution.mygrocerylist.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.seeksolution.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        quantity = (TextView) findViewById(R.id.quantityDet);
        dateAdded = (TextView) findViewById(R.id.dateaddedDet);

        //fatching intent extras send to us

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryid = bundle.getInt("id");

        }

    }
}