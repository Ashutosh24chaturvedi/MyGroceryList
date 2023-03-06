package com.seeksolution.mygrocerylist.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.seeksolution.mygrocerylist.Activity.DetailsActivity;
import com.seeksolution.mygrocerylist.Data.DatabaseHandler;
import com.seeksolution.mygrocerylist.Model.Grocery;
import com.seeksolution.mygrocerylist.R;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent , false);

        return new ViewHolder(view , context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {

        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            groceryItemName = (TextView) itemView.findViewById(R.id.name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            dateAdded = (TextView) itemView.findViewById(R.id.dateAdded);
            editButton = (Button) itemView.findViewById(R.id.editButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next screen i.e DetailsActivity

                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("date",grocery.getDateItemAdded());

                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    editItem(grocery);

                    break;
                case R.id.deleteButton:
                    //to get id
                    position = getAdapterPosition();
                    grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());

                    break;

            }

        }

        public void deleteItem(int id){
                //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noBtn);
            Button yesButton = (Button) view.findViewById(R.id.yesBtn);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete the item
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }

        public void editItem (final Grocery grocery){

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view= inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
            final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);

            //title of popup
            final TextView title = (TextView) view.findViewById(R.id.title);
            title.setText("Edit Grocery Items");

            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    //update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if(!groceryItem.getText().toString().isEmpty()
                                && !quantity.getText().toString().isEmpty() ){

                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else{
                        Snackbar.make(view, "Add Grocery and Quantity ",Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });

        }
    }
}
